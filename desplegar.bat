@echo off
setlocal

echo ================================================
echo         EDUTECH - CONSTRUCCION Y DESPLIEGUE
echo ================================================
echo.

REM Configurar opciones JVM para evitar warnings
set "MAVEN_OPTS=--add-opens java.base/sun.misc=ALL-UNNAMED --enable-native-access=ALL-UNNAMED"

echo [1/4] Limpiando compilaciones anteriores...
mvn clean -q 2>nul
if errorlevel 1 (
    echo ERROR: Fallo en la limpieza
    pause
    exit /b 1
)

echo [2/4] Compilando todos los microservicios...
mvn package -DskipTests -q 2>nul
if errorlevel 1 (
    echo ERROR: Fallo en la compilacion
    pause
    exit /b 1
)

echo [3/4] Deteniendo contenedores existentes...
docker-compose down > nul 2>&1

echo [4/4] Iniciando servicios con Docker Compose...
docker-compose up -d
if errorlevel 1 (
    echo ERROR: Fallo al iniciar Docker Compose
    echo Nota: Asegurate de que Docker Desktop este iniciado
    pause
    exit /b 1
)

echo.
echo ================================================
echo           DESPLIEGUE COMPLETADO
echo ================================================
echo.
echo Servicios disponibles:
echo - Microservicio Persona:      http://localhost:8081
echo - Microservicio Curso:        http://localhost:8082
echo - Microservicio Ejecucion:    http://localhost:8083
echo - Microservicio Evaluacion:   http://localhost:8084
echo - Microservicio Comunicacion: http://localhost:8085
echo - phpMyAdmin:                 http://localhost:8080
echo.
echo Para ver los logs: docker-compose logs -f
echo Para detener:     docker-compose down
echo.
pause
