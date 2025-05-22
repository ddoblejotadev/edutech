-- Crear tabla tipo_curso si no existe
CREATE TABLE IF NOT EXISTS tipo_curso (
    id_tipocurso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- Crear tabla curso si no existe
CREATE TABLE IF NOT EXISTS curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    duracion_horas INT,
    nivel VARCHAR(50),
    categoria VARCHAR(50),
    precio DECIMAL(10,2),
    sence VARCHAR(1),
    fec_creacion DATE,
    fec_publicacion DATE,
    id_tipocurso INT,
    version BIGINT DEFAULT 0,
    FOREIGN KEY (id_tipocurso) REFERENCES tipo_curso(id_tipocurso)
);