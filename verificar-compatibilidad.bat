@echo off
echo ===== VERIFICACION DE COMPATIBILIDAD - MICROSERVICIOS EDUTECH =====
echo.

echo [1/5] Compilando Microservicio Persona...
cd microservicio_persona
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Persona - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Persona - ERROR EN COMPILACION
)
cd ..
echo.

echo [2/5] Compilando Microservicio Curso...
cd microservicio_curso
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Curso - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Curso - ERROR EN COMPILACION
)
cd ..
echo.

echo [3/5] Compilando Microservicio Evaluacion...
cd microservicio_evaluacion
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Evaluacion - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Evaluacion - ERROR EN COMPILACION
)
cd ..
echo.

echo [4/5] Compilando Microservicio Ejecucion...
cd microservicio_ejecucion
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Ejecucion - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Ejecucion - ERROR EN COMPILACION
)
cd ..
echo.

echo [5/5] Compilando Microservicio Comunicacion...
cd microservicio_comunicacion
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Comunicacion - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Comunicacion - ERROR EN COMPILACION
)
cd ..
echo.

echo ===== VERIFICACION COMPLETADA =====
echo Las correcciones de compatibilidad han sido aplicadas:
echo - ✅ Migración javax.persistence → jakarta.persistence
echo - ✅ Spring Boot unificado a versión 3.3.0
echo - ✅ SpringDoc OpenAPI agregado a todos los microservicios
echo - ✅ Configuraciones de documentación creadas
echo.
echo Documentación de APIs disponible en:
echo - Persona: http://localhost:8081/swagger-ui.html
echo - Curso: http://localhost:8082/swagger-ui.html
echo - Evaluación: http://localhost:8083/swagger-ui.html
echo - Ejecución: http://localhost:8084/swagger-ui.html
echo - Comunicación: http://localhost:8085/swagger-ui.html
echo.
pause