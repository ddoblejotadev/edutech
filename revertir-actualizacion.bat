@echo off
:: Script para revertir cambios de Spring Boot en el proyecto Edu-Tech
echo ======================================================
echo = Revertir actualización de Spring Boot             =
echo ======================================================
echo.

:: Revertir versiones en POM padre
echo Paso 1: Revirtiendo a versiones anteriores en POM padre...
powershell -Command "(Get-Content -Path '%~dp0\pom.xml') -replace '<spring-boot.version>3.5.0</spring-boot.version>', '<spring-boot.version>3.4.6</spring-boot.version>' | Set-Content -Path '%~dp0\pom.xml'"
powershell -Command "(Get-Content -Path '%~dp0\pom.xml') -replace '<spring-cloud.version>2023.0.3</spring-cloud.version>', '<spring-cloud.version>2023.0.1</spring-cloud.version>' | Set-Content -Path '%~dp0\pom.xml'"

:: Informar al usuario
echo.
echo Cambios revertidos a la versión anterior de Spring Boot. Ahora debes ejecutar los siguientes comandos:
echo.
echo cd "%~dp0"
echo mvn clean
echo mvn compile -DskipTests
echo.

pause
