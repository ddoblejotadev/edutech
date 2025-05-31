@echo off
echo.
echo =====================================================
echo   VERIFICACION FINAL - EDUTECH APP
echo =====================================================
echo.

cd /d "c:\Users\Administrator\Documents\edutech\edutech-app"

echo [1/5] Verificando archivos criticos...
if exist "App.js" (echo ✓ App.js) else (echo ✗ App.js)
if exist "src\context\AuthContext.js" (echo ✓ AuthContext.js) else (echo ✗ AuthContext.js)
if exist "src\services\studentApiService.js" (echo ✓ studentApiService.js) else (echo ✗ studentApiService.js)
if exist "src\config\api.js" (echo ✓ api.js) else (echo ✗ api.js)
if exist "src\navigation\AppNavigator.js" (echo ✓ AppNavigator.js) else (echo ✗ AppNavigator.js)

echo.
echo [2/5] Verificando e instalando dependencias...
echo Instalando dependencias faltantes...
call npm install

echo.
echo [3/5] Verificando configuracion de Expo...
if exist "app.config.js" (echo ✓ app.config.js presente) else (echo ✗ app.config.js faltante)
if not exist "app.json" (echo ✓ app.json eliminado correctamente) else (echo ⚠ app.json aun presente)

echo.
echo [4/5] Verificando compatibilidad con Expo...
echo Usando nuevo Expo CLI local...
call npx expo install --check

echo.
echo [5/5] Iniciando aplicacion con nuevo Expo CLI...
echo Para continuar, escanea el codigo QR con Expo Go
echo O presiona 'a' para Android, 'i' para iOS, 'w' para Web
echo.
call npx expo start

pause
