@echo off
chcp 65001 > nul
title Detener Microservicios EduTech

echo =========================================
echo    DETENIENDO MICROSERVICIOS
echo =========================================
echo.

echo Cerrando todas las ventanas de microservicios...
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /FI "WINDOWTITLE:Microservicio*" >nul 2>&1

echo.
echo Verificando que los puertos estén liberados...
timeout /t 3 /nobreak >nul

echo.
echo Estado final de los puertos:
netstat -an | findstr ":808[1-5] "

echo.
echo ✓ Todos los microservicios han sido detenidos
echo.
pause