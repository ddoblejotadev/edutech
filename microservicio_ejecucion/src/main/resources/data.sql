-- Insertar datos de ejecuciones si la tabla está vacía
INSERT INTO ejecucion (id_ejecucion, fec_inicio, fec_fin, estado, id_curso) 
SELECT 1, '2025-06-01', '2025-07-30', 'ACTIVA', 1
WHERE NOT EXISTS (SELECT * FROM ejecucion WHERE id_ejecucion = 1);

INSERT INTO ejecucion (id_ejecucion, fec_inicio, fec_fin, estado, id_curso) 
SELECT 2, '2025-07-15', '2025-08-30', 'PLANIFICADA', 2
WHERE NOT EXISTS (SELECT * FROM ejecucion WHERE id_ejecucion = 2);

INSERT INTO ejecucion (id_ejecucion, fec_inicio, fec_fin, estado, id_curso) 
SELECT 3, '2025-08-01', '2025-09-15', 'ACTIVA', 3
WHERE NOT EXISTS (SELECT * FROM ejecucion WHERE id_ejecucion = 3);

-- Insertar inscripciones iniciales
INSERT INTO ejecucion_persona (rut_persona, id_ejecucion) 
SELECT '12345678', 1
WHERE NOT EXISTS (SELECT * FROM ejecucion_persona WHERE rut_persona = '12345678' AND id_ejecucion = 1);

INSERT INTO ejecucion_persona (rut_persona, id_ejecucion) 
SELECT '87654321', 2
WHERE NOT EXISTS (SELECT * FROM ejecucion_persona WHERE rut_persona = '87654321' AND id_ejecucion = 2);