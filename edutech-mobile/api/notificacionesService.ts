import apiClient from './apiClient';
import { Notificacion } from '../types';

// Clase de error personalizada para el servicio de notificaciones
export class NotificacionesError extends Error {
  statusCode: number;
  data?: any;

  constructor(message: string, statusCode: number = 500, data?: any) {
    super(message);
    this.name = 'NotificacionesError';
    this.statusCode = statusCode;
    this.data = data;
  }
}

// Endpoints para el microservicio de comunicación (notificaciones)
export const notificacionesApi = {
  // Obtener todas las notificaciones
  getAll: async (): Promise<Notificacion[]> => {
    try {
      const response = await apiClient.get('/api/notificaciones');
      return response.data;
    } catch (error: any) {
      throw new NotificacionesError(
        'Error al obtener notificaciones',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener notificación por ID
  getById: async (id: number): Promise<Notificacion> => {
    try {
      const response = await apiClient.get(`/api/notificaciones/${id}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 404) {
        throw new NotificacionesError('Notificación no encontrada', 404);
      }
      throw new NotificacionesError(
        'Error al obtener la notificación',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener notificaciones por persona
  getByPersona: async (idPersona: string): Promise<Notificacion[]> => {
    try {
      const response = await apiClient.get(`/api/notificaciones/persona/${idPersona}`);
      return response.data;
    } catch (error: any) {
      throw new NotificacionesError(
        'Error al obtener notificaciones de la persona',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Obtener notificaciones no leídas por persona
  getNoLeidas: async (idPersona: string): Promise<Notificacion[]> => {
    try {
      const response = await apiClient.get(`/api/notificaciones/noleidas/${idPersona}`);
      return response.data;
    } catch (error: any) {
      throw new NotificacionesError(
        'Error al obtener notificaciones no leídas',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Crear una nueva notificación
  crear: async (notificacion: Partial<Notificacion>): Promise<Notificacion> => {
    try {
      const response = await apiClient.post('/api/notificaciones', notificacion);
      return response.data;
    } catch (error: any) {
      throw new NotificacionesError(
        'Error al crear la notificación',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Marcar notificación como leída
  marcarLeida: async (id: number): Promise<Notificacion> => {
    try {
      const response = await apiClient.put(`/api/notificaciones/${id}/leida`);
      return response.data;
    } catch (error: any) {
      throw new NotificacionesError(
        'Error al marcar la notificación como leída',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Eliminar una notificación
  eliminar: async (id: number): Promise<boolean> => {
    try {
      await apiClient.delete(`/api/notificaciones/${id}`);
      return true;
    } catch (error: any) {
      throw new NotificacionesError(
        'Error al eliminar la notificación',
        error.response?.status,
        error.response?.data
      );
    }
  }
};
