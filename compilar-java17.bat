@echo off
setlocal

echo ================================================
echo      EDUTECH - COMPILACION CON JAVA 17
echo ================================================
echo.

REM Configurar Java 17
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.15.6-hotspot"
set "PATH=C:\Program Files\Microsoft\jdk-17.0.15.6-hotspot\bin;%PATH%"

REM Limpiar variables de VS Code Java
set "JAVA_TOOL_OPTIONS="

echo Usando Java 17 para compilacion...
java -version
echo.

echo [1/6] Compilando Microservicio Persona...
cd microservicio_persona
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Fallo en microservicio_persona
    cd ..
    pause
    exit /b 1
)
cd ..

echo [2/6] Compilando Microservicio Curso...
cd microservicio_curso
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Fallo en microservicio_curso
    cd ..
    pause
    exit /b 1
)
cd ..

echo [3/6] Compilando Microservicio Ejecucion...
cd microservicio_ejecucion
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Fallo en microservicio_ejecucion
    cd ..
    pause
    exit /b 1
)
cd ..

echo [4/6] Compilando Microservicio Evaluacion...
cd microservicio_evaluacion
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Fallo en microservicio_evaluacion
    cd ..
    pause
    exit /b 1
)
cd ..

echo [5/6] Compilando Microservicio Comunicacion...
cd microservicio_comunicacion
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Fallo en microservicio_comunicacion
    cd ..
    pause
    exit /b 1
)
cd ..

echo [6/6] Compilacion completada exitosamente!
echo.
echo ================================================
echo         MICROSERVICIOS COMPILADOS
echo ================================================
echo.
echo Todos los archivos JAR han sido generados en:
echo - microservicio_persona/target/
echo - microservicio_curso/target/
echo - microservicio_ejecucion/target/
echo - microservicio_evaluacion/target/
echo - microservicio_comunicacion/target/
echo.
echo Ahora puede ejecutar: docker-compose up -d
echo.
pause
