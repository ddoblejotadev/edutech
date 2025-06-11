# Script PowerShell para limpiar y compilar el proyecto
$ErrorActionPreference = "Continue"

Write-Host "==============================================";
Write-Host "LIMPIEZA Y COMPILACION EDUTECH-BACKEND";
Write-Host "==============================================";
Write-Host "";

# Limpiar variables de entorno problemáticas
$env:JAVA_TOOL_OPTIONS = $null
$env:MAVEN_OPTS = $null
$env:_JAVA_OPTIONS = $null

# Cambiar al directorio del proyecto
Set-Location "c:\Users\CETECOM\Desktop\edutech\edutech-backend"

Write-Host "Directorio actual: $(Get-Location)";
Write-Host "";

# Ejecutar Maven clean
Write-Host "Ejecutando Maven clean...";
& "C:\Users\CETECOM\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" clean

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Limpieza exitosa" -ForegroundColor Green;
    
    # Si clean fue exitoso, compilar
    Write-Host "";
    Write-Host "Ejecutando Maven compile...";
    & "C:\Users\CETECOM\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd" compile
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Compilación exitosa" -ForegroundColor Green;
    } else {
        Write-Host "✗ Error en compilación" -ForegroundColor Red;
    }
} else {
    Write-Host "✗ Error en limpieza" -ForegroundColor Red;
}

Write-Host "";
Write-Host "Presiona cualquier tecla para continuar...";
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
