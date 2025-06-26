@echo off
setlocal enabledelayedexpansion

:: --------------项目根目录---------------------
:: 设置项目根目录
set "PROJECT_DIR=%~dp0"
:: 移除末尾的反斜杠
set "PROJECT_DIR=!PROJECT_DIR:~0,-1!"
:: --------------------------------------------

:: 初始化计时变量
set "START_TIME=%TIME%"

:: 主程序开始
call :log "========================================="
call :log "      MaidsoulKitchen 双项目构建脚本      "
call :log "========================================="

:: 阶段1: 获取项目属性
call :log "🚩 阶段1: 获取项目属性"
call :run_gradle "!PROJECT_DIR!" "properties" || goto :error
call :log "⚠️ 从Gradle获取的项目属性: MOD_ID=%MOD_ID%, VERSION=%VERSION%, MINECRAFT_VERSION=%MINECRAFT_VERSION%"

:: 阶段2: 生成主项目数据，用于打包
call :log "🚩 阶段2: 生成主项目数据，用于打包"
call :run_gradle "!PROJECT_DIR!" "runData" || goto :error

:: 阶段2: 清理Build旧版本JAR文件
call :log "🚩 阶段3: 清理主项目的Build旧版本JAR文件"
call :clean_old_jars "!PROJECT_DIR!\build\libs\" "%MOD_ID%*.jar" "%MOD_ID%-%VERSION%*.jar" || goto :error

:: 阶段3: 第一次构建
call :log "🚩 阶段4: 构建主项目"
call :run_gradle "!PROJECT_DIR!" "build" || goto :error

:: 阶段2: 清理Legacy的Build旧版本JAR文件
call :log "🚩 阶段5: 清理Legacy的Build旧版本JAR文件"
call :clean_old_jars "!PROJECT_DIR!\Legacy\build\libs\" "%MOD_ID%_legacy*.jar" "%MOD_ID%_legacy-%VERSION%*.jar" || goto :error

:: 阶段2: 构建Legacy项目
call :log "🚩 阶段6: 构建Legacy项目"
call :run_gradle "!PROJECT_DIR!\Legacy" "build" || goto :error

:: 阶段3: 复制Legacy构建产物到主项目
call :log "🚩 阶段7: 复制Legacy构建产物到主项目"
set "SOURCE_JAR=!PROJECT_DIR!\Legacy\build\libs\%MOD_ID%_legacy-%VERSION%-all.jar"
set "TARGET_DIR=!PROJECT_DIR!\libs\%MINECRAFT_VERSION%\legacy"
call :copy_files "!SOURCE_JAR!" "!TARGET_DIR!" || goto :error

:: 阶段4: 清理旧版本JAR文件
call :log "🚩 阶段8: 清理旧版本JAR文件"
call :clean_old_jars "!PROJECT_DIR!\libs\%MINECRAFT_VERSION%\legacy" "%MOD_ID%_legacy*.jar" "%MOD_ID%_legacy-%VERSION%-all.jar" || goto :error
call :clean_old_jars "!PROJECT_DIR!\Legacy\libs\%MINECRAFT_VERSION%\legacy" "%MOD_ID%*.jar" "%MOD_ID%-%VERSION%-all.jar" || goto :error

:: 阶段4: 构建主项目
call :log "🚩 阶段9: 构建并合并项目"
call :run_gradle "!PROJECT_DIR!" "buildAll" || goto :error

goto :success

:: util
:: 日志函数
:log
    echo [%TIME%] %~1
    exit /b 0

:: 错误处理函数
:handle_error
    call :log "❌ 错误: %~1"
    exit /b 1

:: 复制文件函数
:copy_files
    set "SOURCE=%~1"
    set "TARGET_DIR=%~2"

    call :log "⚠️ 开始复制文件: !SOURCE! 到 !TARGET_DIR!"

    :: 检查源文件是否存在
    if not exist "!SOURCE!" (
        call :handle_error "源文件不存在 - !SOURCE!"
    )

    :: 创建目标目录（如果不存在）
    if not exist "!TARGET_DIR!" (
        mkdir "!TARGET_DIR!"
        if errorlevel 1 (
            call :handle_error "无法创建目标目录 - !TARGET_DIR!"
        )
    )

    :: 复制文件
    copy /Y "!SOURCE!" "!TARGET_DIR!" >nul
    if errorlevel 1 (
        call :handle_error "复制文件失败 - !SOURCE!"
    )

    call :log "✅ 文件复制成功: !SOURCE!"
    exit /b 0

:: 执行Gradle任务函数
:run_gradle
    set "GRADLE_DIR=%~1"
    set "TASK=%~2"

    call :log "⚠️ 执行Gradle任务: !TASK! 在目录 !GRADLE_DIR!"

    pushd "!GRADLE_DIR!"
    call "!GRADLE_DIR!\gradlew" %TASK%
    if errorlevel 1 (
        popd
        call :handle_error "Gradle任务失败: !TASK!"
    )
    popd

    call :log "✅ Gradle任务完成: !TASK!"
    exit /b 0


:: 清理旧版本JAR文件函数
:clean_old_jars
    set "JAR_DIR=%~1"
    set "PATTERN=%~2"
    set "CURRENT_JAR=%~3"
    set "DELETED_FILES=0"

    call :log "⚠️ 开始清理旧版本JAR文件: !JAR_DIR!"

    :: 检查目标目录是否存在
    if not exist "!JAR_DIR!" (
        call :log "⚠️ 目标目录不存在，跳过删除操作: !JAR_DIR!"
        exit /b 0
    )

    :: 使用for循环遍历所有匹配的文件并排除当前版本
    for %%f in ("!JAR_DIR!\%PATTERN%") do (
        if /i not "%%~nxf"=="%CURRENT_JAR%" (
            call :log "删除: %%~nxf"
            del /Q "%%f"
            if errorlevel 1 (
                call :handle_error "删除文件失败 - %%~nxf"
            )
            set /a "DELETED_FILES+=1"
        )
    )

    if !DELETED_FILES! GTR 0 (
        call :log "✅ 成功删除 !DELETED_FILES! 个旧版本JAR文件"
    ) else (
        call :log "⚠️ 未找到需要删除的旧版本JAR文件"
    )

    exit /b 0


:: 计算两个时间点之间的差值
:calculate_elapsed
    set "START=%~1"
    set "END=%~2"
    set "RESULT_VAR=%~3"

    :: 确保时间格式为 HH:MM:SS.XX（补全前导零）
    if "!START:~1,1!"==":" set "START=0!START!"
    if "!END:~1,1!"==":" set "END=0!END!"

    :: 将时间转换为100纳秒为单位
    set "START_HH=!START:~0,2!"
    set "START_MM=!START:~3,2!"
    set "START_SS=!START:~6,2!"
    set "START_MS=!START:~9,2!"

    set "END_HH=!END:~0,2!"
    set "END_MM=!END:~3,2!"
    set "END_SS=!END:~6,2!"
    set "END_MS=!END:~9,2!"

    :: 转换为毫秒
    set /a "START_TOTAL=!START_HH!*360000+!START_MM!*6000+!START_SS!*100+!START_MS!"
    set /a "END_TOTAL=!END_HH!*360000+!END_MM!*6000+!END_SS!*100+!END_MS!"

    :: 计算差值
    set /a "ELAPSED=!END_TOTAL!-!START_TOTAL!"
    if !ELAPSED! LSS 0 set /a "ELAPSED=!ELAPSED!+8640000"

    :: 转换回时:分:秒.毫秒格式
    set /a "ELAPSED_HH=!ELAPSED!/360000"
    set /a "ELAPSED_REST=!ELAPSED!%%360000"
    set /a "ELAPSED_MM=!ELAPSED_REST!/6000"
    set /a "ELAPSED_REST=!ELAPSED_REST!%%6000"
    set /a "ELAPSED_SS=!ELAPSED_REST!/100"
    set /a "ELAPSED_MS=!ELAPSED_REST!%%100"

    :: 格式化结果
    if !ELAPSED_HH! LSS 10 set "ELAPSED_HH=0!ELAPSED_HH!"
    if !ELAPSED_MM! LSS 10 set "ELAPSED_MM=0!ELAPSED_MM!"
    if !ELAPSED_SS! LSS 10 set "ELAPSED_SS=0!ELAPSED_SS!"
    if !ELAPSED_MS! LSS 10 set "ELAPSED_MS=0!ELAPSED_MS!"

    set "%RESULT_VAR%=!ELAPSED_HH!:!ELAPSED_MM!:!ELAPSED_SS!.!ELAPSED_MS!"
    exit /b 0

:error
    call :calculate_elapsed "!START_TIME!" "%TIME%" "ELAPSED_TIME"
    call :log "❌❌❌ 构建过程中发生错误，脚本已终止 ❌❌❌"
    call :log "⚠️ 总耗时: !ELAPSED_TIME!"
    exit /b 1

:success
    call :calculate_elapsed "!START_TIME!" "%TIME%" "ELAPSED_TIME"
    call :log "========================================="
    call :log "          ✅ 所有任务已成功完成!            "
    call :log "        🎉 总耗时: !ELAPSED_TIME!         "
    call :log "========================================="
    exit /b 0

exit /b 0