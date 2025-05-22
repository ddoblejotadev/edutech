// Tipos para los modelos de datos
export interface Notificacion {
  id: number;
  titulo: string;
  mensaje: string;
  tipo: string;
  idPersona: string;
  fechaCreacion: string;
  leida: boolean;
}

export interface EjecucionPersona {
  id: number;
  idEjecucion: number;
  rutPersona: string;
  fechaInscripcion: string;
  estado: string;
}

export interface Persona {
  rut: string;
  nombres: string;
  apellidos: string;
  email: string;
  telefono: string;
  rol: string;
  avatar?: string;
}

export interface Curso {
  id: number;
  nombre: string;
  descripcion: string;
  duracion: number;
  imagen: string;
}

export interface Ejecucion {
  id: number;
  idCurso: number;
  fechaInicio: string;
  fechaFin: string;
  modalidad: string;
  cupos: number;
  estado: string;
}
