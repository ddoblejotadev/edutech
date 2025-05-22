-- Agrega la columna version a la tabla curso
ALTER TABLE curso ADD COLUMN IF NOT EXISTS version BIGINT DEFAULT 0;

-- Eliminar datos existentes primero para evitar duplicados
DELETE FROM curso;
DELETE FROM tipo_curso;

-- Insertar tipos de curso primero (importante: antes de los cursos)
INSERT INTO tipo_curso (id_tipocurso, nombre) VALUES (1, 'Programacion');
INSERT INTO tipo_curso (id_tipocurso, nombre) VALUES (2, 'Diseno');
INSERT INTO tipo_curso (id_tipocurso, nombre) VALUES (3, 'Marketing Digital');
INSERT INTO tipo_curso (id_tipocurso, nombre) VALUES (4, 'Administracion');
INSERT INTO tipo_curso (id_tipocurso, nombre) VALUES (5, 'Idiomas');

-- Insertar tipos de curso (solo si no existen)
INSERT INTO tipo_curso (nombre) 
SELECT 'Presencial' FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM tipo_curso WHERE nombre = 'Presencial');

INSERT INTO tipo_curso (nombre) 
SELECT 'Online' FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM tipo_curso WHERE nombre = 'Online');

INSERT INTO tipo_curso (nombre) 
SELECT 'Mixto' FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM tipo_curso WHERE nombre = 'Mixto');

-- Ahora insertar cursos con todos los campos requeridos
-- Estructura para cada INSERT:
-- id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel
INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (1, 'Java Basico', 'Curso introductorio a la programacion en Java', CURRENT_DATE(), '2023-06-01', 'S', 120000, 1, 'Informatica', 40, 'Basico');

INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (2, 'Python para Ciencia de Datos', 'Aprende Python orientado al analisis de datos', CURRENT_DATE(), '2023-07-15', 'S', 150000, 1, 'Informatica', 45, 'Intermedio');

INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (3, 'Diseno UX/UI', 'Principios del diseno de experiencia de usuario', CURRENT_DATE(), '2023-05-10', 'N', 100000, 2, 'Diseno', 35, 'Intermedio');

INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (4, 'Marketing en Redes Sociales', 'Estrategias efectivas de marketing digital', CURRENT_DATE(), '2023-08-01', 'S', 80000, 3, 'Marketing', 25, 'Basico');

INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (5, 'Gestion de Proyectos', 'Metodologias agiles para la gestion de proyectos', CURRENT_DATE(), '2023-09-15', 'S', 130000, 4, 'Administracion', 50, 'Avanzado');

INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (6, 'Ingles para Negocios', 'Ingles tecnico orientado al mundo empresarial', CURRENT_DATE(), '2023-10-01', 'N', 90000, 5, 'Idiomas', 60, 'Intermedio');

INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (7, 'Desarrollo Web Full Stack', 'Aprende a desarrollar aplicaciones web completas', CURRENT_DATE(), '2023-11-01', 'S', 200000, 1, 'Informatica', 120, 'Avanzado');

INSERT INTO curso (id_curso, nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES (8, 'Diseno Grafico con Adobe', 'Domina las herramientas de Adobe para diseno grafico', CURRENT_DATE(), '2023-12-01', 'S', 110000, 2, 'Diseno', 40, 'Basico');

-- Insertar tipos de curso
INSERT INTO tipo_curso (nombre) VALUES ('Presencial');
INSERT INTO tipo_curso (nombre) VALUES ('Online');
INSERT INTO tipo_curso (nombre) VALUES ('Mixto');

-- Insertar cursos con precios dentro del rango DECIMAL(10,2)
INSERT INTO curso (nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES ('Java Básico', 'Curso introductorio a la programación en Java', CURRENT_DATE(), '2023-06-01', 'S', 9999.99, 1, 'Informática', 40, 'Básico');

INSERT INTO curso (nombre, descripcion, fec_creacion, fec_publicacion, sence, precio, id_tipocurso, categoria, duracion_horas, nivel)
VALUES ('Python para Ciencia de Datos', 'Aprende Python enfocado en análisis de datos', CURRENT_DATE(), '2023-07-01', 'S', 8500.00, 2, 'Informática', 35, 'Intermedio');