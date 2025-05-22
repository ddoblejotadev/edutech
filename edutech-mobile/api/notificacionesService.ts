import apiClient from './apiClient';

// Endpoints para el microservicio de comunicación (notificaciones)
export const notificacionesApi = {
  getAll: () => apiClient.get('/api/notificaciones'),
  getById: (id: number) => apiClient.get(`/api/notificaciones/${id}`),
  getByPersona: (idPersona: string) => apiClient.get(`/api/notificaciones/persona/${idPersona}`),
  getNoLeidas: (idPersona: string) => apiClient.get(`/api/notificaciones/noleidas/${idPersona}`),
  crear: (notificacion: any) => apiClient.post('/api/notificaciones', notificacion),
  marcarLeida: (id: number) => apiClient.put(`/api/notificaciones/${id}/leida`),
  eliminar: (id: number) => apiClient.delete(`/api/notificaciones/${id}`)
};
