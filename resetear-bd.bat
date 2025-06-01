@echo off
chcp 65001 > nul
title Resetear Base de Datos EduTech

echo =========================================
echo    RESETEAR BASE DE DATOS EDUTECH
echo =========================================
echo.

:: Verificar MySQL
echo Verificando MySQL...
netstat -an | findstr ":3306" > nul
if %errorlevel% neq 0 (
    echo ERROR: MySQL no está ejecutándose. Inicia XAMPP primero.
    pause
    exit /b 1
)
echo ✓ MySQL está funcionando
echo.

echo ⚠️  ATENCIÓN: Esto eliminará TODOS los datos de la base de datos edutech_db
echo.
set /p confirm="¿Estás seguro? (S/N): "
if /i not "%confirm%"=="S" (
    echo Operación cancelada.
    pause
    exit /b 0
)

echo.
echo Eliminando base de datos existente...
C:\xampp\mysql\bin\mysql.exe -u root -e "DROP DATABASE IF EXISTS edutech_db;"
echo ✓ Base de datos eliminada

echo.
echo Creando nueva base de datos con datos frescos...
cd database
call setup-mysql.bat
cd ..

echo.
echo ✅ Base de datos resetada exitosamente
echo.
echo La base de datos edutech_db ha sido recreada con:
echo   • Esquemas de todas las tablas
echo   • Datos de prueba realistas
echo   • 4 usuarios de ejemplo
echo   • 5 cursos con profesores
echo   • Evaluaciones y calificaciones
echo   • Horarios y comunicaciones
echo.
echo Puedes acceder a phpMyAdmin en: http://localhost/phpmyadmin
echo.
pause