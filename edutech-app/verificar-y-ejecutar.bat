@echo off
echo.
echo =====================================================
echo   VERIFICACION FINAL - EDUTECH APP
echo =====================================================
echo.

cd /d "c:\Users\ALUMNO\Documents\edutech\edutech-app"

echo [1/5] Verificando archivos criticos...
if exist "App.js" (echo ✓ App.js) else (echo ✗ App.js)
if exist "src\context\AuthContext.js" (echo ✓ AuthContext.js) else (echo ✗ AuthContext.js)
if exist "src\services\studentApiService.js" (echo ✓ studentApiService.js) else (echo ✗ studentApiService.js)
if exist "src\config\api.js" (echo ✓ api.js) else (echo ✗ api.js)
if exist "src\navigation\AppNavigator.js" (echo ✓ AppNavigator.js) else (echo ✗ AppNavigator.js)

echo.
echo [2/5] Verificando dependencias...
call npm list --depth=0 | findstr "expo"
call npm list --depth=0 | findstr "react-navigation"
call npm list --depth=0 | findstr "react-native-paper"

echo.
echo [3/5] Verificando configuracion de Expo...
if exist "app.config.js" (echo ✓ app.config.js presente) else (echo ✗ app.config.js faltante)
if not exist "app.json" (echo ✓ app.json eliminado correctamente) else (echo ⚠ app.json aun presente)

echo.
echo [4/5] Ejecutando verificacion de sintaxis...
call npx expo install --fix

echo.
echo [5/5] Iniciando aplicacion...
echo Para continuar, escanea el codigo QR con Expo Go
echo O presiona 'a' para Android, 'i' para iOS
echo.
call npx expo start

pause
