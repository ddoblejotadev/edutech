@echo off
setlocal

REM Limpiar completamente todas las variables problemáticas
set "JAVA_TOOL_OPTIONS="
set "MAVEN_OPTS="
set "_JAVA_OPTIONS="

REM Mostrar lo que estamos haciendo
echo Limpiando proyecto edutech-backend...
echo.

REM Ir al directorio correcto
cd /d "c:\Users\CETECOM\Desktop\edutech\edutech-backend"

REM Ejecutar Maven clean directamente
"C:\Users\CETECOM\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" clean

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Limpieza exitosa
) else (
    echo.
    echo ✗ Error en la limpieza: %ERRORLEVEL%
)

echo.
pause
