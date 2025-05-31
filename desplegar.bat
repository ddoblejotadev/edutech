@echo off
setlocal

echo ================================================
echo           EDUTECH - DESPLEGAR DOCKER
echo ================================================
echo.

REM Configurar Java 17 para evitar problemas
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.15.6-hotspot"
set "PATH=C:\Program Files\Microsoft\jdk-17.0.15.6-hotspot\bin;%PATH%"

echo Verificando Docker...
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker no esta disponible
    echo Por favor inicie Docker Desktop o verifique la instalacion
    pause
    exit /b 1
)

echo Docker OK - Iniciando despliegue...
echo.

echo [1/2] Construyendo imagenes y desplegando servicios...
docker-compose up -d --build

if errorlevel 1 (
    echo.
    echo ERROR: Fallo en el despliegue
    echo Verificando logs...
    docker-compose logs
    pause
    exit /b 1
)

echo.
echo [2/2] Verificando estado de los servicios...
timeout /t 5 /nobreak >nul
docker-compose ps

echo.
echo ================================================
echo          DESPLIEGUE COMPLETADO
echo ================================================
echo.
echo Servicios disponibles:
echo - Microservicio Persona:     http://localhost:8081
echo - Microservicio Curso:       http://localhost:8082  
echo - Microservicio Ejecucion:   http://localhost:8083
echo - Microservicio Evaluacion:  http://localhost:8084
echo - Microservicio Comunicacion: http://localhost:8085
echo - phpMyAdmin:                http://localhost:8080
echo.
echo Para detener: detener.bat
echo Para ver logs: docker-compose logs [servicio]
echo.
pause
