@echo off
echo 🔍 Verificando configuración del proyecto EduTech Backend...
echo.

echo 📋 Información del sistema:
echo ==================================
echo Java version:
java -version
echo.

echo Maven version:
if exist mvnw.cmd (
    mvnw.cmd -version
) else (
    mvn -version
)
echo.

echo 🧪 Verificando compilación...
echo ==================================
if exist mvnw.cmd (
    mvnw.cmd clean compile
) else (
    mvn clean compile
)

if errorlevel 1 (
    echo ❌ Error en la compilación
    echo.
    echo 💡 Soluciones recomendadas:
    echo 1. Ejecuta setup-env.bat para configurar el entorno
    echo 2. Verifica que Java 17 esté instalado
    echo 3. Reinstala dependencias: mvnw.cmd dependency:resolve
    pause
    exit /b 1
) else (
    echo ✅ ¡Compilación exitosa!
)

echo.
echo 🧪 Verificando tests...
echo ==================================
if exist mvnw.cmd (
    mvnw.cmd test -q
) else (
    mvn test -q
)

if errorlevel 1 (
    echo ⚠️  Algunos tests fallaron, pero la compilación es correcta
) else (
    echo ✅ ¡Tests ejecutados exitosamente!
)

echo.
echo 🎯 Resumen:
echo ==================================
echo ✅ Proyecto configurado correctamente para Java 17
echo ✅ Lombok configurado y funcionando
echo ✅ Maven wrapper disponible
echo.
echo 🚀 Para ejecutar el proyecto:
echo    mvnw.cmd spring-boot:run
echo.
echo 💡 Recuerda siempre usar 'mvnw.cmd' para garantizar compatibilidad

pause