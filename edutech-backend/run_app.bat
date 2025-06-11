@echo off
set JAVA_TOOL_OPTIONS=
mvn spring-boot:run -Dspring-boot.run.profiles=dev
