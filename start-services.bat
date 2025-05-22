@echo off
echo Iniciando servicios de Edutech...

:: Limpiar y construir todos los proyectos
echo Construyendo todos los proyectos...
cd /d %~dp0
mvn clean install -DskipTests

:: Iniciar el registro de servicios (Eureka)
echo Iniciando servidor Eureka...
start "Eureka Server" cmd /c "cd service-registry && mvn spring-boot:run"
timeout /t 15 /nobreak

:: Iniciar el API Gateway
echo Iniciando API Gateway...
start "API Gateway" cmd /c "cd api_gateway && mvn spring-boot:run"
timeout /t 10 /nobreak

:: Iniciar los microservicios
echo Iniciando microservicio de Persona...
start "Microservicio Persona" cmd /c "cd microservicio_persona && mvn spring-boot:run"
timeout /t 5 /nobreak

echo Iniciando microservicio de Curso...
start "Microservicio Curso" cmd /c "cd microservicio_curso && mvn spring-boot:run"
timeout /t 5 /nobreak

echo Iniciando microservicio de Ejecución...
start "Microservicio Ejecución" cmd /c "cd microservicio_ejecucion && mvn spring-boot:run"
timeout /t 5 /nobreak

echo Iniciando microservicio de Comunicación...
start "Microservicio Comunicación" cmd /c "cd microservicio_comunicacion && mvn spring-boot:run"
timeout /t 5 /nobreak

echo Iniciando microservicio de Evaluación...
start "Microservicio Evaluación" cmd /c "cd microservicio_evaluacion && mvn spring-boot:run"
timeout /t 5 /nobreak

:: Iniciar los servicios de monitoreo
echo Iniciando Prometheus y Grafana...
docker-compose up -d prometheus grafana

echo Todos los servicios han sido iniciados.
echo Para detener los servicios, ejecute stop-services.bat
echo.
echo URLs de acceso:
echo - Eureka Server: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - Prometheus: http://localhost:9090
echo - Grafana: http://localhost:3000 (admin/admin)
