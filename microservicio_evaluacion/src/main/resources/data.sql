-- Insertar evaluaciones iniciales
INSERT INTO evaluacion (titulo, descripcion, id_curso, id_ejecucion, fecha_creacion, fecha_vencimiento, puntaje_maximo, tipo)
SELECT 'Examen Final Java', 'Evaluación final del curso de Java', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 100, 'EXAMEN'
WHERE NOT EXISTS (SELECT 1 FROM evaluacion WHERE titulo = 'Examen Final Java' AND id_curso = 1);

INSERT INTO evaluacion (titulo, descripcion, id_curso, id_ejecucion, fecha_creacion, fecha_vencimiento, puntaje_maximo, tipo)
SELECT 'Tarea 1: Programación Orientada a Objetos', 'Implementar clases y objetos', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY), 50, 'TAREA'
WHERE NOT EXISTS (SELECT 1 FROM evaluacion WHERE titulo = 'Tarea 1: Programación Orientada a Objetos' AND id_curso = 1);

-- Insertar preguntas
INSERT INTO pregunta (enunciado, descripcion, tipo, puntaje, id_evaluacion)
SELECT '¿Cuál es la diferencia entre una clase y un objeto en Java?', 'Explique con ejemplos', 'DESARROLLO', 20.0, 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuál es la diferencia entre una clase y un objeto en Java?' AND id_evaluacion = 1);

INSERT INTO pregunta (enunciado, descripcion, tipo, puntaje, id_evaluacion)
SELECT '¿Qué es la herencia en Java?', NULL, 'MULTIPLE_CHOICE', 10.0, 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué es la herencia en Java?' AND id_evaluacion = 1);

-- Insertar opciones
INSERT INTO opcion (texto, correcta, id_pregunta)
SELECT 'Es un mecanismo para reutilizar código que permite que una clase adquiera propiedades de otra', TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM opcion WHERE texto = 'Es un mecanismo para reutilizar código que permite que una clase adquiera propiedades de otra' AND id_pregunta = 2);

INSERT INTO opcion (texto, correcta, id_pregunta)
SELECT 'Es una técnica para crear objetos a partir de interfaces', FALSE, 2
WHERE NOT EXISTS (SELECT 1 FROM opcion WHERE texto = 'Es una técnica para crear objetos a partir de interfaces' AND id_pregunta = 2);

INSERT INTO opcion (texto, correcta, id_pregunta)
SELECT 'Es un patrón de diseño exclusivo de Java', FALSE, 2
WHERE NOT EXISTS (SELECT 1 FROM opcion WHERE texto = 'Es un patrón de diseño exclusivo de Java' AND id_pregunta = 2);

-- Insertar calificaciones
INSERT INTO calificacion (id_evaluacion, rut_estudiante, puntaje_obtenido, comentario, fecha_calificacion, fecha_entrega)
SELECT 1, '12345678', 85.0, 'Buen trabajo, pero falta profundizar en los conceptos de polimorfismo', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM calificacion WHERE id_evaluacion = 1 AND rut_estudiante = '12345678');

-- Insertar respuestas
INSERT INTO respuesta (rut_estudiante, id_pregunta, id_opcion, texto_respuesta, correcta, puntaje_obtenido)
SELECT '12345678', 1, NULL, 'Una clase es una plantilla o un plano para crear objetos, mientras que un objeto es una instancia de una clase.', TRUE, 20.0
WHERE NOT EXISTS (SELECT 1 FROM respuesta WHERE rut_estudiante = '12345678' AND id_pregunta = 1);

INSERT INTO respuesta (rut_estudiante, id_pregunta, id_opcion, texto_respuesta, correcta, puntaje_obtenido)
SELECT '12345678', 2, 1, NULL, TRUE, 10.0
WHERE NOT EXISTS (SELECT 1 FROM respuesta WHERE rut_estudiante = '12345678' AND id_pregunta = 2);