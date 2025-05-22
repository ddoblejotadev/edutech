-- Eliminar tablas si existen para recrearlas
DROP TABLE IF EXISTS persona;
DROP TABLE IF EXISTS tipo_persona;

-- Crear tabla tipo_persona
CREATE TABLE IF NOT EXISTS tipo_persona (
    id_tipo_persona INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

-- Crear tabla persona (sin auto_increment en rut que es VARCHAR)
CREATE TABLE IF NOT EXISTS persona (
    rut VARCHAR(8) NOT NULL PRIMARY KEY,
    dv VARCHAR(1) NOT NULL,
    nombres VARCHAR(50) NOT NULL,
    ape1 VARCHAR(50) NOT NULL,
    ape2 VARCHAR(50),
    fec_nac DATE,
    correo VARCHAR(100),
    telefono VARCHAR(15),
    direccion VARCHAR(100),
    id_tipo_persona INT NOT NULL,
    CONSTRAINT unico_correo_rut UNIQUE (correo, rut),
    FOREIGN KEY (id_tipo_persona) REFERENCES tipo_persona(id_tipo_persona)
);