@echo off
echo ===== CONFIGURANDO JAVA 17 PARA EDUTECH =====
echo.

REM Buscar Java 17 en ubicaciones comunes
set JAVA17_PATH=
if exist "C:\Program Files\Eclipse Adoptium\jdk-17*" (
    for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set JAVA17_PATH=%%i
)
if exist "C:\Program Files\Java\jdk-17*" (
    for /d %%i in ("C:\Program Files\Java\jdk-17*") do set JAVA17_PATH=%%i
)
if exist "C:\Program Files\OpenJDK\jdk-17*" (
    for /d %%i in ("C:\Program Files\OpenJDK\jdk-17*") do set JAVA17_PATH=%%i
)

if "%JAVA17_PATH%"=="" (
    echo ❌ Java 17 no encontrado. Por favor:
    echo 1. Descarga Java 17 desde: https://adoptium.net/temurin/releases/?version=17
    echo 2. Instala Java 17
    echo 3. Ejecuta este script nuevamente
    echo.
    pause
    exit /b 1
)

echo ✅ Java 17 encontrado en: %JAVA17_PATH%
echo.
echo Configurando variables de entorno...

REM Configurar JAVA_HOME para esta sesión
set JAVA_HOME=%JAVA17_PATH%
set PATH=%JAVA_HOME%\bin;%PATH%

echo ✅ JAVA_HOME configurado: %JAVA_HOME%
echo.

REM Verificar la versión
echo Verificando versión de Java:
java -version
echo.

echo ===== COMPILANDO MICROSERVICIOS CON JAVA 17 =====
echo.

echo [1/5] Compilando Microservicio Persona...
cd microservicio_persona
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Persona - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Persona - ERROR EN COMPILACION
)
cd ..
echo.

echo [2/5] Compilando Microservicio Curso...
cd microservicio_curso
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Curso - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Curso - ERROR EN COMPILACION
)
cd ..
echo.

echo [3/5] Compilando Microservicio Evaluacion...
cd microservicio_evaluacion
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Evaluacion - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Evaluacion - ERROR EN COMPILACION
)
cd ..
echo.

echo [4/5] Compilando Microservicio Ejecucion...
cd microservicio_ejecucion
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Ejecucion - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Ejecucion - ERROR EN COMPILACION
)
cd ..
echo.

echo [5/5] Compilando Microservicio Comunicacion...
cd microservicio_comunicacion
call mvn clean compile -q
if %ERRORLEVEL% equ 0 (
    echo ✅ Microservicio Comunicacion - COMPILACION EXITOSA
) else (
    echo ❌ Microservicio Comunicacion - ERROR EN COMPILACION
)
cd ..
echo.

echo ===== COMPILACION COMPLETADA =====
echo Para futuras compilaciones, siempre ejecuta este script
echo para usar Java 17 en lugar de Java 24.
echo.
pause