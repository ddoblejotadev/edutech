# Dockerfile multi-etapa para compilar y ejecutar
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar POM y código fuente
COPY pom.xml .
COPY src src

# Compilar la aplicación
RUN mvn package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
