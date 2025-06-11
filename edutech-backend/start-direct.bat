@echo off
setlocal
set "JAVA_TOOL_OPTIONS="
set "MAVEN_OPTS="

echo Ejecutando aplicacion directamente con Java...
cd /d "c:\Users\CETECOM\Desktop\edutech\edutech-backend"

java -Dspring.profiles.active=test -cp "target\classes;%USERPROFILE%\.m2\repository\org\springframework\boot\spring-boot-starter\3.4.0\spring-boot-starter-3.4.0.jar;%USERPROFILE%\.m2\repository\*\*\*.jar" com.edutech.EdutechBackendApplication
