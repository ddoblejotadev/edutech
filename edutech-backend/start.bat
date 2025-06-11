@echo off
setlocal
set "JAVA_TOOL_OPTIONS="
set "MAVEN_OPTS="

echo Ejecutando aplicacion Spring Boot con perfil test (H2)...
call mvn spring-boot:run -Dspring-boot.run.profiles=test -q
