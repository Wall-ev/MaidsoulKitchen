#!/bin/bash
set -eo pipefail  # 启用严格模式：出错立即退出，管道命令出错也退出

# ---------------------- 工具函数 ----------------------
# 日志函数（格式：[时间] 消息）
log() {
    local current_time=$(date +"%H:%M:%S.%N" | cut -c1-11)
    echo "[$current_time] $1"
}

# 错误处理函数
handle_error() {
    log "❌ 错误: $1"
    exit 1  # 错误退出，终止脚本
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

# 清理文件函数（参数：目录 文件名模式）
clean_files() {
    local clean_dir="$1"
    local pattern="$2"
    local deleted_files=0

    log "⚠️ 开始清理: ${clean_dir} (模式: ${pattern})"
    list_directory_files "${clean_dir}" "清理前的文件列表"

    # 检查目录是否存在
    if [ ! -d "${clean_dir}" ]; then
        log "⚠️ 目录不存在，跳过清理: ${clean_dir}"
        return 0
    fi

    # 关键修复：使用-print替代-print0，避免空字节问题
    # 同时设置LC_ALL=C确保文件名正确处理
    local files
    LC_ALL=C
    files=$(find "${clean_dir}" -maxdepth 1 -type f -name "${pattern}" -print)

    # 检查是否有匹配的文件
    if [ -z "${files}" ]; then
        log "⚠️ 未找到匹配模式 '${pattern}' 的文件"
        list_directory_files "${clean_dir}" "清理后的文件列表"
        return 0
    fi

    # 逐个处理文件（使用while循环和IFS避免空格问题）
    while IFS= read -r file; do
        # 跳过空行
        [ -z "${file}" ] && continue

        local filename=$(basename "${file}")
        log "删除: ${filename}"

        # 尝试删除并验证结果
        rm -f "${file}"
        if [ -f "${file}" ]; then
            # 检查文件权限
            if [ ! -w "${file}" ]; then
                handle_error "删除失败：没有权限 - ${filename}"
            else
                handle_error "删除失败：未知原因 - ${filename}"
            fi
        else
            log "✅ 已删除: ${filename}"
            ((deleted_files++))
        fi
    done <<< "${files}"

    log "✅ 成功删除 ${deleted_files} 个文件"
    list_directory_files "${clean_dir}" "清理后的文件列表"
    return 0
}

# 复制文件函数（参数：源目录 目标目录 文件名模式）
copy_files() {
    local source_dir="$1"
    local target_dir="$2"
    local pattern="$3"
    local copy_count=0

    log "⚠️ 开始复制文件: ${source_dir} (模式: ${pattern}) 到 ${target_dir}"
    # 复制前先列出源目录和目标目录的文件
    list_directory_files "${source_dir}" "源目录文件列表"
    list_directory_files "${target_dir}" "目标目录文件列表（复制前）"

    # 检查源目录是否存在
    if [ ! -d "${source_dir}" ]; then
        handle_error "源目录不存在 - ${source_dir}"
    fi

    # 创建目标目录（-p 确保父目录存在）
    mkdir -p "${target_dir}"
    if [ $? -ne 0 ]; then
        handle_error "无法创建目标目录 - ${target_dir}"
    fi

    # 关键修复：使用-print替代-print0，避免空字节问题
    # 统一与clean_files函数的文件处理方式
    local files
    LC_ALL=C
    files=$(find "${source_dir}" -maxdepth 1 -type f -name "${pattern}" -print)

    # 检查是否有匹配的文件
    if [ -z "${files}" ]; then
        handle_error "未找到匹配的文件进行复制 - ${source_dir}/${pattern}"
    fi

    # 逐个复制文件（使用while循环处理换行分隔的文件列表）
    while IFS= read -r file; do
        # 跳过空行
        [ -z "${file}" ] && continue

        local filename=$(basename "${file}")
        log "复制: ${filename} → ${target_dir}"

        # 执行复制并验证结果
        cp -f "${file}" "${target_dir}/"
        if [ $? -ne 0 ]; then
            handle_error "复制文件失败 - ${filename}"
        fi

        # 验证文件是否成功复制
        if [ -f "${target_dir}/${filename}" ]; then
            log "✅ 已复制: ${filename}"
            ((copy_count++))
        else
            handle_error "复制验证失败，目标文件不存在 - ${filename}"
        fi
    done <<< "${files}"

    log "✅ 成功复制 ${copy_count} 个文件"
    # 复制后列出目标目录的文件
    list_directory_files "${target_dir}" "目标目录文件列表（复制后）"
    return 0
}

# 执行Gradle任务函数（参数：工作目录 [任务名]）
run_gradle() {
    local gradle_dir="$1"
    local task="${2:-build}"  # 默认任务为 build（对应原bat的空参数处理）

    log "⚠️ 执行Gradle任务: ${task} 在目录 ${gradle_dir}"

    # 进入Gradle目录并执行任务（pushd/popd 保存/恢复当前目录）
    pushd "${gradle_dir}" &>/dev/null || handle_error "无法进入目录 - ${gradle_dir}"
    chmod +x ./gradlew  # 确保gradlew有可执行权限
    ./gradlew ${task}  # 执行Gradle任务
    local exit_code=$?
    popd &>/dev/null || true  # 恢复目录，即使失败也不终止

    if [ ${exit_code} -ne 0 ]; then
        handle_error "Gradle任务失败: ${task}"
    fi

    log "✅ Gradle任务完成: ${task}"
    return 0
}

# 计算耗时函数（参数：开始时间 结束时间，输出：HH:MM:SS.ss）
calculate_elapsed() {
    local start="$1"
    local end="$2"

    # 将时间转换为秒数（HH:MM:SS.ss → 总秒数，保留两位小数）
    local start_seconds=$(echo "${start}" | awk -F'[:.]' '{print $1*3600 + $2*60 + $3 + $4/100}')
    local end_seconds=$(echo "${end}" | awk -F'[:.]' '{print $1*3600 + $2*60 + $3 + $4/100}')

    # 计算差值（处理跨天情况，假设最大耗时不超过24小时）
    local elapsed_seconds=$(echo "${end_seconds} - ${start_seconds}" | bc)
    if (( $(echo "${elapsed_seconds} < 0" | bc -l) )); then
        elapsed_seconds=$(echo "${elapsed_seconds} + 86400" | bc)  # 加24小时
    fi

    # 转换回 HH:MM:SS.ss 格式
    local hours=$(echo "${elapsed_seconds} / 3600" | bc)
    local remaining=$(echo "${elapsed_seconds} % 3600" | bc)
    local minutes=$(echo "${remaining} / 60" | bc)
    local seconds=$(echo "${remaining} % 60" | bc)

    # 补前导零（确保两位格式）
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
    exit 0  # 成功退出
}

# 错误逻辑（由 || exit 1 触发，此处保留原bat的逻辑结构）
goto_error() {
    local end_time=$(date +"%H:%M:%S.%N" | cut -c1-11)
    local elapsed_time=$(calculate_elapsed "${START_TIME}" "${end_time}")
    log "❌❌❌ 构建过程中发生错误，脚本已终止 ❌❌❌"
    log "⚠️ 总耗时: ${elapsed_time}"
    exit 1
}


# --------------项目根目录---------------------
# 设置项目根目录（获取脚本所在目录，移除末尾斜杠）
PROJECT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd)
MINECRAFT_VERSION="1.20.1"
# --------------------------------------------

# 初始化计时变量（格式：HH:MM:SS.ss）
START_TIME=$(date +"%H:%M:%S.%N" | cut -c1-11)  # 截取到小数点后两位

# 主程序开始
log "========================================="
log "       MaidsoulKitchen 构建脚本 v2.0      "
log "========================================="

# 阶段1: 初始化设置
log "🚩 阶段1: 初始化设置"
SOURCE_SETTINGS="${PROJECT_DIR}/setting/common/settings.gradle"
TARGET_SETTINGS="${PROJECT_DIR}/settings.gradle"

if [ ! -f "${SOURCE_SETTINGS}" ]; then
    handle_error "源设置文件不存在 - ${SOURCE_SETTINGS}"
fi

cp -f "${SOURCE_SETTINGS}" "${TARGET_SETTINGS}"
if [ $? -ne 0 ]; then
    handle_error "复制设置文件失败"
fi
log "✅ 设置文件初始化完成"

# 阶段2: 清理残存文件
log "🚩 阶段2: 清理残存文件"
clean_files "${PROJECT_DIR}/Legacy/libs/${MINECRAFT_VERSION}/legacy" "*.jar" || exit 1
clean_files "${PROJECT_DIR}/Legacy/build/libs" "*.jar" || exit 1
clean_files "${PROJECT_DIR}/libs/${MINECRAFT_VERSION}/legacy" "*.jar" || exit 1
clean_files "${PROJECT_DIR}/build/libs" "*.jar" || exit 1

# 阶段3: 第一次构建
log "🚩 阶段3: 第一次构建"
run_gradle "${PROJECT_DIR}" || exit 1
run_gradle "${PROJECT_DIR}" "build --stacktrace" || exit 1

# 阶段4: 复制构建产物到Legacy
log "🚩 阶段4: 复制构建产物到Legacy"
copy_files "${PROJECT_DIR}/build/libs" "${PROJECT_DIR}/Legacy/libs/${MINECRAFT_VERSION}/legacy" "*-all.jar" || exit 1

# 阶段5: 构建Legacy项目
log "🚩 阶段5: 构建Legacy项目"
run_gradle "${PROJECT_DIR}/Legacy" || exit 1
run_gradle "${PROJECT_DIR}/Legacy" "build" || exit 1

# 阶段6: 复制Legacy构建产物
log "🚩 阶段6: 复制Legacy构建产物"
copy_files "${PROJECT_DIR}/Legacy/build/libs" "${PROJECT_DIR}/libs/${MINECRAFT_VERSION}/legacy" "*-all.jar" || exit 1

# 阶段7: 最终设置和构建
log "🚩 阶段7: 最终设置和构建"
SOURCE_ALL_SETTINGS="${PROJECT_DIR}/setting/common/settings-all.gradle"
TARGET_ALL_SETTINGS="${PROJECT_DIR}/settings.gradle"

if [ ! -f "${SOURCE_ALL_SETTINGS}" ]; then
    handle_error "源设置文件不存在 - ${SOURCE_ALL_SETTINGS}"
fi

# 复制最终设置文件
cp -f "${SOURCE_ALL_SETTINGS}" "${TARGET_ALL_SETTINGS}"
if [ $? -ne 0 ]; then
    handle_error "复制设置文件失败"
fi

run_gradle "${PROJECT_DIR}" || exit 1

# 跳转到成功逻辑
goto_success