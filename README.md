# 🎓 EduTech App - Plataforma Educativa Móvil

Una aplicación móvil moderna desarrollada con React Native y Expo para la gestión académica y estudiantil.

## 📱 Características Principales

### 👤 Gestión de Usuario
- Sistema de autenticación seguro
- Perfil de estudiante personalizable
- Dashboard con información relevante

### 📚 Gestión Académica
- **Cursos**: Visualización y gestión de cursos inscritos
- **Calificaciones**: Seguimiento del rendimiento académico
- **Horarios**: Consulta de horarios de clases
- **Tareas**: Gestión de asignaciones y entregas
- **Evaluaciones**: Sistema de evaluaciones en línea

### 🏛️ Servicios Estudiantiles
- **Biblioteca**: Catálogo y préstamos de recursos
- **Servicios Académicos**: Solicitud de certificados y constancias
- **Estado Financiero**: Consulta de pagos y becas
- **Comunicaciones**: Anuncios y notificaciones institucionales

## 🚀 Tecnologías Utilizadas

- **Frontend**: React Native con Expo
- **Navegación**: React Navigation 7
- **UI Components**: React Native Paper
- **Iconos**: Expo Vector Icons
- **Storage**: Expo SecureStore
- **Backend**: Spring Boot (Java)
- **Base de Datos**: H2 Database (desarrollo)

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Node.js (versión 18 o superior)
- npm o yarn
- Expo CLI
- Android Studio o Xcode (para simuladores)

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/edutech.git
cd edutech
```

### 2. Configurar Frontend (React Native)
```bash
cd edutech-app
npm install
```

### 3. Configurar Backend (Spring Boot)
```bash
cd edutech-backend
./mvnw clean install
```

### 4. Iniciar la Aplicación

#### Frontend
```bash
cd edutech-app
npm start
# o
expo start
```

#### Backend
```bash
cd edutech-backend
./mvnw spring-boot:run
```

## 📱 Uso de la Aplicación

### Credenciales de Demo
- **Usuario**: `carlos.mendoza@duocuc.cl`
- **Contraseña**: `duoc2024`

### Funcionalidades Disponibles
1. **Login** - Acceso al sistema
2. **Dashboard** - Vista general del estudiante
3. **Cursos** - Exploración y detalles de cursos
4. **Calificaciones** - Consulta de notas por período
5. **Servicios** - Acceso a servicios estudiantiles
6. **Perfil** - Gestión de datos personales

## 🏗️ Arquitectura del Proyecto

```
edutech/
├── edutech-app/                 # Frontend React Native
│   ├── src/
│   │   ├── components/         # Componentes reutilizables
│   │   ├── screens/           # Pantallas de la aplicación
│   │   ├── navigation/        # Configuración de navegación
│   │   ├── services/          # Servicios API
│   │   ├── context/           # Contextos de React
│   │   ├── config/            # Configuraciones (tema, API)
│   │   └── data/              # Datos demo
│   ├── assets/                # Recursos (imágenes, iconos)
│   └── App.js                 # Componente principal
└── edutech-backend/            # Backend Spring Boot
    ├── src/main/java/com/edutech/
    │   ├── controller/        # Controladores REST
    │   ├── model/             # Entidades JPA
    │   ├── repository/        # Repositorios de datos
    │   ├── service/           # Lógica de negocio
    │   └── config/            # Configuraciones
    └── src/main/resources/    # Recursos y configuración
```

## 🎨 Sistema de Diseño

### Colores Principales
- **Primario**: `#1565C0` (Azul universitario)
- **Secundario**: `#42A5F5`
- **Éxito**: `#4CAF50`
- **Advertencia**: `#FF9800`
- **Error**: `#F44336`

### Tipografía
- **Títulos**: Roboto Bold
- **Subtítulos**: Roboto Medium
- **Cuerpo**: Roboto Regular

## 🧪 Testing

### Frontend
```bash
cd edutech-app
npm test
```

### Backend
```bash
cd edutech-backend
./mvnw test
```

## 📦 Build para Producción

### Frontend (Expo Build)
```bash
cd edutech-app
eas build --platform android
eas build --platform ios
```

### Backend (JAR)
```bash
cd edutech-backend
./mvnw clean package
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Equipo de Desarrollo

- **Frontend**: React Native / Expo
- **Backend**: Spring Boot / Java
- **Diseño UI/UX**: Inspirado en plataformas educativas modernas

## 📞 Soporte

Para soporte técnico o consultas:
- Email: soporte@edutech.cl
- Issues: [GitHub Issues](https://github.com/tu-usuario/edutech/issues)

## 🔄 Roadmap

- [ ] Implementación de notificaciones push
- [ ] Modo offline para contenido crítico
- [ ] Integración con sistemas LMS externos
- [ ] Aplicación web complementaria
- [ ] Módulo de videoconferencias

---

**Desarrollado con ❤️ para la educación digital**
