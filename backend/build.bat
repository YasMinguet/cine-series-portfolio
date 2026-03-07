@echo off
REM Script simple para compilar con Maven y levantar con Docker
setlocal enabledelayedexpansion

set MAVEN=C:\Tools\apache-maven-3.9.12\bin\mvn.cmd
set PROJECT_PATH=%cd%

echo.
echo ================================================
echo  Compilando Cine Series Portfolio
echo ================================================
echo.

REM Compilar
echo [1/2] Compilando con Maven...
call %MAVEN% clean package -DskipTests -q

if %errorlevel% neq 0 (
    echo [ERROR] Fallo la compilacion
    pause
    exit /b 1
)

echo [OK] Compilacion completada
echo.
echo [2/2] Para levantar los servicios, ejecuta:
echo.
echo     docker-compose up --build
echo.
pause

