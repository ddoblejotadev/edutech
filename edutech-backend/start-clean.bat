@echo off
setlocal EnableDelayedExpansion

REM Limpiar variables problemáticas
set "JAVA_TOOL_OPTIONS="
set "MAVEN_OPTS="

REM Ir al directorio del proyecto
cd /d "C:\Users\CETECOM\Desktop\edutech\edutech-backend"

REM Ejecutar con Maven
echo Iniciando aplicacion Spring Boot...
"C:\Users\CETECOM\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" spring-boot:run -Dspring-boot.run.profiles=test

pause
