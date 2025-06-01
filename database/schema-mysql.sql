-- Script de creación de esquemas para EduTech
-- Base de datos: edutech_db

-- =====================================================
-- MICROSERVICIO PERSONA
-- =====================================================

-- Tabla de usuarios/estudiantes
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    direccion VARCHAR(255),
    region VARCHAR(50),
    sede VARCHAR(50),
    carrera VARCHAR(100),
    año TINYINT,
    semestre TINYINT,
    modalidad ENUM('Presencial', 'Online', 'Híbrida') DEFAULT 'Presencial',
    jornada ENUM('Diurna', 'Vespertina', 'Nocturna') DEFAULT 'Diurna',
    cohorte VARCHAR(10),
    estado_academico ENUM('Regular', 'Condicional', 'Suspendido', 'Egresado') DEFAULT 'Regular',
    promedio DECIMAL(3,1) DEFAULT 0.0,
    creditos_aprobados INT DEFAULT 0,
    creditos_totales INT DEFAULT 240,
    foto_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de beneficios estudiantiles
CREATE TABLE IF NOT EXISTS beneficios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado ENUM('ACTIVA', 'INACTIVA', 'SUSPENDIDA') DEFAULT 'ACTIVA',
    porcentaje DECIMAL(5,2) DEFAULT 0.00,
    fecha_inicio DATE,
    fecha_fin DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- =====================================================
-- MICROSERVICIO CURSO
-- =====================================================

-- Tabla de cursos
CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    creditos TINYINT NOT NULL DEFAULT 3,
    semestre VARCHAR(10),
    nivel ENUM('Básico', 'Intermedio', 'Avanzado') DEFAULT 'Básico',
    modalidad ENUM('Presencial', 'Online', 'Híbrida') DEFAULT 'Presencial',
    categoria VARCHAR(50),
    estado ENUM('Activo', 'Inactivo', 'Programado') DEFAULT 'Activo',
    cupos_totales INT DEFAULT 30,
    cupos_disponibles INT DEFAULT 30,
    horario VARCHAR(100),
    aula VARCHAR(50),
    edificio VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de profesores
CREATE TABLE IF NOT EXISTS profesores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    especialidad VARCHAR(100),
    titulo VARCHAR(150),
    grado_academico ENUM('Licenciado', 'Magíster', 'Doctor') DEFAULT 'Licenciado',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de asignación de profesores a cursos
CREATE TABLE IF NOT EXISTS curso_profesor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    profesor_id BIGINT NOT NULL,
    rol ENUM('Titular', 'Ayudante', 'Colaborador') DEFAULT 'Titular',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    FOREIGN KEY (profesor_id) REFERENCES profesores(id) ON DELETE CASCADE
);

-- Tabla de inscripciones
CREATE TABLE IF NOT EXISTS inscripciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('Inscrito', 'Aprobado', 'Reprobado', 'Retirado') DEFAULT 'Inscrito',
    nota_final DECIMAL(3,1) DEFAULT NULL,
    asistencia DECIMAL(5,2) DEFAULT 0.00,
    progreso DECIMAL(5,2) DEFAULT 0.00,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_course (usuario_id, curso_id)
);

-- =====================================================
-- MICROSERVICIO EVALUACION
-- =====================================================

-- Tabla de evaluaciones
CREATE TABLE IF NOT EXISTS evaluaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    tipo ENUM('Examen', 'Prueba', 'Tarea', 'Proyecto', 'Laboratorio') NOT NULL,
    fecha_programada DATETIME NOT NULL,
    duracion_minutos INT DEFAULT 120,
    ponderacion DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    nota_maxima DECIMAL(3,1) DEFAULT 7.0,
    nota_aprobacion DECIMAL(3,1) DEFAULT 4.0,
    aula VARCHAR(50),
    instrucciones TEXT,
    estado ENUM('Programada', 'En_Progreso', 'Finalizada', 'Cancelada') DEFAULT 'Programada',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE
);

-- Tabla de calificaciones
CREATE TABLE IF NOT EXISTS calificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluacion_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    nota DECIMAL(3,1) DEFAULT NULL,
    fecha_entrega TIMESTAMP NULL,
    retroalimentacion TEXT,
    estado ENUM('Pendiente', 'Entregada', 'Calificada', 'Tardía') DEFAULT 'Pendiente',
    intentos INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (evaluacion_id) REFERENCES evaluaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    UNIQUE KEY unique_evaluation_user (evaluacion_id, usuario_id)
);

-- =====================================================
-- MICROSERVICIO COMUNICACION
-- =====================================================

-- Tabla de anuncios/comunicaciones
CREATE TABLE IF NOT EXISTS comunicaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    contenido TEXT NOT NULL,
    tipo ENUM('Anuncio', 'Notificacion', 'Emergencia', 'Academico', 'Administrativo') DEFAULT 'Anuncio',
    prioridad ENUM('Baja', 'Media', 'Alta', 'Urgente') DEFAULT 'Media',
    dirigido_a ENUM('Todos', 'Estudiantes', 'Profesores', 'Personal') DEFAULT 'Estudiantes',
    departamento VARCHAR(100),
    autor VARCHAR(100),
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion TIMESTAMP NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de servicios estudiantiles
CREATE TABLE IF NOT EXISTS servicios_estudiantiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    categoria ENUM('CERTIFICADOS', 'CREDENCIALES', 'TRAMITES', 'OTROS') NOT NULL,
    precio DECIMAL(10,2) DEFAULT 0.00,
    tiempo_entrega VARCHAR(50),
    formato SET('DIGITAL', 'FISICO', 'PRESENCIAL') DEFAULT 'DIGITAL',
    requisitos JSON,
    estado ENUM('DISPONIBLE', 'NO_DISPONIBLE', 'MANTENIMIENTO') DEFAULT 'DISPONIBLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- MICROSERVICIO EJECUCION
-- =====================================================

-- Tabla de horarios
CREATE TABLE IF NOT EXISTS horarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    dia_semana ENUM('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    aula VARCHAR(50),
    edificio VARCHAR(50),
    tipo_clase ENUM('Teórica', 'Laboratorio', 'Práctica', 'Seminario', 'Taller') DEFAULT 'Teórica',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE
);

-- Tabla de asistencia
CREATE TABLE IF NOT EXISTS asistencia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    presente BOOLEAN DEFAULT FALSE,
    justificada BOOLEAN DEFAULT FALSE,
    observaciones TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (usuario_id, curso_id, fecha)
);