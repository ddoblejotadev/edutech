-- Eliminar datos existentes primero para evitar duplicados
DELETE FROM persona;
DELETE FROM tipo_persona;

-- Insertar tipos de persona
INSERT INTO tipo_persona (id_tipo_persona, nombre) VALUES (1, 'Estudiante');
INSERT INTO tipo_persona (id_tipo_persona, nombre) VALUES (2, 'Profesor');
INSERT INTO tipo_persona (id_tipo_persona, nombre) VALUES (3, 'Administrador');

-- Insertar personas (sin caracteres especiales)
INSERT INTO persona (rut, dv, nombres, ape1, ape2, fec_nac, correo, telefono, direccion, id_tipo_persona)
VALUES ('12345678', 'K', 'Juan Pedro', 'Perez', 'Gonzalez', '1990-01-15', 'juan.perez@edutech.cl', '912345678', 'Av. Providencia 123', 1);

INSERT INTO persona (rut, dv, nombres, ape1, ape2, fec_nac, correo, telefono, direccion, id_tipo_persona)
VALUES ('98765432', '1', 'Maria Jose', 'Lopez', 'Fernandez', '1985-05-20', 'maria.lopez@edutech.cl', '987654321', 'Calle Principal 456', 2);

INSERT INTO persona (rut, dv, nombres, ape1, ape2, fec_nac, correo, telefono, direccion, id_tipo_persona)
VALUES ('11223344', '5', 'Carlos Alberto', 'Rodriguez', 'Martinez', '1992-10-08', 'carlos.rodriguez@edutech.cl', '955667788', 'Pasaje Los Olivos 789', 3);