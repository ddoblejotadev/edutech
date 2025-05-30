@echo off
setlocal

echo ================================================
echo         EDUTECH - CONSTRUCCION LOCAL
echo ================================================
echo.

REM Configurar opciones JVM para evitar warnings
set "MAVEN_OPTS=--add-opens java.base/sun.misc=ALL-UNNAMED --enable-native-access=ALL-UNNAMED"

echo [1/3] Limpiando compilaciones anteriores...
mvn clean -q 2>nul
if errorlevel 1 (
    echo ERROR: Fallo en la limpieza
    pause
    exit /b 1
)

echo [2/3] Compilando todos los microservicios...
mvn package -DskipTests -q 2>nul
if errorlevel 1 (
    echo ERROR: Fallo en la compilacion
    pause
    exit /b 1
)

echo [3/3] Compilacion completada exitosamente!
echo.
echo ================================================
echo               MICROSERVICIOS DISPONIBLES
echo ================================================
echo.
echo Para ejecutar los microservicios individualmente:
echo.
echo - Microservicio Persona (Puerto 8081):
echo   cd microservicio_persona ^&^& mvn spring-boot:run
echo.
echo - Microservicio Curso (Puerto 8082):
echo   cd microservicio_curso ^&^& mvn spring-boot:run
echo.
echo - Microservicio Ejecucion (Puerto 8083):
echo   cd microservicio_ejecucion ^&^& mvn spring-boot:run
echo.
echo - Microservicio Evaluacion (Puerto 8084):
echo   cd microservicio_evaluacion ^&^& mvn spring-boot:run
echo.
echo - Microservicio Comunicacion (Puerto 8085):
echo   cd microservicio_comunicacion ^&^& mvn spring-boot:run
echo.
echo ================================================
echo.
pause
