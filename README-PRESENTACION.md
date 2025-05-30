# EduTech - Sistema de Gestión Educativa

Plataforma completa de gestión educativa con arquitectura de microservicios, desarrollada con Spring Boot y React Native.

## 🏗️ Arquitectura

### Microservicios
- **API Gateway** (Puerto 8762) - Punto de entrada único
- **Microservicio Persona** (Puerto 8081) - Gestión de usuarios
- **Microservicio Curso** (Puerto 8082) - Gestión de cursos
- **Microservicio Ejecución** (Puerto 8083) - Ejecuciones e inscripciones
- **Microservicio Evaluación** (Puerto 8084) - Evaluaciones y calificaciones
- **Microservicio Comunicación** (Puerto 8085) - Mensajes y notificaciones

### Aplicación Móvil
- **EduTech App** - Aplicación React Native para estudiantes y profesores

### Base de Datos
- **MySQL 8.0** - Base de datos unificada
- **phpMyAdmin** (Puerto 8080) - Administración web de BD

## 🚀 Despliegue con Docker

### Prerrequisitos
- Docker y Docker Compose instalados
- Maven 3.6+
- Java 17+

### Pasos para el despliegue

1. **Compilar los microservicios:**
```bash
mvn clean package -DskipTests
```

2. **Iniciar los servicios con Docker:**
```bash
docker-compose up -d
```

3. **Verificar el estado:**
```bash
docker-compose ps
```

### Acceso a los servicios

- **phpMyAdmin:** http://localhost:8080 (Usuario: root, Contraseña: rootpassword)
- **API Gateway:** http://localhost:8762
- **Microservicios:** Puertos 8081-8085

## 📱 Aplicación Móvil

La aplicación React Native se encuentra en la carpeta `edutech-app/`.

### Iniciar la aplicación:
```bash
cd edutech-app
npm install
npm start
```

## 🛠️ Configuración de Base de Datos

La base de datos se crea automáticamente al iniciar Docker con el nombre `edutech_db`.

**Credenciales:**
- Host: localhost:3306
- Base de datos: edutech_db
- Usuario: edutech_user
- Contraseña: edutech_pass

## 📊 Monitoreo

Los logs se pueden ver con:
```bash
docker-compose logs -f [nombre-servicio]
```

## 🔧 Comandos útiles

### Reiniciar servicios:
```bash
docker-compose restart
```

### Detener servicios:
```bash
docker-compose down
```

### Limpiar volúmenes:
```bash
docker-compose down -v
```

## 📝 Funcionalidades

### Gestión Académica
- ✅ Registro y gestión de personas (estudiantes, profesores, administrativos)
- ✅ Creación y administración de cursos
- ✅ Programación de ejecuciones de cursos
- ✅ Sistema de inscripciones
- ✅ Evaluaciones y calificaciones
- ✅ Comunicación interna (mensajes y notificaciones)

### Aplicación Móvil
- ✅ Autenticación de usuarios
- ✅ Visualización de cursos disponibles
- ✅ Inscripción a cursos
- ✅ Seguimiento de calificaciones
- ✅ Sistema de mensajería
- ✅ Calendario académico

## 🎯 Preparado para Presentación

Este proyecto ha sido optimizado para demostraciones:
- ❌ Configuraciones complejas eliminadas
- ✅ Docker Compose simplificado
- ✅ Base de datos unificada
- ✅ phpMyAdmin incluido
- ✅ Logs habilitados para debugging
- ✅ Solo componentes esenciales

## 👥 Equipo de Desarrollo

Proyecto desarrollado para CETECOM - Sistemas de Información Educativa.
