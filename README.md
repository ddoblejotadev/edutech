# EduTech - Plataforma de educación en línea

## Descripción
EduTech es una plataforma de educación en línea desarrollada con una arquitectura de microservicios para gestionar cursos, estudiantes, evaluaciones y comunicaciones.

## Estructura del proyecto

El proyecto se compone de:

- **API Gateway**: Punto de entrada para todas las peticiones
- **Microservicios Backend**:
  - **microservicio_persona**: Gestión de usuarios (estudiantes, profesores, administradores)
  - **microservicio_curso**: Gestión de cursos y sus contenidos
  - **microservicio_ejecucion**: Gestión de las instancias de cursos y sus inscripciones
  - **microservicio_comunicacion**: Gestión de notificaciones y mensajes
  - **microservicio_evaluacion**: Gestión de evaluaciones, preguntas y calificaciones
- **Frontend Móvil**: Aplicación React Native/Expo para acceso desde dispositivos móviles

## Requisitos

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 18+
- npm 8+
- Expo CLI

## Configuración del entorno de desarrollo

### Backend

1. Clonar el repositorio:
```
git clone https://github.com/tu-usuario/edutech.git
cd edutech
```

2. Configurar la base de datos MySQL:
- Asegúrate de tener MySQL 8.0+ instalado y ejecutándose
- El sistema creará automáticamente las bases de datos

3. Construir los microservicios:
```
cd api_gateway
mvn clean install

cd ../microservicio_persona
mvn clean install

cd ../microservicio_curso
mvn clean install

cd ../microservicio_ejecucion
mvn clean install

cd ../microservicio_comunicacion
mvn clean install

cd ../microservicio_evaluacion
mvn clean install
```

4. Ejecutar los microservicios (cada uno en una terminal separada):
```
cd api_gateway
mvn spring-boot:run

cd microservicio_persona
mvn spring-boot:run

cd microservicio_curso
mvn spring-boot:run

cd microservicio_ejecucion
mvn spring-boot:run

cd microservicio_comunicacion
mvn spring-boot:run

cd microservicio_evaluacion
mvn spring-boot:run
```

### Frontend

1. Instalar dependencias:
```
cd edutech-mobile
npm install
```

2. Configurar la URL de la API:
```
npm run show-ip
```
Este comando mostrará tu IP local y actualizará automáticamente el archivo apiClient.ts.

3. Iniciar el servidor de desarrollo:
```
npm start
```

4. Escanea el código QR con la app Expo Go en tu dispositivo o ejecuta en un emulador:
```
npm run android
```
o
```
npm run ios
```

## Ejecutar con Docker Compose

También puedes ejecutar todo el ecosistema usando Docker Compose:

```
docker-compose up
```

## Acceso al sistema

- **API Gateway**: http://localhost:8080
- **Microservicio Persona**: http://localhost:8081
- **Microservicio Curso**: http://localhost:8082
- **Microservicio Ejecución**: http://localhost:8084
- **Microservicio Comunicación**: http://localhost:8085
- **Microservicio Evaluación**: http://localhost:8086

## Documentación de API

La documentación de la API está disponible en:
- http://localhost:8080/swagger-ui.html

## Autores

- [Tu Nombre]

## Licencia

Este proyecto está licenciado bajo [Tu Licencia] - ver el archivo LICENSE.md para más detalles.
