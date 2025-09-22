#!/bin/bash
set -eo pipefail  # 启用严格模式，出错立即退出

# ---------------------- 工具函数 ----------------------

# 日志函数（格式：[时间] 消息）
log() {
    local current_time=$(date +"%H:%M:%S.%N" | cut -c1-11)
    echo "[$current_time] $1"
}

# 错误处理函数
handle_error() {
    log "❌ 错误: $1"
    exit 1
}

# 列出目录中的所有文件（辅助函数）
list_directory_files() {
    local dir="$1"
    local title="$2"

    if [ ! -d "${dir}" ]; then
        log "📂 ${title} 目录不存在: ${dir}"
        return
    fi

    log "📂 ${title} (${dir}):"
    # 列出所有文件和子目录，使用缩进和图标区分
    local items=$(ls -1 "${dir}" 2>/dev/null || true)
    if [ -z "${items}" ]; then
        log "  └── 目录为空"
        return
    fi

    local count=0
    local total=$(echo "${items}" | wc -l)
    while IFS= read -r item; do
        ((count++))
        if [ -d "${dir}/${item}" ]; then
            prefix="📁"  # 目录图标
        else
            prefix="📄"  # 文件图标
        fi
        if [ ${count} -eq ${total} ]; then
            log "  └── ${prefix} ${item}"
        else
            log "  ├── ${prefix} ${item}"
        fi
    done <<< "${items}"
}

# 修复后的清理旧版本JAR文件函数
clean_old_jars() {
    local jar_dir="$1"
    local pattern="$2"
    local current_jar="$3"
    local deleted_files=0

    # 显示清理上下文信息，便于调试
    log "⚠️ 开始清理旧版本JAR文件: ${jar_dir}"
    log "📌 匹配模式: ${pattern}"
    log "📌 需保留的当前版本: ${current_jar}"
    list_directory_files "${jar_dir}" "清理前的文件列表"

    # 检查目录是否存在
    if [ ! -d "${jar_dir}" ]; then
        log "⚠️ 目标目录不存在，跳过清理: ${jar_dir}"
        return 0
    fi

    # 关键修复：使用find命令确保正确匹配文件，避免通配符解析问题
    local files
    LC_ALL=C
    files=$(find "${jar_dir}" -maxdepth 1 -type f -name "${pattern}" -print)

    # 处理无匹配文件的情况
    if [ -z "${files}" ]; then
        log "ℹ️ 未找到匹配模式 '${pattern}' 的文件"
        list_directory_files "${jar_dir}" "清理后的文件列表"
        return 0
    fi

    # 逐个处理文件
    while IFS= read -r file; do
        # 跳过空行
        [ -z "${file}" ] && continue

        local filename=$(basename "${file}")

        # 关键修复：使用正确的模式匹配逻辑
        log "🔍 正在判断: ${filename} 是否需要保留"
        if [[ "${filename}" != ${current_jar} ]]; then
            log "🗑️ 准备删除: ${filename}"

            # 尝试删除并验证结果
            rm -f "${file}"
            if [ -f "${file}" ]; then
                # 检查是否因权限问题导致删除失败
                if [ ! -w "${file}" ]; then
                    handle_error "删除失败：没有文件写入权限 - ${filename}"
                else
                    handle_error "删除失败：未知原因 - ${filename}"
                fi
            fi

            log "✅ 已删除: ${filename}"
            ((deleted_files++))
        else
            log "🔒 保留当前版本: ${filename}"
        fi
    done <<< "${files}"

    # 总结清理结果
    if [ ${deleted_files} -gt 0 ]; then
        log "📊 清理完成：共删除 ${deleted_files} 个旧版本文件"
    else
        log "📊 没有需要删除的旧版本文件"
    fi

    # 显示清理后的目录状态
    list_directory_files "${jar_dir}" "清理后的文件列表"
    return 0
}


# 修复后的复制文件函数
copy_files() {
    local source="$1"
    local target_dir="$2"
    local source_dir=$(dirname "${source}")
    local filename=$(basename "${source}")

    log "⚠️ 开始复制文件: ${filename} 到 ${target_dir}"
    # 复制前显示源文件所在目录和目标目录
    list_directory_files "${source_dir}" "源文件所在目录"
    list_directory_files "${target_dir}" "目标目录（复制前）"

    # 检查源文件是否存在
    if [ ! -f "${source}" ]; then
        handle_error "源文件不存在 - ${source}"
    fi

    # 创建目标目录（如果不存在）
    mkdir -p "${target_dir}"
    if [ $? -ne 0 ]; then
        handle_error "无法创建目标目录 - ${target_dir}"
    fi

    # 复制文件并验证结果
    log "复制: ${filename} → ${target_dir}"
    cp -f "${source}" "${target_dir}/"
    if [ $? -ne 0 ]; then
        handle_error "复制文件失败 - ${filename}"
    fi

    # 验证文件是否成功复制
    if [ -f "${target_dir}/${filename}" ]; then
        log "✅ 已复制: ${filename}"
    else
        handle_error "复制验证失败，目标文件不存在 - ${filename}"
    fi

    # 复制后显示目标目录
    list_directory_files "${target_dir}" "目标目录（复制后）"
    return 0
}

# 执行Gradle任务函数
run_gradle() {
    local gradle_dir="$1"
    local task="${2:-build}"  # 默认任务为build

    log "⚠️ 执行Gradle任务: ${task} 在目录 ${gradle_dir}"

    # 进入Gradle目录并执行任务
    pushd "${gradle_dir}" &>/dev/null || handle_error "无法进入目录 - ${gradle_dir}"

    # 确保gradlew有可执行权限
    chmod +x ./gradlew

    # 执行Gradle任务，如果是properties任务则提取所需属性
    if [ "${task}" = "properties" ]; then
        ./gradlew ${task} | tee /tmp/gradle_properties.txt

        # 从输出中提取所需属性
        MOD_ID=$(grep -oP 'mod_id:\s*\K.*' /tmp/gradle_properties.txt | head -n1 | xargs)
        VERSION=$(grep -oP 'version:\s*\K.*' /tmp/gradle_properties.txt | head -n1 | xargs)
        MINECRAFT_VERSION=$(grep -oP 'minecraft_version:\s*\K.*' /tmp/gradle_properties.txt | head -n1 | xargs)

        # 验证属性是否成功提取
        if [ -z "${MOD_ID}" ] || [ -z "${VERSION}" ] || [ -z "${MINECRAFT_VERSION}" ]; then
            popd &>/dev/null || true
            handle_error "无法从Gradle properties中提取必要属性"
        fi
    else
        ./gradlew ${task}
    fi

    local exit_code=$?
    popd &>/dev/null || true  # 恢复目录

    if [ ${exit_code} -ne 0 ]; then
        handle_error "Gradle任务失败: ${task}"
    fi

    log "✅ Gradle任务完成: ${task}"
    return 0
}

# 计算耗时函数
calculate_elapsed() {
    local start="$1"
    local end="$2"

    # 将时间转换为秒数（HH:MM:SS.ss → 总秒数，保留两位小数）
    local start_seconds=$(echo "${start}" | awk -F'[:.]' '{print $1*3600 + $2*60 + $3 + $4/100}')
    local end_seconds=$(echo "${end}" | awk -F'[:.]' '{print $1*3600 + $2*60 + $3 + $4/100}')

    # 计算差值（处理跨天情况）
    local elapsed_seconds=$(echo "${end_seconds} - ${start_seconds}" | bc)
    if (( $(echo "${elapsed_seconds} < 0" | bc -l) )); then
        elapsed_seconds=$(echo "${elapsed_seconds} + 86400" | bc)  # 加24小时
    fi

    # 转换回 HH:MM:SS.ss 格式
    local hours=$(echo "${elapsed_seconds} / 3600" | bc)
    local remaining=$(echo "${elapsed_seconds} % 3600" | bc)
    local minutes=$(echo "${remaining} / 60" | bc)
    local seconds=$(echo "${remaining} % 60" | bc)

    # 补前导零
    printf "%02d:%02d:%05.2f" "${hours}" "${minutes}" "${seconds}"
}

# 成功逻辑
goto_success() {
    local end_time=$(date +"%H:%M:%S.%N" | cut -c1-11)
    local elapsed_time=$(calculate_elapsed "${START_TIME}" "${end_time}")
    log "========================================="
    log "          ✅ 所有任务已成功完成!            "
    log "        🎉 总耗时: ${elapsed_time}         "
    log "========================================="
    exit 0
}

# 错误逻辑
goto_error() {
    local end_time=$(date +"%H:%M:%S.%N" | cut -c1-11)
    local elapsed_time=$(calculate_elapsed "${START_TIME}" "${end_time}")
    log "❌❌❌ 构建过程中发生错误，脚本已终止 ❌❌❌"
    log "⚠️ 总耗时: ${elapsed_time}"
    exit 1
}

# --------------项目根目录---------------------
# 设置项目根目录（获取脚本所在目录）
PROJECT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd)
# --------------------------------------------

# 初始化计时变量（格式：HH:MM:SS.ss）
START_TIME=$(date +"%H:%M:%S.%N" | cut -c1-11)

# 初始化全局变量
MOD_ID=""
VERSION=""
MINECRAFT_VERSION=""

# 主程序开始
log "========================================="
log "      MaidsoulKitchen Legacy构建脚本      "
log "========================================="

# 阶段1: 获取项目属性
log "🚩 阶段1: 获取项目属性"
run_gradle "${PROJECT_DIR}" "properties" || goto_error
log "⚠️ 从Gradle获取的项目属性: MOD_ID=${MOD_ID}, VERSION=${VERSION}, MINECRAFT_VERSION=${MINECRAFT_VERSION}"

# 阶段2: 清理Build旧版本JAR文件
log "🚩 阶段2: 清理Build旧版本JAR文件"
clean_old_jars "${PROJECT_DIR}/build/libs/" "${MOD_ID}*.jar" "*.jar" || goto_error

# 阶段3: 执行Gradle构建
log "🚩 阶段3: 执行Gradle构建"
run_gradle "${PROJECT_DIR}" "build --stacktrace" || goto_error

# 阶段4: 复制JAR文件到Legacy项目
log "🚩 阶段4: 复制JAR文件到Legacy项目"
SOURCE_JAR="${PROJECT_DIR}/build/libs/${MOD_ID}-${VERSION}-all.jar"
TARGET_DIR="${PROJECT_DIR}/Legacy/libs/${MINECRAFT_VERSION}/legacy"
copy_files "${SOURCE_JAR}" "${TARGET_DIR}" || goto_error

# 阶段5: 清理旧版本JAR文件
log "🚩 阶段5: 清理旧版本JAR文件"
clean_old_jars "${PROJECT_DIR}/Legacy/libs/${MINECRAFT_VERSION}/legacy" "${MOD_ID}*.jar" "${MOD_ID}-${VERSION}-all.jar" || goto_error

# 阶段6: 重载Legacy依赖
log "🚩 阶段6: 重载Legacy依赖"
run_gradle "${PROJECT_DIR}/Legacy" || goto_error

goto_success