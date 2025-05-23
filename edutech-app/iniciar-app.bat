@echo off
echo Iniciando la aplicación EduTech...

:: Limpiar caché
echo Limpiando caché...
cd c:\Users\ALUMNO\Documents\Edu-Tech\edutech\edutech-app
npx expo start --clear

:: Verificar si ocurrió algún error
if %ERRORLEVEL% neq 0 (
    echo Error al iniciar la aplicación.
    echo Intentando reinstalar dependencias...
    
    npm install
    
    echo Intentando iniciar de nuevo...
    npx expo start --clear
)

pause
