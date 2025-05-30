@echo off
echo ================================================
echo        EDUTECH - DETENER SERVICIOS
echo ================================================
echo.

echo Deteniendo todos los servicios Docker...
docker-compose down

echo.
echo Removiendo volúmenes (opcional - datos de BD):
docker-compose down -v

echo.
echo ================================================
echo         SERVICIOS DETENIDOS
echo ================================================
echo.
echo Todos los contenedores han sido detenidos.
echo Para iniciar nuevamente: docker-desplegar.bat
echo.
pause
