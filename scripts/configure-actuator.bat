@echo off
echo Configurando Actuator para todos los microservicios...

echo Configurando API Gateway...
echo # Configuración de Actuator > c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo management: >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo   endpoints: >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo     web: >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo       exposure: >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo         include: health,info,prometheus,metrics,gateway >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo   endpoint: >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo     health: >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo       show-details: always >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo     prometheus: >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml
echo       enabled: true >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application-actuator.yml

echo spring.profiles.include=actuator >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\api_gateway\src\main\resources\application.yml

echo Configurando microservicio de Personas...
echo # Configuración de Actuator para monitoreo > c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_persona\src\main\resources\application-actuator.properties
echo management.endpoints.web.exposure.include=health,info,prometheus,metrics >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_persona\src\main\resources\application-actuator.properties
echo management.endpoint.health.show-details=always >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_persona\src\main\resources\application-actuator.properties
echo management.prometheus.metrics.export.enabled=true >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_persona\src\main\resources\application-actuator.properties

echo spring.profiles.include=actuator >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_persona\src\main\resources\application.properties

echo Configurando microservicio de Cursos...
mkdir c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\ 2>nul
echo # Configuración de Actuator para monitoreo > c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\application-actuator.properties
echo management.endpoints.web.exposure.include=health,info,prometheus,metrics >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\application-actuator.properties
echo management.endpoint.health.show-details=always >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\application-actuator.properties
echo management.prometheus.metrics.export.enabled=true >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\application-actuator.properties

IF EXIST c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\application.properties (
  echo spring.profiles.include=actuator >> c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\application.properties
) ELSE (
  echo spring.profiles.include=actuator > c:\Users\ALUMNO\Documents\Edu-Tech\edutech\microservicio_curso\src\main\resources\application.properties
)

echo Configuración completa.
echo.
echo Nota: Este script ha configurado Actuator en los servicios principales.
echo Para aplicar los cambios, compile y reinicie los servicios.
