@echo off
chcp 65001 > nul
title Despliegue Rápido EduTech

echo =========================================
echo    DESPLIEGUE SIMPLIFICADO EDUTECH
echo =========================================
echo.

:: Verificar MySQL
echo [1/3] Verificando MySQL...
netstat -an | findstr ":3306" > nul
if %errorlevel% neq 0 (
    echo ERROR: MySQL no está ejecutándose. Inicia XAMPP primero.
    pause
    exit /b 1
)
echo ✓ MySQL OK

:: Limpiar puertos
echo [2/3] Liberando puertos...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul
echo ✓ Puertos liberados

:: Verificar/Crear base de datos
echo [3/3] Verificando base de datos...
C:\xampp\mysql\bin\mysql.exe -u root -e "USE edutech_db;" 2>nul
if %errorlevel% neq 0 (
    echo Base de datos no existe, creándola...
    cd database
    call setup-mysql.bat
    cd ..
    echo ✓ Base de datos creada
) else (
    echo ✓ Base de datos OK
)

echo.
echo =========================================
echo    INICIANDO MICROSERVICIOS
echo =========================================

:: Guardar directorio actual
set "ORIGINAL_DIR=%CD%"
echo Directorio base: %ORIGINAL_DIR%
echo.

:: Método más directo - cada comando en una línea separada
echo [1/5] Abriendo Microservicio Persona...
start "Persona-8081" cmd /k "cd /d "%ORIGINAL_DIR%\microservicio_persona" && mvn spring-boot:run"

echo [2/5] Abriendo Microservicio Curso...
start "Curso-8082" cmd /k "cd /d "%ORIGINAL_DIR%\microservicio_curso" && mvn spring-boot:run"

echo [3/5] Abriendo Microservicio Ejecucion...
start "Ejecucion-8083" cmd /k "cd /d "%ORIGINAL_DIR%\microservicio_ejecucion" && mvn spring-boot:run"

echo [4/5] Abriendo Microservicio Evaluacion...
start "Evaluacion-8084" cmd /k "cd /d "%ORIGINAL_DIR%\microservicio_evaluacion" && mvn spring-boot:run"

echo [5/5] Abriendo Microservicio Comunicacion...
start "Comunicacion-8085" cmd /k "cd /d "%ORIGINAL_DIR%\microservicio_comunicacion" && mvn spring-boot:run"

echo.
echo ✅ TERMINALES INICIADAS
echo.
echo Deberías ver 5 ventanas de terminal abriéndose ahora:
echo   → Persona-8081
echo   → Curso-8082  
echo   → Ejecucion-8083
echo   → Evaluacion-8084
echo   → Comunicacion-8085
echo.
echo 🌐 URLs una vez iniciados:
echo   • http://localhost:8081 - Persona
echo   • http://localhost:8082 - Curso  
echo   • http://localhost:8083 - Ejecución
echo   • http://localhost:8084 - Evaluación
echo   • http://localhost:8085 - Comunicación
echo.
echo ⏱️ Espera 30-45 segundos para que todos inicien completamente
echo.
echo Para detener: Cierra las ventanas o ejecuta detener-microservicios.bat
pause