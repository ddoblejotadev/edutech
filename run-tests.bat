@echo off
echo Ejecutando pruebas de integración...

:: Asegurarse de que los servicios estén en ejecución
echo Verificando que los servicios estén en ejecución...
curl -s http://localhost:8761 > nul
if %ERRORLEVEL% neq 0 (
    echo ERROR: Eureka Server no está en ejecución.
    echo Por favor, ejecute start-services.bat primero.
    exit /b 1
)

:: Ejecutar pruebas de integración
echo Ejecutando pruebas de integración...
cd /d %~dp0\integration-tests
mvn failsafe:integration-test
if %ERRORLEVEL% neq 0 (
    echo.
    echo Hubo errores en las pruebas de integración.
    echo Revise los logs para más detalles.
) else (
    echo.
    echo Todas las pruebas de integración se ejecutaron correctamente.
)

echo.
echo Reporte de pruebas disponible en:
echo %~dp0\integration-tests\target\site\failsafe-report.html
