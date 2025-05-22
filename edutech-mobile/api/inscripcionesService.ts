import apiClient from './apiClient';

// Endpoints para el microservicio de ejecución (inscripciones)
export const inscripcionesApi = {
  inscribirEstudiante: (datos: { rutPersona: string, idEjecucion: number }) => 
    apiClient.post('/api/inscripciones', datos),
  cancelarInscripcion: (rutPersona: string, idEjecucion: number) => 
    apiClient.delete(`/api/inscripciones/${rutPersona}/${idEjecucion}`),
  getInscripcionesByPersona: (rutPersona: string) => 
    apiClient.get(`/api/inscripciones/persona/${rutPersona}`),
  getInscripcionesByEjecucion: (idEjecucion: number) => 
    apiClient.get(`/api/inscripciones/ejecucion/${idEjecucion}`)
};
