@echo off
:: Script para actualizar Spring Boot en el proyecto Edu-Tech
echo ======================================================
echo = Actualización de Spring Boot en Proyecto Edu-Tech =
echo ======================================================
echo.

:: Actualizar versiones en POM padre
echo Paso 1: Actualizando versiones en POM padre...
powershell -Command "(Get-Content -Path '%~dp0\pom.xml') -replace '<spring-boot.version>3.4.6</spring-boot.version>', '<spring-boot.version>3.5.0</spring-boot.version>' | Set-Content -Path '%~dp0\pom.xml'"
powershell -Command "(Get-Content -Path '%~dp0\pom.xml') -replace '<spring-cloud.version>2023.0.1</spring-cloud.version>', '<spring-cloud.version>2023.0.3</spring-cloud.version>' | Set-Content -Path '%~dp0\pom.xml'"

:: Actualizar configuración del compilador
echo Paso 2: Actualizando configuración del compilador Java...
set "OLD_COMPILER_CONFIG=                <plugin>\n                    <groupId>org.apache.maven.plugins</groupId>\n                    <artifactId>maven-compiler-plugin</artifactId>\n                    <version>3.13.0</version>\n                    <configuration>\n                        <fork>true</fork>\n                        <source>${java.version}</source>\n                        <target>${java.version}</target>\n                        <parameters>true</parameters>\n                        <compilerArgs>\n                            <arg>-Xlint:deprecation</arg>\n                            <arg>-proc:none</arg>\n                        </compilerArgs>\n                    </configuration>\n                </plugin>"

set "NEW_COMPILER_CONFIG=                <plugin>\n                    <groupId>org.apache.maven.plugins</groupId>\n                    <artifactId>maven-compiler-plugin</artifactId>\n                    <version>3.13.0</version>\n                    <configuration>\n                        <source>${java.version}</source>\n                        <target>${java.version}</target>\n                        <parameters>true</parameters>\n                        <fork>true</fork>\n                        <annotationProcessorPaths>\n                            <path>\n                                <groupId>org.projectlombok</groupId>\n                                <artifactId>lombok</artifactId>\n                                <version>${lombok.version}</version>\n                            </path>\n                            <path>\n                                <groupId>org.mapstruct</groupId>\n                                <artifactId>mapstruct-processor</artifactId>\n                                <version>${mapstruct.version}</version>\n                            </path>\n                        </annotationProcessorPaths>\n                    </configuration>\n                </plugin>"

powershell -Command "$content = Get-Content -Path '%~dp0\pom.xml' -Raw; $content = $content -replace [regex]::Escape('%OLD_COMPILER_CONFIG%'), '%NEW_COMPILER_CONFIG%'; Set-Content -Path '%~dp0\pom.xml' -Value $content -NoNewline"

:: Informar al usuario
echo.
echo Actualización completada. Ahora debes ejecutar los siguientes comandos manualmente:
echo.
echo cd "%~dp0"
echo mvn clean
echo mvn compile -DskipTests
echo.
echo Si hay errores específicos en un microservicio, ejecuta:
echo mvn compile -DskipTests -rf :nombre_del_microservicio
echo.
echo Para ver instrucciones más detalladas, consulta el archivo INSTRUCCIONES-ACTUALIZACION.md

pause
