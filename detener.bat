@echo off
echo ================================================
echo           EDUTECH - DETENER SERVICIOS
echo ================================================
echo.

echo Deteniendo todos los servicios...
docker-compose down

echo.
echo Limpiando volumenes de base de datos...
docker-compose down -v

echo.
echo ================================================
echo            SERVICIOS DETENIDOS
echo ================================================
echo.
pause
