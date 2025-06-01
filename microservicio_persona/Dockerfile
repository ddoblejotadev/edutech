# Dockerfile multi-etapa para compilar y ejecutar
FROM openjdk:17-jdk-slim-bookworm AS builder

WORKDIR /app

# Instalar Maven y limpiar caché para reducir vulnerabilidades
RUN apt-get update && apt-get install -y --no-install-recommends maven && \
	apt-get clean && rm -rf /var/lib/apt/lists/*

# Copiar POM y código fuente
COPY pom.xml .
COPY src src

# Compilar la aplicación
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim-bookworm

WORKDIR /app
WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
