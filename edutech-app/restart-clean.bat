@echo off
REM Script para limpiar el caché y reiniciar la aplicación React Native
echo Limpiando cache de Metro y NPM...

REM Limpiar los directorios de caché
rd /s /q "%TEMP%\metro-cache"
rd /s /q "c:\Users\ALUMNO\Documents\Edu-Tech\edutech\edutech-app\node_modules\.cache"

REM Actualizar dependencias si es necesario
cd c:\Users\ALUMNO\Documents\Edu-Tech\edutech\edutech-app
call npm cache clean --force
call npm install

REM Reiniciar la aplicación con la caché limpia
call npx expo start -c

echo Aplicación reiniciada con éxito!
pause
