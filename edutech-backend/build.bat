@echo off
setlocal
set "JAVA_TOOL_OPTIONS="
set "MAVEN_OPTS="

echo Limpiando proyecto...
call mvn clean -q

echo Compilando proyecto...
call mvn compile -q

if %ERRORLEVEL% EQU 0 (
    echo Compilacion exitosa
) else (
    echo Error en la compilacion
    exit /b %ERRORLEVEL%
)
