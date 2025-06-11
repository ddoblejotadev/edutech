@echo off
echo ==============================================
echo DIAGNOSTICO DE VARIABLES DE ENTORNO
echo ==============================================
echo.

echo JAVA_TOOL_OPTIONS actual:
echo "%JAVA_TOOL_OPTIONS%"
echo.

echo MAVEN_OPTS actual:
echo "%MAVEN_OPTS%"
echo.

echo PATH de Java:
where java
echo.

echo PATH de Maven:
where mvn
echo.

pause
