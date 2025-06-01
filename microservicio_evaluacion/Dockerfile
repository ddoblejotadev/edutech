# Dockerfile multi-etapa para compilar y ejecutar
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Instalar Maven
RUN apk add --no-cache maven

# Copiar POM y código fuente
COPY pom.xml .
COPY src src

# Compilar la aplicación
RUN mvn package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
