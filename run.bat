@echo off
REM 检查参数是否传入
if "%~1"=="" (
    echo 用法: run.bat [offline|streaming]
    exit /b 1
)

REM 运行Jar包，传入参数
java -jar target\spark-ml-platform-1.0.jar %1

pause
