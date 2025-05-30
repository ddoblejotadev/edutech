@echo off
setlocal

echo ================================================
echo         EDUTECH - CONFIGURAR DOCKER PATH
echo ================================================
echo.

echo Configurando PATH para Docker...
set "PATH=%PATH%;C:\Program Files\Docker\Docker\resources\bin"

echo Verificando Docker...
docker --version
if errorlevel 1 (
    echo ERROR: Docker no esta funcionando correctamente
    pause
    exit /b 1
)

echo Docker configurado correctamente!
echo.

REM Exportar la variable PATH para otros scripts
setx PATH "%PATH%" > nul 2>&1

echo PATH actualizado permanentemente.
echo.
pause
