@echo off
setlocal enabledelayedexpansion

echo ================================================
echo        EDUTECH - DESPLIEGUE DIRECTO
echo ================================================
echo.

REM Limpiar todas las variables Java de VS Code
set "JAVA_TOOL_OPTIONS="
set "MAVEN_OPTS="

REM Configurar Java 17 directamente
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.15.6-hotspot"
set "PATH=C:\Program Files\Microsoft\jdk-17.0.15.6-hotspot\bin;%PATH%"

echo Verificando Java...
java -version
echo.

echo Verificando Maven...
mvn -version
echo.

echo [1/2] Compilando microservicios usando mvnw (wrapper)...
echo.

echo Compilando microservicio_persona...
cd microservicio_persona
call mvnw.cmd clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR en microservicio_persona
    cd ..
    goto :error
)
cd ..

echo Compilando microservicio_curso...
cd microservicio_curso
call mvnw.cmd clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR en microservicio_curso
    cd ..
    goto :error
)
cd ..

echo Compilando microservicio_ejecucion...
cd microservicio_ejecucion
call mvnw.cmd clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR en microservicio_ejecucion
    cd ..
    goto :error
)
cd ..

echo Compilando microservicio_evaluacion...
cd microservicio_evaluacion
call mvnw.cmd clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR en microservicio_evaluacion
    cd ..
    goto :error
)
cd ..

echo Compilando microservicio_comunicacion...
cd microservicio_comunicacion
call mvnw.cmd clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR en microservicio_comunicacion
    cd ..
    goto :error
)
cd ..

echo.
echo [2/2] Iniciando despliegue con Docker...
echo.

docker-compose up -d

echo.
echo ================================================
echo           DESPLIEGUE COMPLETADO
echo ================================================
echo.
echo Servicios disponibles en:
echo - Microservicio Persona:     http://localhost:8081
echo - Microservicio Curso:       http://localhost:8082
echo - Microservicio Ejecucion:   http://localhost:8083
echo - Microservicio Evaluacion:  http://localhost:8084
echo - Microservicio Comunicacion: http://localhost:8085
echo - MySQL Database:            localhost:3306
echo - phpMyAdmin:                http://localhost:8080
echo.
echo Para ver logs: docker-compose logs -f
echo Para detener: docker-compose down
echo.
goto :end

:error
echo.
echo ================================================
echo                ERROR DE COMPILACION
echo ================================================
echo.
echo Revisar los mensajes de error anteriores.
echo.

:end
pause
