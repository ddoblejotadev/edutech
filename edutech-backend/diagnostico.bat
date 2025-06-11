@echo off
echo ==============================================
echo DIAGNOSTICO DEL PROYECTO EDUTECH-BACKEND
echo ==============================================
echo.

echo Verificando Java...
java -version

echo.
echo Verificando Maven...
mvn -version

echo.
echo Verificando directorio target...
if exist "target\classes\com\edutech\EdutechBackendApplication.class" (
    echo ✓ Aplicacion compilada correctamente
) else (
    echo ✗ Aplicacion NO esta compilada
)

echo.
echo Verificando archivos de configuracion...
if exist "src\main\resources\application-test.properties" (
    echo ✓ Archivo application-test.properties existe
) else (
    echo ✗ Archivo application-test.properties NO existe
)

echo.
echo Intentando ejecutar aplicacion...
set "JAVA_TOOL_OPTIONS="
set "MAVEN_OPTS="

java -Dspring.profiles.active=test -jar target\classes com.edutech.EdutechBackendApplication

pause
