@echo off
setlocal

echo ================================================
echo       EDUTECH - INICIANDO MICROSERVICIOS
echo ================================================
echo.

REM Configurar opciones JVM para evitar warnings
set "MAVEN_OPTS=--add-opens java.base/sun.misc=ALL-UNNAMED --enable-native-access=ALL-UNNAMED"

echo Iniciando microservicios en paralelo...
echo.

echo [1/5] Iniciando Microservicio Persona (Puerto 8081)...
start "Microservicio Persona" cmd /k "cd microservicio_persona && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [2/5] Iniciando Microservicio Curso (Puerto 8082)...
start "Microservicio Curso" cmd /k "cd microservicio_curso && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [3/5] Iniciando Microservicio Ejecucion (Puerto 8083)...
start "Microservicio Ejecucion" cmd /k "cd microservicio_ejecucion && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [4/5] Iniciando Microservicio Evaluacion (Puerto 8084)...
start "Microservicio Evaluacion" cmd /k "cd microservicio_evaluacion && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo [5/5] Iniciando Microservicio Comunicacion (Puerto 8085)...
start "Microservicio Comunicacion" cmd /k "cd microservicio_comunicacion && mvn spring-boot:run"

echo.
echo ================================================
echo         MICROSERVICIOS INICIADOS
echo ================================================
echo.
echo Los microservicios se estan iniciando en ventanas separadas.
echo.
echo URLs de acceso:
echo - Microservicio Persona:     http://localhost:8081
echo - Microservicio Curso:       http://localhost:8082
echo - Microservicio Ejecucion:   http://localhost:8083
echo - Microservicio Evaluacion:  http://localhost:8084
echo - Microservicio Comunicacion: http://localhost:8085
echo.
echo Para detener todos los servicios, cierre las ventanas abiertas.
echo.
pause
