@echo off
setlocal

set "GRADLE_VERSION=8.12.1"
set "CACHE_DIR=%LOCALAPPDATA%\AggroBossBarsBuild"
set "GRADLE_HOME=%CACHE_DIR%\gradle-%GRADLE_VERSION%"
set "GRADLE_ZIP=%CACHE_DIR%\gradle-%GRADLE_VERSION%-bin.zip"

where java >nul 2>nul
if errorlevel 1 (
    echo Java was not found. Install a Java 17 JDK, then reopen this file.
    pause
    exit /b 1
)

if not exist "%GRADLE_HOME%\bin\gradle.bat" (
    echo Downloading Gradle %GRADLE_VERSION%...
    if not exist "%CACHE_DIR%" mkdir "%CACHE_DIR%"

    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
      "$ErrorActionPreference='Stop'; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%GRADLE_ZIP%'"
    if errorlevel 1 goto :failed

    echo Extracting Gradle...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
      "$ErrorActionPreference='Stop'; Expand-Archive -Path '%GRADLE_ZIP%' -DestinationPath '%CACHE_DIR%' -Force"
    if errorlevel 1 goto :failed
)

echo Building Aggro Boss Bars...
call "%GRADLE_HOME%\bin\gradle.bat" clean build
if errorlevel 1 goto :failed

echo.
echo Build complete.
echo Jar: build\libs\aggro-boss-bars-1.0.1.jar
pause
exit /b 0

:failed
echo.
echo Build failed. Copy the full error from this window so we can troubleshoot it.
pause
exit /b 1
