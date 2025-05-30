@echo off
echo ================================================
echo         EDUTECH - CONSTRUCCION Y DESPLIEGUE
echo ================================================
echo.

echo [1/4] Limpiando compilaciones anteriores...
mvn clean -q
if errorlevel 1 (
    echo ERROR: Fallo en la limpieza
    pause
    exit /b 1
)

echo [2/4] Compilando todos los microservicios...
mvn package -DskipTests -q
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
    pause
    exit /b 1
)

echo.
echo ================================================
echo                 DESPLIEGUE EXITOSO
echo ================================================
echo.
echo Servicios disponibles:
echo - phpMyAdmin:     http://localhost:8080
echo - API Gateway:    http://localhost:8762
echo - Microservicios: Puertos 8081-8085
echo.
echo Para ver los logs: docker-compose logs -f
echo Para detener:     docker-compose down
echo.
pause
