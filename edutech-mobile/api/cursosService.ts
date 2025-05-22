import apiClient from './apiClient';
import { Curso, Ejecucion } from '../types';

// Clase de error personalizada para el servicio de cursos
export class CursosError extends Error {
  statusCode: number;
  data?: any;

  constructor(message: string, statusCode: number = 500, data?: any) {
    super(message);
    this.name = 'CursosError';
    this.statusCode = statusCode;
    this.data = data;
  }
}

// Endpoints para el microservicio de cursos
export const cursosApi = {
  // Obtener todos los cursos
  getAll: async (): Promise<Curso[]> => {
    try {
      const response = await apiClient.get('/api/cursos');
      return response.data;
    } catch (error: any) {
      throw new CursosError(
        'Error al obtener cursos',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener curso por ID
  getById: async (id: number): Promise<Curso> => {
    try {
      const response = await apiClient.get(`/api/cursos/${id}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        throw new CursosError('Curso no encontrado', 404);
      }
      throw new CursosError(
        'Error al obtener el curso',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener ejecuciones de un curso
  getEjecuciones: async (idCurso: number): Promise<Ejecucion[]> => {
    try {
      const response = await apiClient.get(`/api/cursos/${idCurso}/ejecuciones`);
      return response.data;
    } catch (error: any) {
      throw new CursosError(
        'Error al obtener ejecuciones del curso',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener ejecución específica por ID
  getEjecucionById: async (idEjecucion: number): Promise<Ejecucion> => {
    try {
      const response = await apiClient.get(`/api/ejecuciones/${idEjecucion}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        throw new CursosError('Ejecución no encontrada', 404);
      }
      throw new CursosError(
        'Error al obtener la ejecución',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Buscar cursos por término
  buscar: async (query: string): Promise<Curso[]> => {
    try {
      const response = await apiClient.get(`/api/cursos/buscar?q=${encodeURIComponent(query)}`);
      return response.data;
    } catch (error: any) {
      throw new CursosError(
        'Error al buscar cursos',
        error.response?.status,
        error.response?.data
      );
    }
  }
};
