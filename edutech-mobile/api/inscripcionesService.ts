import apiClient from './apiClient';
import { EjecucionPersona } from '../types';

// Clase de error personalizada para el servicio de inscripciones
export class InscripcionesError extends Error {
  statusCode: number;
  data?: any;

  constructor(message: string, statusCode: number = 500, data?: any) {
    super(message);
    this.name = 'InscripcionesError';
    this.statusCode = statusCode;
    this.data = data;
  }
}

// Endpoints para el microservicio de ejecución (inscripciones)
export const inscripcionesApi = {
  // Inscribir estudiante en un curso
  inscribirEstudiante: async (datos: { rutPersona: string, idEjecucion: number }): Promise<string> => {
    try {
      const response = await apiClient.post('/api/inscripciones', datos);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 400) {
        throw new InscripcionesError('Datos de inscripción inválidos', 400, error.response?.data);
      }
      throw new InscripcionesError(
        'Error al inscribir estudiante',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Cancelar inscripción
  cancelarInscripcion: async (rutPersona: string, idEjecucion: number): Promise<boolean> => {
    try {
      const response = await apiClient.delete(`/api/inscripciones/${rutPersona}/${idEjecucion}`);
      return response.status === 200;
    } catch (error: any) {
      if (error.response?.status === 404) {
        throw new InscripcionesError('Inscripción no encontrada', 404);
      }
      throw new InscripcionesError(
        'Error al cancelar la inscripción',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener inscripciones por persona
  getInscripcionesByPersona: async (rutPersona: string): Promise<EjecucionPersona[]> => {
    try {
      const response = await apiClient.get(`/api/inscripciones/persona/${rutPersona}`);
      return response.data;
    } catch (error: any) {
      throw new InscripcionesError(
        'Error al obtener inscripciones de la persona',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener inscripciones por ejecución
  getInscripcionesByEjecucion: async (idEjecucion: number): Promise<EjecucionPersona[]> => {
    try {
      const response = await apiClient.get(`/api/inscripciones/ejecucion/${idEjecucion}`);
      return response.data;
    } catch (error: any) {
      throw new InscripcionesError(
        'Error al obtener inscripciones de la ejecución',
        error.response?.status,
        error.response?.data
      );
    }
  }
};
