@echo off
echo ================================================
echo        EDUTECH - DESPLEGAR CON DOCKER
echo ================================================
echo.

echo Verificando Docker...
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker no está disponible
    echo Asegúrese de que Docker Desktop esté iniciado
    pause
    exit /b 1
)

echo Docker OK - Iniciando despliegue...
echo.

echo Deteniendo servicios existentes...
docker-compose down

echo Iniciando servicios con Docker...
docker-compose up -d --build

if errorlevel 1 (
    echo.
    echo ERROR: Falló el despliegue
    echo.
    echo Mostrando logs de errores:
    docker-compose logs
    echo.
    pause
    exit /b 1
)

echo.
echo Esperando que los servicios inicien...
timeout /t 10 /nobreak >nul

echo Verificando estado de los contenedores:
docker-compose ps

echo.
echo ================================================
echo          DESPLIEGUE COMPLETADO
echo ================================================
echo.
echo URLs de los servicios:
echo - Microservicio Persona:     http://localhost:8081
echo - Microservicio Curso:       http://localhost:8082
echo - Microservicio Ejecucion:   http://localhost:8083
echo - Microservicio Evaluacion:  http://localhost:8084
echo - Microservicio Comunicacion: http://localhost:8085
echo - phpMyAdmin:                http://localhost:8080
echo.
echo Para detener los servicios: detener.bat
echo.
pause
