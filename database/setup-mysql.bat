@echo off
echo =========================================
echo    CONFIGURACION MYSQL PARA EDUTECH
echo =========================================
echo.

echo [1/4] Verificando XAMPP...
if not exist "C:\xampp\mysql\bin\mysql.exe" (
    echo ERROR: XAMPP no encontrado en C:\xampp\
    echo Por favor, instala XAMPP primero.
    pause
    exit /b 1
)

echo [2/4] Verificando MySQL en XAMPP...
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="1" (
    echo ERROR: MySQL no esta ejecutandose en XAMPP.
    echo Por favor, inicia MySQL desde el panel de control de XAMPP.
    pause
    exit /b 1
)

echo [3/4] Creando base de datos edutech_db...
"C:\xampp\mysql\bin\mysql.exe" -u root -e "CREATE DATABASE IF NOT EXISTS edutech_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if %ERRORLEVEL% neq 0 (
    echo ERROR: No se pudo crear la base de datos.
    echo Verifica que MySQL este ejecutandose y que no tengas password en root.
    pause
    exit /b 1
)

echo [4/4] Ejecutando scripts SQL...
"C:\xampp\mysql\bin\mysql.exe" -u root edutech_db < "%~dp0schema-mysql.sql"
if %ERRORLEVEL% neq 0 (
    echo ERROR: No se pudo ejecutar el script de esquemas.
    pause
    exit /b 1
)

"C:\xampp\mysql\bin\mysql.exe" -u root edutech_db < "%~dp0data-mysql.sql"
if %ERRORLEVEL% neq 0 (
    echo ERROR: No se pudo ejecutar el script de datos.
    pause
    exit /b 1
)

echo.
echo =========================================
echo    CONFIGURACION COMPLETADA
echo =========================================
echo.
echo Base de datos 'edutech_db' creada exitosamente con:
echo - Esquemas de todas las tablas
echo - Datos de prueba realistas
echo - 4 usuarios de ejemplo
echo - 5 cursos con profesores
echo - Evaluaciones y calificaciones
echo - Horarios y comunicaciones
echo.
echo Puedes acceder a phpMyAdmin en: http://localhost/phpmyadmin
echo Usuario: root (sin password)
echo.
echo Para ejecutar los microservicios con MySQL:
echo java -jar microservicio-persona.jar --spring.profiles.active=mysql
echo.
pause