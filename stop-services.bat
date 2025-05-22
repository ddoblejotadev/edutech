@echo off
echo Deteniendo servicios de Edutech...

:: Detener servicios de Docker
echo Deteniendo contenedores de Docker...
docker-compose down

:: Detener procesos de Java
echo Deteniendo procesos Java...
taskkill /F /FI "WINDOWTITLE eq Eureka Server*" 2>nul
taskkill /F /FI "WINDOWTITLE eq API Gateway*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Microservicio Persona*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Microservicio Curso*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Microservicio Ejecución*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Microservicio Comunicación*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Microservicio Evaluación*" 2>nul

echo Todos los servicios han sido detenidos.
