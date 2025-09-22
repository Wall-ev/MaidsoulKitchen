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

# 修复后的复制文件函数
copy_files() {
    local source="$1"
    local target_dir="$2"
    local source_dir=$(dirname "${source}")
    local filename=$(basename "${source}")

    log "⚠️ 开始复制文件: ${filename} 到 ${target_dir}"
    list_directory_files "${source_dir}" "源文件所在目录"
    list_directory_files "${target_dir}" "目标目录（复制前）"

    if [ ! -f "${source}" ]; then
        handle_error "源文件不存在 - ${source}"
    fi

    mkdir -p "${target_dir}"
    if [ $? -ne 0 ]; then
        handle_error "无法创建目标目录 - ${target_dir}"
    fi

    log "正在复制: ${filename} → ${target_dir}"
    cp -f "${source}" "${target_dir}/"
    if [ $? -ne 0 ]; then
        handle_error "复制命令执行失败 - ${source}"
    fi

    if [ -f "${target_dir}/${filename}" ]; then
        log "✅ 文件复制成功: ${filename}"
    else
        handle_error "复制验证失败，目标文件不存在 - ${target_dir}/${filename}"
    fi

    list_directory_files "${target_dir}" "目标目录（复制后）"
    return 0
}

# 执行Gradle任务函数 - 关键修复：优先使用环境变量中的版本号
run_gradle() {
    local gradle_dir="$1"
    local task="${2:-build}"  # 默认任务为build

    log "⚠️ 执行Gradle任务: ${task} 在目录 ${gradle_dir}"

    pushd "${gradle_dir}" &>/dev/null || handle_error "无法进入目录 - ${gradle_dir}"
    chmod +x ./gradlew

    # 关键修复：如果环境变量中已有版本信息，则直接使用
    if [ "${task}" = "properties" ] && [ -n "${MOD_ID}" ] && [ -n "${VERSION}" ] && [ -n "${MINECRAFT_VERSION}" ]; then
        log "ℹ️ 使用环境变量中的版本属性: MOD_ID=${MOD_ID}, VERSION=${VERSION}, MINECRAFT_VERSION=${MINECRAFT_VERSION}"
    else
        # 只有在没有环境变量时才从Gradle输出提取
        ./gradlew ${task} | tee /tmp/gradle_properties.txt

        if [ "${task}" = "properties" ]; then
            # 从输出中提取所需属性
            MOD_ID=$(grep -oP 'mod_id:\s*\K.*' /tmp/gradle_properties.txt | head -n1 | xargs)
            VERSION=$(grep -oP 'version:\s*\K.*' /tmp/gradle_properties.txt | head -n1 | xargs)
            MINECRAFT_VERSION=$(grep -oP 'minecraft_version:\s*\K.*' /tmp/gradle_properties.txt | head -n1 | xargs)

            # 验证属性是否成功提取
            if [ -z "${MOD_ID}" ] || [ -z "${VERSION}" ] || [ -z "${MINECRAFT_VERSION}" ]; then
                popd &>/dev/null || true
                handle_error "无法从Gradle properties中提取必要属性"
            fi
        fi
    fi

    local exit_code=$?
    popd &>/dev/null || true

    if [ ${exit_code} -ne 0 ]; then
        handle_error "Gradle任务失败: ${task}"
    fi

    log "✅ Gradle任务完成: ${task}"
    return 0
}

# 修复后的清理旧版本JAR文件函数
clean_old_jars() {
    local jar_dir="$1"
    local pattern="$2"
    local current_jar="$3"
    local deleted_files=0

    log "⚠️ 开始清理旧版本JAR文件: ${jar_dir} (模式: ${pattern})"
    log "⚠️ 当前保留版本: ${current_jar}"
    list_directory_files "${jar_dir}" "清理前的文件列表"

    if [ ! -d "${jar_dir}" ]; then
        log "⚠️ 目标目录不存在，跳过删除操作: ${jar_dir}"
        return 0
    fi

    local files
    LC_ALL=C
    files=$(find "${jar_dir}" -maxdepth 1 -type f -name "${pattern}" -print)

    if [ -z "${files}" ]; then
        log "⚠️ 未找到匹配模式 ${pattern} 的文件"
        list_directory_files "${jar_dir}" "清理后的文件列表"
        return 0
    fi

    while IFS= read -r file; do
        [ -z "${file}" ] && continue

        local filename=$(basename "${file}")

        log "🔍 比较: 文件名=${filename} 保留版本=${current_jar}"
        if [[ "${filename}" != ${current_jar} ]]; then
            log "删除: ${filename}"
            rm -f "${file}"

            if [ -f "${file}" ]; then
                if [ ! -w "${file}" ]; then
                    handle_error "删除失败：没有权限 - ${filename}"
                else
                    handle_error "删除失败：文件依然存在 - ${filename}"
                fi
            fi
            ((deleted_files++))
        else
            log "保留: ${filename}（当前版本）"
        fi
    done <<< "${files}"

    if [ ${deleted_files} -gt 0 ]; then
        log "✅ 成功删除 ${deleted_files} 个旧版本JAR文件"
    else
        log "⚠️ 未找到需要删除的旧版本JAR文件"
    fi

    list_directory_files "${jar_dir}" "清理后的文件列表"
    return 0
}

# 计算耗时函数
calculate_elapsed() {
    local start="$1"
    local end="$2"

    local start_seconds=$(echo "${start}" | awk -F'[:.]' '{print $1*3600 + $2*60 + $3 + $4/100}')
    local end_seconds=$(echo "${end}" | awk -F'[:.]' '{print $1*3600 + $2*60 + $3 + $4/100}')

    local elapsed_seconds=$(echo "${end_seconds} - ${start_seconds}" | bc)
    if (( $(echo "${elapsed_seconds} < 0" | bc -l) )); then
        elapsed_seconds=$(echo "${elapsed_seconds} + 86400" | bc)
    fi

    local hours=$(echo "${elapsed_seconds} / 3600" | bc)
    local remaining=$(echo "${elapsed_seconds} % 3600" | bc)
    local minutes=$(echo "${remaining} / 60" | bc)
    local seconds=$(echo "${remaining} % 60" | bc)

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
PROJECT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd)
# --------------------------------------------

# 初始化计时变量
START_TIME=$(date +"%H:%M:%S.%N" | cut -c1-11)

# 关键修复：优先使用环境变量中的版本信息，如果不存在则从Gradle获取
if [ -z "${MOD_ID}" ] || [ -z "${VERSION}" ] || [ -z "${MINECRAFT_VERSION}" ]; then
    log "⚠️ 环境变量中未找到版本信息，将从Gradle获取"
else
    log "⚠️ 使用环境变量中的版本信息: MOD_ID=${MOD_ID}, VERSION=${VERSION}, MINECRAFT_VERSION=${MINECRAFT_VERSION}"
fi

# 主程序开始
log "========================================="
log "      MaidsoulKitchen 双项目构建脚本      "
log "========================================="

# 阶段1: 获取项目属性 - 现在会优先使用环境变量
log "🚩 阶段1: 获取项目属性"
run_gradle "${PROJECT_DIR}" "properties" || goto_error
log "⚠️ 最终使用的项目属性: MOD_ID=${MOD_ID}, VERSION=${VERSION}, MINECRAFT_VERSION=${MINECRAFT_VERSION}"

# 阶段2: 生成主项目数据
log "🚩 阶段2: 生成主项目数据，用于打包"
run_gradle "${PROJECT_DIR}" "runData" || goto_error

# 阶段3: 清理Build旧版本JAR文件
log "🚩 阶段3: 清理主项目的Build旧版本JAR文件"
clean_old_jars "${PROJECT_DIR}/build/libs/" "${MOD_ID}*.jar" "${MOD_ID}-${VERSION}*.jar" || goto_error

# 阶段4: 第一次构建
log "🚩 阶段4: 构建主项目"
run_gradle "${PROJECT_DIR}" "build" || goto_error

# 阶段5: 清理Legacy的Build旧版本JAR文件
log "🚩 阶段5: 清理Legacy的Build旧版本JAR文件"
clean_old_jars "${PROJECT_DIR}/Legacy/build/libs/" "${MOD_ID}_legacy*.jar" "${MOD_ID}_legacy-${VERSION}*.jar" || goto_error

# 阶段6: 构建Legacy项目
log "🚩 阶段6: 构建Legacy项目"
run_gradle "${PROJECT_DIR}/Legacy" "build" || goto_error

# 阶段7: 复制Legacy构建产物到主项目
log "🚩 阶段7: 复制Legacy构建产物到主项目"
SOURCE_JAR="${PROJECT_DIR}/Legacy/build/libs/${MOD_ID}_legacy-${VERSION}-all.jar"
TARGET_DIR="${PROJECT_DIR}/libs/${MINECRAFT_VERSION}/legacy"
copy_files "${SOURCE_JAR}" "${TARGET_DIR}" || goto_error

# 阶段8: 清理旧版本JAR文件
log "🚩 阶段8: 清理旧版本JAR文件"
clean_old_jars "${PROJECT_DIR}/libs/${MINECRAFT_VERSION}/legacy" "${MOD_ID}_legacy*.jar" "${MOD_ID}_legacy-${VERSION}-all.jar" || goto_error
clean_old_jars "${PROJECT_DIR}/Legacy/libs/${MINECRAFT_VERSION}/legacy" "${MOD_ID}*.jar" "${MOD_ID}-${VERSION}-all.jar" || goto_error

# 阶段9: 构建并合并项目
log "🚩 阶段9: 构建并合并项目"
run_gradle "${PROJECT_DIR}" "buildAll" || goto_error

goto_success