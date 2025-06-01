@echo off
echo =========================================
echo    EJECUTAR MICROSERVICIOS CON MYSQL
echo =========================================
echo.

echo Verificando que MySQL este ejecutandose...
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="1" (
    echo ERROR: MySQL no esta ejecutandose.
    echo Por favor, inicia MySQL desde XAMPP.
    pause
    exit /b 1
)

echo Verificando base de datos edutech_db...
"C:\xampp\mysql\bin\mysql.exe" -u root -e "USE edutech_db; SHOW TABLES;" >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Base de datos edutech_db no encontrada.
    echo Ejecuta primero: database\setup-mysql.bat
    pause
    exit /b 1
)

echo.
echo Compilando microservicios...
echo.

cd /d "%~dp0"

echo Compilando todos los microservicios...
mvn clean compile -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo la compilacion de microservicios.
    pause
    exit /b 1
)

echo.
echo Iniciando microservicios...
echo.

echo [1/5] Iniciando Microservicio Persona (Puerto 8081)...
start "Microservicio Persona" cmd /k "cd microservicio_persona && mvn spring-boot:run"

timeout /t 3 >nul

echo [2/5] Iniciando Microservicio Curso (Puerto 8082)...
start "Microservicio Curso" cmd /k "cd microservicio_curso && mvn spring-boot:run"

timeout /t 3 >nul

echo [3/5] Iniciando Microservicio Ejecucion (Puerto 8083)...
start "Microservicio Ejecucion" cmd /k "cd microservicio_ejecucion && mvn spring-boot:run"

timeout /t 3 >nul

echo [4/5] Iniciando Microservicio Evaluacion (Puerto 8084)...
start "Microservicio Evaluacion" cmd /k "cd microservicio_evaluacion && mvn spring-boot:run"

timeout /t 3 >nul

echo [5/5] Iniciando Microservicio Comunicacion (Puerto 8085)...
start "Microservicio Comunicacion" cmd /k "cd microservicio_comunicacion && mvn spring-boot:run"

echo.
echo =========================================
echo    MICROSERVICIOS INICIADOS
echo =========================================
echo.
echo Microservicios ejecutandose en:
echo - Persona:      http://localhost:8081
echo - Curso:        http://localhost:8082
echo - Ejecucion:    http://localhost:8083
echo - Evaluacion:   http://localhost:8084
echo - Comunicacion: http://localhost:8085
echo.
echo phpMyAdmin:     http://localhost/phpmyadmin
echo Base de datos:  edutech_db
echo.
echo Presiona cualquier tecla para salir...
pause >nul