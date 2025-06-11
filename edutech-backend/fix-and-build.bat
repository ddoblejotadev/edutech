@echo off
echo Solucionando problema de JAVA_TOOL_OPTIONS con caracter # problematico

REM Crear un nuevo entorno limpio
setlocal

REM Establecer variables limpias explícitamente
set "JAVA_TOOL_OPTIONS=-Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8"
set "MAVEN_OPTS=-Xmx512m"

echo Variables configuradas:
echo JAVA_TOOL_OPTIONS=%JAVA_TOOL_OPTIONS%
echo MAVEN_OPTS=%MAVEN_OPTS%
echo.

REM Cambiar al directorio del proyecto
cd /d "c:\Users\CETECOM\Desktop\edutech\edutech-backend"

echo Ejecutando Maven clean...
call "C:\Users\CETECOM\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" clean

echo.
echo Ejecutando Maven compile...
call "C:\Users\CETECOM\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" compile

echo.
echo Proceso completado.
pause
