# 🗄️ Configuración MySQL para EduTech

Esta guía te ayudará a configurar MySQL de XAMPP como base de datos para todos los microservicios de EduTech.

## 📋 Requisitos Previos

1. **XAMPP instalado** con MySQL
2. **Java 17 o superior**
3. **Maven 3.6+**

## 🚀 Configuración Automática (Recomendada)

### Paso 1: Iniciar XAMPP

1. Abre el Panel de Control de XAMPP
2. Inicia **Apache** y **MySQL**
3. Verifica que MySQL esté ejecutándose (luz verde)

### Paso 2: Configurar Base de Datos

Ejecuta el script automático:

```bash
cd database
setup-mysql.bat
```

Este script:

- ✅ Verifica que XAMPP esté ejecutándose
- ✅ Crea la base de datos `edutech_db`
- ✅ Ejecuta los scripts de esquemas y datos
- ✅ Configura datos de prueba realistas

### Paso 3: Ejecutar Microservicios

```bash
ejecutar-microservicios-mysql.bat
```

## 🛠️ Configuración Manual

### 1. Crear Base de Datos

```sql
CREATE DATABASE edutech_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Ejecutar Scripts

```bash
# Desde la carpeta database/
mysql -u root edutech_db < schema-mysql.sql
mysql -u root edutech_db < data-mysql.sql
```

### 3. Ejecutar Microservicios Individualmente

```bash
# Microservicio Persona
cd microservicio_persona
mvn spring-boot:run -Dspring-boot.run.profiles=mysql

# Microservicio Curso
cd microservicio_curso
mvn spring-boot:run -Dspring-boot.run.profiles=mysql

# Microservicio Ejecución
cd microservicio_ejecucion
mvn spring-boot:run -Dspring-boot.run.profiles=mysql

# Microservicio Evaluación
cd microservicio_evaluacion
mvn spring-boot:run -Dspring-boot.run.profiles=mysql

# Microservicio Comunicación
cd microservicio_comunicacion
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## 📊 Estructura de la Base de Datos

### Microservicio Persona

- `usuarios` - Información de estudiantes
- `beneficios` - Becas y beneficios estudiantiles

### Microservicio Curso

- `cursos` - Catálogo de asignaturas
- `profesores` - Información docente
- `curso_profesor` - Asignación de profesores
- `inscripciones` - Matrículas de estudiantes

### Microservicio Evaluación

- `evaluaciones` - Pruebas y exámenes
- `calificaciones` - Notas y retroalimentación

### Microservicio Comunicación

- `comunicaciones` - Anuncios y notificaciones
- `servicios_estudiantiles` - Catálogo de servicios

### Microservicio Ejecución

- `horarios` - Programación de clases
- `asistencia` - Control de asistencia

## 👥 Datos de Prueba Incluidos

### Usuarios de Ejemplo

| RUT             | Nombre                          | Email                     | Carrera                          |
|-----------------|---------------------------------|---------------------------|----------------------------------|
| 19.234.567-8    | Carlos Andrés Mendoza Vargas    | carlos.mendoza@duocuc.cl | Ingeniería en Informática        |
| 18.123.456-7    | María José González Silva      | maria.gonzalez@duocuc.cl | Ingeniería en Informática        |
| 20.345.678-9    | Juan Pablo Rodríguez López      | juan.rodriguez@duocuc.cl | Administración de Empresas       |
| 17.987.654-3    | Ana Carolina Torres Muñoz      | <ana.torres@duocuc.cl>  | Diseño Gráfico                   |

### Cursos Disponibles

- **INFO1166** - Programación Orientada a Objetos
- **INFO1277** - Base de Datos II
- **INGL2201** - Inglés Técnico II
- **INFO1288** - Arquitectura de Software
- **INFO1304** - Redes de Computadoras

## 🔗 URLs de Acceso

- **phpMyAdmin**: <http://localhost/phpmyadmin>
- **Microservicio Persona**: <http://localhost:8081>
- **Microservicio Curso**: <http://localhost:8082>
- **Microservicio Ejecución**: <http://localhost:8083>
- **Microservicio Evaluación**: <http://localhost:8084>
- **Microservicio Comunicación**: <http://localhost:8085>

## 🔧 Configuración de Conexión

### Parámetros MySQL

- **Host**: localhost
- **Puerto**: 3306
- **Base de datos**: edutech_db
- **Usuario**: root
- **Contraseña**: (vacía)

### Configuración Spring Boot

Cada microservicio incluye `application-mysql.properties` con:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/edutech_db?useSSL=false&serverTimezone=America/Santiago
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

## 🐛 Solución de Problemas

### Error: MySQL no está ejecutándose

- Verifica que XAMPP esté iniciado
- Inicia el servicio MySQL desde el panel de XAMPP

### Error: Base de datos no encontrada

- Ejecuta el script `setup-mysql.bat`
- O crea manualmente la base de datos `edutech_db`

### Error: Puerto ya en uso

- Verifica que no hay otros servicios ejecutándose en los puertos 8081-8085
- Usa `netstat -an | findstr 808` para verificar puertos ocupados

### Error: Dependencias Maven

```bash
mvn clean install
```

## 📈 Próximos Pasos

1. **Verificar conexiones**: Accede a los endpoints de salud
   - [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
   - [http://localhost:8082/actuator/health](http://localhost:8082/actuator/health)
   - etc.

2. **Explorar datos**: Usa phpMyAdmin para verificar las tablas y datos

3. **Probar APIs**: Usa Postman o curl para probar los endpoints

4. **Integrar con la app móvil**: Configura los URLs de los microservicios en tu aplicación React Native

---

💡 **Tip**: Guarda este archivo README.md para futuras referencias y compártelo con tu equipo de desarrollo.

---

## Microservicio Persona - Configuración MySQL

Este microservicio utiliza MySQL como base de datos. Sigue estos pasos para configurarlo correctamente.

## Prerrequisitos

- MySQL 8.0 o superior
- Java 17 o superior
- Maven 3.6 o superior

## Configuración de la Base de Datos

### Crear la base de datos

```sql
CREATE DATABASE edutech_persona;
USE edutech_persona;
```

### Variables de entorno requeridas

- `DB_HOST`: Host de la base de datos (por defecto: localhost)
- `DB_PORT`: Puerto de la base de datos (por defecto: 3306)
- `DB_NAME`: Nombre de la base de datos (edutech_persona)
- `DB_USER`: Usuario de la base de datos
- `DB_PASSWORD`: Contraseña de la base de datos

## Configuración del archivo application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:edutech_persona}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```

## Inicialización de datos

```sql
INSERT INTO tipo_persona (nombre, descripcion) VALUES 
('Estudiante', 'Persona que está estudiando'),
('Profesor', 'Persona que enseña');
```

## Ejecutar el microservicio

```bash
mvn spring-boot:run
```

## Endpoints disponibles

### Personas

- GET `/api/personas` - Listar todas las personas
- GET `/api/personas/{id}` - Obtener persona por ID
- POST `/api/personas` - Crear nueva persona
- PUT `/api/personas/{id}` - Actualizar persona
- DELETE `/api/personas/{id}` - Eliminar persona

### Tipos de Persona

- GET `/api/tipos-persona` - Listar todos los tipos
- GET `/api/tipos-persona/{id}` - Obtener tipo por ID
- POST `/api/tipos-persona` - Crear nuevo tipo
- PUT `/api/tipos-persona/{id}` - Actualizar tipo
- DELETE `/api/tipos-persona/{id}` - Eliminar tipo

## Formato JSON para Persona

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@example.com",
  "tipoPersona": {
    "id": 1
  }
}
```

## URLs de prueba

| Endpoint | URL |
|----------|-----|
| Personas | <http://localhost:8080/api/personas> |
| Tipos | <http://localhost:8080/api/tipos-persona> |
| Health | <http://localhost:8080/actuator/health> |
| Swagger | <http://localhost:8080/swagger-ui.html> |

## Comandos útiles

- Verificar conexión a la base de datos
- Reiniciar el servicio
- Ver logs en tiempo real
- Hacer backup de la base de datos

Ejemplos de uso:

- <http://localhost:8080/api/personas>
- <http://localhost:8080/api/tipos-persona>
- <http://localhost:8080/api/personas/1>
- <http://localhost:8080/actuator/health>
- <http://localhost:8080/actuator/info>
- <http://localhost:8080/swagger-ui.html>

## Troubleshooting

### Problemas comunes

- Error de conexión a la base de datos
- Puerto ya en uso

### Logs y monitoreo

- Revisar logs de aplicación
- Monitorear métricas

### Performance

- Optimización de consultas
- Configuración de pool de conexiones

## Configuración con Docker

```bash
docker run -d --name mysql-edutech \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=edutech_persona \
  -p 3306:3306 \
  mysql:8.0
```

## Comandos de desarrollo

### Testing

- Ejecutar tests unitarios
- Ejecutar tests de integración

### Build

- Compilar proyecto
- Generar JAR

## 📚 Referencias adicionales

- [Documentación Spring Boot](<https://spring.io/projects/spring-boot>)
- [Documentación MySQL](<https://dev.mysql.com/doc/>)
- [Docker Compose reference](<https://docs.docker.com/compose/>)

Con esta configuración tendrás un entorno robusto y escalable para el desarrollo de tu plataforma educativa EduTech.