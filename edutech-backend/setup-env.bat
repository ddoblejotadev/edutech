@echo off
echo 🔧 Configurando entorno para EduTech Backend...

:: Buscar Java 17 en ubicaciones comunes de Windows
set JAVA17_HOME=
for /d %%i in ("C:\Program Files\Java\jdk-17*") do set JAVA17_HOME=%%i
if "%JAVA17_HOME%"=="" (
    for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set JAVA17_HOME=%%i
)
if "%JAVA17_HOME%"=="" (
    for /d %%i in ("C:\Program Files\OpenJDK\jdk-17*") do set JAVA17_HOME=%%i
)
if "%JAVA17_HOME%"=="" (
    for /d %%i in ("C:\Program Files\Amazon Corretto\jdk17*") do set JAVA17_HOME=%%i
)

if "%JAVA17_HOME%"=="" (
    echo ❌ Java 17 no encontrado en ubicaciones comunes.
    echo 📥 Por favor instala Java 17 desde:
    echo    - https://adoptium.net/temurin/releases/
    echo    - https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

:: Configurar variables de entorno para esta sesión
set JAVA_HOME=%JAVA17_HOME%
set PATH=%JAVA_HOME%\bin;%PATH%

echo ✅ JAVA_HOME configurado a: %JAVA_HOME%
echo 📋 Verificando versión de Java:
java -version

echo.
echo 🔍 Verificando Maven...
if exist mvnw.cmd (
    echo ✅ Maven wrapper encontrado
    echo 📋 Versión de Maven:
    mvnw.cmd -version
) else (
    echo ⚠️  Maven wrapper no encontrado, creando...
    mvn wrapper:wrapper -Dmaven=3.9.5
    if errorlevel 1 (
        echo ❌ Error creando Maven wrapper
        echo 🔧 Usando Maven del sistema...
    )
)

echo.
echo 🧪 Compilando proyecto con Java 17...
if exist mvnw.cmd (
    mvnw.cmd clean compile
) else (
    mvn clean compile
)

if errorlevel 1 (
    echo ❌ Error en la compilación
    echo 💡 Posibles soluciones:
    echo    1. Verificar que Java 17 esté instalado
    echo    2. Limpiar cache de Maven: mvnw.cmd clean
    echo    3. Reinstalar dependencias: mvnw.cmd dependency:purge-local-repository
    pause
    exit /b 1
) else (
    echo ✅ ¡Proyecto compilado exitosamente!
    echo.
    echo 🎯 Para ejecutar el proyecto usa:
    echo    mvnw.cmd spring-boot:run
    echo.
    echo 💡 Recuerda siempre usar 'mvnw.cmd' en lugar de 'mvn'
)

pause