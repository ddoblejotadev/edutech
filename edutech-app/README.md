# EduTech Mobile App

Aplicación móvil para la plataforma educativa EduTech. Esta aplicación permite a los estudiantes acceder a sus cursos, realizar evaluaciones y gestionar su perfil desde dispositivos móviles.

## Características

- **Autenticación**: Inicio de sesión y registro de usuarios
- **Exploración de cursos**: Visualización y búsqueda de cursos disponibles
- **Detalle de cursos**: Información detallada, contenido y material de cada curso
- **Evaluaciones**: Realización de pruebas y exámenes
- **Perfil de usuario**: Gestión de información personal y seguimiento del progreso

## Tecnologías utilizadas

- **React Native**: Framework para el desarrollo de aplicaciones móviles
- **Expo**: Plataforma para facilitar el desarrollo y pruebas
- **React Navigation**: Navegación entre pantallas
- **Async Storage**: Almacenamiento local seguro
- **API REST**: Comunicación con el backend de EduTech

## Estructura del proyecto

```
edutech-app/
├── assets/               # Recursos estáticos (imágenes, fuentes, etc.)
├── src/                  # Código fuente
│   ├── components/       # Componentes reutilizables
│   │   └── common/       # Componentes comunes (botones, tarjetas, etc.)
│   ├── config/           # Configuración (tema, constantes, etc.)
│   ├── context/          # Contextos de React (autenticación, etc.)
│   ├── navigation/       # Configuración de navegación
│   ├── screens/          # Pantallas de la aplicación
│   │   ├── auth/         # Pantallas de autenticación
│   │   ├── courses/      # Pantallas relacionadas con cursos
│   │   ├── evaluation/   # Pantallas de evaluación
│   │   ├── home/         # Pantalla principal
│   │   └── profile/      # Pantalla de perfil
│   └── services/         # Servicios (API, almacenamiento, etc.)
├── App.js                # Punto de entrada de la aplicación
└── package.json          # Dependencias y scripts
```

## Instalación

1. Asegúrate de tener instalado Node.js y npm
2. Clona este repositorio
3. Instala las dependencias con `npm install`
4. Inicia la aplicación con `npx expo start`

## Ejecución

Para facilitar la ejecución, puedes utilizar el script `iniciar-app.bat` que limpia la caché e inicia la aplicación.

## Conexión con el backend

La aplicación se conecta al backend de EduTech a través de una API REST. La configuración de la URL base se encuentra en `src/services/api.js`. Asegúrate de actualizar la dirección IP según tu configuración de red local.

## Desarrollo

Para añadir nuevas características o realizar modificaciones:

1. Crea los componentes necesarios en la carpeta `src/components`
2. Añade las pantallas en la carpeta `src/screens`
3. Configura la navegación en `src/navigation/AppNavigator.js`
4. Actualiza los servicios de API si es necesario en `src/services/api.js`
