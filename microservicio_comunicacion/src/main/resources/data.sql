-- Insertar mensajes iniciales
INSERT INTO mensaje (remitente, destinatario, asunto, contenido, fecha_envio, leido)
SELECT '12345678', '87654321', 'Bienvenida al curso', 'Estimado estudiante, bienvenido al curso de Java Avanzado', NOW(), false
WHERE NOT EXISTS (SELECT 1 FROM mensaje WHERE remitente = '12345678' AND destinatario = '87654321' AND asunto = 'Bienvenida al curso');

INSERT INTO mensaje (remitente, destinatario, asunto, contenido, fecha_envio, leido)
SELECT '87654321', '12345678', 'Consulta sobre el curso', '¿Cuándo comienza el curso de Java Avanzado?', NOW(), false
WHERE NOT EXISTS (SELECT 1 FROM mensaje WHERE remitente = '87654321' AND destinatario = '12345678' AND asunto = 'Consulta sobre el curso');

-- Insertar notificaciones iniciales
INSERT INTO notificacion (id_persona, titulo, contenido, fecha_creacion, tipo, leida) 
SELECT '12345678', 'Nuevo curso disponible', 'Se ha añadido un nuevo curso: Python para Data Science', NOW(), 'CURSO', false
WHERE NOT EXISTS (SELECT 1 FROM notificacion WHERE id_persona = '12345678' AND titulo = 'Nuevo curso disponible');

INSERT INTO notificacion (id_persona, titulo, contenido, fecha_creacion, tipo, leida)
SELECT '87654321', 'Actualización de plataforma', 'Hemos actualizado la plataforma con nuevas funcionalidades', NOW(), 'SISTEMA', false
WHERE NOT EXISTS (SELECT 1 FROM notificacion WHERE id_persona = '87654321' AND titulo = 'Actualización de plataforma');