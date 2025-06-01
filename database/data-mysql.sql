-- Script de poblado de datos para EduTech
-- Base de datos: edutech_db

-- =====================================================
-- DATOS DE USUARIOS/ESTUDIANTES
-- =====================================================

INSERT INTO usuarios (rut, nombre, email, password, telefono, fecha_nacimiento, direccion, region, sede, carrera, año, semestre, modalidad, jornada, cohorte, estado_academico, promedio, creditos_aprobados, creditos_totales) VALUES
('19.234.567-8', 'Carlos Andrés Mendoza Vargas', 'carlos.mendoza@duocuc.cl', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '+56 9 8765 4321', '1999-03-15', 'Av. Vicuña Mackenna 3939, La Florida', 'Región Metropolitana', 'Plaza Vespucio', 'Ingeniería en Informática', 4, 8, 'Presencial', 'Diurna', '2021', 'Regular', 6.2, 198, 240),
('18.123.456-7', 'María José González Silva', 'maria.gonzalez@duocuc.cl', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '+56 9 7654 3210', '2000-07-22', 'Av. Los Leones 1842, Providencia', 'Región Metropolitana', 'Plaza Vespucio', 'Ingeniería en Informática', 3, 6, 'Presencial', 'Diurna', '2022', 'Regular', 6.8, 150, 240),
('20.345.678-9', 'Juan Pablo Rodríguez López', 'juan.rodriguez@duocuc.cl', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '+56 9 6543 2109', '2001-11-10', 'Calle San Martín 456, Santiago', 'Región Metropolitana', 'Plaza Vespucio', 'Administración de Empresas', 2, 4, 'Presencial', 'Vespertina', '2023', 'Regular', 5.9, 96, 240),
('17.987.654-3', 'Ana Carolina Torres Muñoz', 'ana.torres@duocuc.cl', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '+56 9 5432 1098', '1998-05-18', 'Av. Grecia 8500, Ñuñoa', 'Región Metropolitana', 'Plaza Vespucio', 'Diseño Gráfico', 4, 7, 'Presencial', 'Diurna', '2021', 'Regular', 6.5, 175, 210);

-- =====================================================
-- DATOS DE BENEFICIOS
-- =====================================================

INSERT INTO beneficios (usuario_id, nombre, descripcion, estado, porcentaje, fecha_inicio, fecha_fin) VALUES
(1, 'Beca de Excelencia Académica DUOC UC', 'Beca por rendimiento académico destacado', 'ACTIVA', 50.00, '2024-03-01', '2024-12-31'),
(1, 'Gratuidad', 'Beneficio estatal de gratuidad', 'ACTIVA', 100.00, '2024-03-01', '2024-12-31'),
(2, 'Beca Socioeconómica', 'Apoyo económico por situación familiar', 'ACTIVA', 25.00, '2024-03-01', '2024-12-31'),
(3, 'Beca de Alimentación JUNAEB', 'Beneficio de alimentación estudiantil', 'ACTIVA', 100.00, '2024-03-01', '2024-12-31');

-- =====================================================
-- DATOS DE PROFESORES
-- =====================================================

INSERT INTO profesores (rut, nombre, email, telefono, especialidad, titulo, grado_academico) VALUES
('12.345.678-9', 'Dr. Carlos Eduardo Mendoza Rivera', 'carlos.mendoza@profesor.duocuc.cl', '+56 2 2345 6789', 'Programación y Desarrollo de Software', 'Doctor en Ciencias de la Computación', 'Doctor'),
('11.234.567-8', 'Ing. María Elena González Silva', 'maria.gonzalez@profesor.duocuc.cl', '+56 2 3456 7890', 'Bases de Datos y Sistemas de Información', 'Ingeniera en Informática, Magíster en TI', 'Magíster'),
('13.456.789-0', 'Prof. Roberto Silva Castro', 'roberto.silva@profesor.duocuc.cl', '+56 2 4567 8901', 'Arquitectura de Software', 'Ingeniero Civil en Computación', 'Magíster'),
('14.567.890-1', 'Lic. Ana Isabel Torres Mitchell', 'ana.torres@profesor.duocuc.cl', '+56 2 5678 9012', 'Idiomas Técnicos', 'Licenciada en Lingüística Aplicada', 'Licenciado'),
('15.678.901-2', 'Dr. Luis Fernando Vargas Herrera', 'luis.vargas@profesor.duocuc.cl', '+56 2 6789 0123', 'Redes y Telecomunicaciones', 'Doctor en Ingeniería Eléctrica', 'Doctor');

-- =====================================================
-- DATOS DE CURSOS
-- =====================================================

INSERT INTO cursos (codigo, nombre, descripcion, creditos, semestre, nivel, modalidad, categoria, estado, cupos_totales, cupos_disponibles, horario, aula, edificio) VALUES
('INFO1166', 'Programación Orientada a Objetos', 'Fundamentos de programación orientada a objetos usando Java', 6, '2024-2', 'Intermedio', 'Presencial', 'programming', 'Activo', 32, 28, 'Lun-Mié-Vie 08:00-09:30', 'Lab. Informática A', 'Edificio Tecnológico'),
('INFO1277', 'Base de Datos II', 'Diseño y administración avanzada de bases de datos', 5, '2024-2', 'Avanzado', 'Presencial', 'database', 'Activo', 28, 25, 'Mar-Jue 10:00-11:30', 'Aula 205', 'Edificio Central'),
('INGL2201', 'Inglés Técnico II', 'Inglés aplicado al área técnica e informática', 3, '2024-2', 'Intermedio', 'Presencial', 'language', 'Activo', 35, 32, 'Vie 14:00-16:30', 'Aula 301', 'Edificio de Idiomas'),
('INFO1288', 'Arquitectura de Software', 'Principios y patrones de diseño de software', 6, '2024-2', 'Avanzado', 'Presencial', 'programming', 'Activo', 30, 27, 'Lun 10:00-11:30, Jue 09:00-10:30', 'Aula 102', 'Edificio Tecnológico'),
('INFO1304', 'Redes de Computadoras', 'Fundamentos de redes y protocolos de comunicación', 5, '2024-2', 'Intermedio', 'Presencial', 'networking', 'Activo', 25, 22, 'Mar 11:00-12:30, Vie 10:00-11:30', 'Lab. Redes', 'Edificio Tecnológico');

-- =====================================================
-- ASIGNACIÓN DE PROFESORES A CURSOS
-- =====================================================

INSERT INTO curso_profesor (curso_id, profesor_id, rol) VALUES
(1, 1, 'Titular'), -- Carlos Mendoza - POO
(2, 2, 'Titular'), -- María González - BD II
(3, 4, 'Titular'), -- Ana Torres - Inglés
(4, 3, 'Titular'), -- Roberto Silva - Arquitectura
(5, 5, 'Titular'); -- Luis Vargas - Redes

-- =====================================================
-- INSCRIPCIONES DE ESTUDIANTES
-- =====================================================

INSERT INTO inscripciones (usuario_id, curso_id, estado, nota_final, asistencia, progreso) VALUES
(1, 1, 'Inscrito', NULL, 92.5, 75.0), -- Carlos en POO
(1, 2, 'Inscrito', NULL, 88.0, 68.0), -- Carlos en BD II
(1, 3, 'Inscrito', NULL, 95.0, 82.0), -- Carlos en Inglés
(2, 1, 'Inscrito', NULL, 89.0, 70.0), -- María en POO
(2, 2, 'Inscrito', NULL, 91.0, 72.0), -- María en BD II
(3, 4, 'Inscrito', NULL, 85.0, 60.0), -- Juan en Arquitectura
(4, 3, 'Inscrito', NULL, 93.0, 85.0); -- Ana en Inglés

-- =====================================================
-- EVALUACIONES
-- =====================================================

INSERT INTO evaluaciones (curso_id, nombre, descripcion, tipo, fecha_programada, duracion_minutos, ponderacion, aula, instrucciones, estado) VALUES
(1, 'Examen Parcial - POO', 'Examen sobre conceptos fundamentales de POO y Java', 'Examen', '2024-06-15 09:00:00', 120, 30.00, 'Lab. Informática A', 'Examen teórico-práctico sobre herencia, polimorfismo y encapsulación', 'Programada'),
(1, 'Proyecto Sistema POO', 'Desarrollo de sistema completo usando principios POO', 'Proyecto', '2024-07-10 23:59:00', 0, 40.00, 'Virtual', 'Implementar sistema de gestión usando Java con interfaz gráfica', 'Programada'),
(2, 'Proyecto Base de Datos', 'Diseño e implementación de sistema de BD para empresa', 'Proyecto', '2024-06-20 23:59:00', 0, 40.00, 'Virtual', 'Diseñar BD completa con normalización y stored procedures', 'Programada'),
(3, 'Presentación Oral Técnica', 'Presentación de tema técnico en inglés', 'Proyecto', '2024-06-25 14:00:00', 15, 35.00, 'Aula 301', 'Presentación de 10-15 minutos sobre tema de especialidad', 'Programada');

-- =====================================================
-- CALIFICACIONES
-- =====================================================

INSERT INTO calificaciones (evaluacion_id, usuario_id, nota, fecha_entrega, retroalimentacion, estado) VALUES
(1, 1, 6.2, '2024-06-15 10:45:00', 'Buen manejo de conceptos, mejorar implementación de interfaces', 'Calificada'),
(1, 2, 6.8, '2024-06-15 10:30:00', 'Excelente comprensión de herencia y polimorfismo', 'Calificada'),
(2, 1, NULL, NULL, NULL, 'Pendiente'),
(3, 1, 6.5, '2024-06-20 22:30:00', 'Buen diseño de BD, falta optimización en consultas', 'Calificada');

-- =====================================================
-- HORARIOS
-- =====================================================

INSERT INTO horarios (curso_id, dia_semana, hora_inicio, hora_fin, aula, edificio, tipo_clase) VALUES
-- POO
(1, 'Lunes', '08:00:00', '09:30:00', 'Lab. Informática A', 'Edificio Tecnológico', 'Laboratorio'),
(1, 'Miércoles', '08:00:00', '09:30:00', 'Lab. Informática A', 'Edificio Tecnológico', 'Laboratorio'),
(1, 'Viernes', '08:00:00', '09:30:00', 'Lab. Informática A', 'Edificio Tecnológico', 'Laboratorio'),
-- BD II
(2, 'Martes', '10:00:00', '11:30:00', 'Aula 205', 'Edificio Central', 'Teórica'),
(2, 'Jueves', '10:00:00', '11:30:00', 'Lab. Informática B', 'Edificio Tecnológico', 'Laboratorio'),
-- Inglés
(3, 'Viernes', '14:00:00', '16:30:00', 'Aula 301', 'Edificio de Idiomas', 'Práctica'),
-- Arquitectura
(4, 'Lunes', '10:00:00', '11:30:00', 'Aula 102', 'Edificio Tecnológico', 'Teórica'),
(4, 'Jueves', '09:00:00', '10:30:00', 'Aula 102', 'Edificio Tecnológico', 'Teórica'),
-- Redes
(5, 'Martes', '11:00:00', '12:30:00', 'Lab. Redes', 'Edificio Tecnológico', 'Laboratorio'),
(5, 'Viernes', '10:00:00', '11:30:00', 'Lab. Redes', 'Edificio Tecnológico', 'Laboratorio');

-- =====================================================
-- COMUNICACIONES
-- =====================================================

INSERT INTO comunicaciones (titulo, contenido, tipo, prioridad, dirigido_a, departamento, autor, fecha_publicacion, activo) VALUES
('Proceso de Matrícula 2025', 'El proceso de matrícula para el año académico 2025 estará disponible desde el 15 de diciembre de 2024 hasta el 15 de enero de 2025. Se recomienda realizar el proceso con anticipación.', 'Academico', 'Alta', 'Estudiantes', 'Secretaría Académica', 'Secretaría Académica', '2024-05-25 09:00:00', TRUE),
('Evaluación Docente 2024-2', 'Ya está disponible la evaluación docente del segundo semestre 2024. Tu opinión es importante para mejorar la calidad académica. Plazo hasta el 30 de junio.', 'Notificacion', 'Media', 'Estudiantes', 'Dirección Académica', 'Dirección Académica', '2024-05-23 14:30:00', TRUE),
('Mantención Sistema U-Campus', 'El sistema estará en mantención el sábado 29 de junio de 14:00 a 18:00 hrs. Durante este período no estará disponible el acceso al portal.', 'Administrativo', 'Media', 'Todos', 'Dirección de TI', 'Soporte Técnico', '2024-05-27 10:15:00', TRUE),
('Postulación Becas de Alimentación', 'Abierta postulación para becas JUNAEB período 2024-2. Requisitos y formularios disponibles en secretaría de estudiantes.', 'Anuncio', 'Media', 'Estudiantes', 'Bienestar Estudiantil', 'Bienestar Estudiantil', '2024-05-20 11:00:00', TRUE);

-- =====================================================
-- SERVICIOS ESTUDIANTILES
-- =====================================================

INSERT INTO servicios_estudiantiles (nombre, descripcion, categoria, precio, tiempo_entrega, formato, requisitos, estado) VALUES
('Certificado de Alumno Regular', 'Certificado que acredita la condición de estudiante activo', 'CERTIFICADOS', 0.00, '24 horas', 'DIGITAL,FISICO', '["Estar matriculado", "Sin deudas pendientes"]', 'DISPONIBLE'),
('Concentración de Notas', 'Documento oficial con historial académico completo', 'CERTIFICADOS', 3500.00, '3-5 días hábiles', 'DIGITAL,FISICO', '["Solicitud formal", "Pago de arancel"]', 'DISPONIBLE'),
('Reposición de Credencial', 'Nueva credencial universitaria por pérdida o daño', 'CREDENCIALES', 8500.00, '5-7 días hábiles', 'FISICO', '["Denuncia de pérdida", "Foto tamaño carnet", "Pago de arancel"]', 'DISPONIBLE'),
('Duplicado de Diploma', 'Copia oficial del título profesional', 'CERTIFICADOS', 15000.00, '10-15 días hábiles', 'FISICO', '["Solicitud notarial", "Motivo justificado", "Pago de arancel"]', 'DISPONIBLE'),
('Cambio de Carrera', 'Trámite para cambio de programa académico', 'TRAMITES', 25000.00, '15-20 días hábiles', 'PRESENCIAL', '["Cumplir requisitos académicos", "Entrevista", "Documentación completa"]', 'DISPONIBLE');

-- =====================================================
-- REGISTROS DE ASISTENCIA (Datos de ejemplo)
-- =====================================================

INSERT INTO asistencia (usuario_id, curso_id, fecha, presente, justificada, observaciones) VALUES
(1, 1, '2024-05-20', TRUE, FALSE, NULL),
(1, 1, '2024-05-22', TRUE, FALSE, NULL),
(1, 1, '2024-05-24', FALSE, TRUE, 'Cita médica justificada'),
(1, 2, '2024-05-21', TRUE, FALSE, NULL),
(1, 2, '2024-05-23', TRUE, FALSE, NULL),
(2, 1, '2024-05-20', TRUE, FALSE, NULL),
(2, 1, '2024-05-22', TRUE, FALSE, NULL),
(2, 2, '2024-05-21', TRUE, FALSE, NULL);

-- Commit de transacciones
COMMIT;