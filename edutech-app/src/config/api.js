// Configuración de URLs de los microservicios
const API_BASE_URL = 'http://localhost';

export const API_ENDPOINTS = {
  // Microservicio Persona (Puerto 8081)
  PERSONA: `${API_BASE_URL}:8081/api`,
  
  // Microservicio Curso (Puerto 8082) 
  CURSO: `${API_BASE_URL}:8082/api`,
  
  // Microservicio Ejecución (Puerto 8083)
  EJECUCION: `${API_BASE_URL}:8083/api`,
  
  // Microservicio Evaluación (Puerto 8084)
  EVALUACION: `${API_BASE_URL}:8084/api`,
  
  // Microservicio Comunicación (Puerto 8085)
  COMUNICACION: `${API_BASE_URL}:8085/api`
};

// URLs específicas por funcionalidad
export const ENDPOINTS = {
  // Autenticación y usuarios
  AUTH: {
    LOGIN: `${API_ENDPOINTS.PERSONA}/auth/login`,
    REGISTER: `${API_ENDPOINTS.PERSONA}/auth/register`,
    PROFILE: `${API_ENDPOINTS.PERSONA}/usuarios/perfil`,
    USERS: `${API_ENDPOINTS.PERSONA}/usuarios`
  },
  
  // Cursos
  COURSES: {
    LIST: `${API_ENDPOINTS.CURSO}/cursos`,
    DETAIL: (id) => `${API_ENDPOINTS.CURSO}/cursos/${id}`,
    INSCRIPTIONS: `${API_ENDPOINTS.CURSO}/inscripciones`,
    PROFESSORS: `${API_ENDPOINTS.CURSO}/profesores`
  },
  
  // Evaluaciones y notas
  EVALUATIONS: {
    LIST: `${API_ENDPOINTS.EVALUACION}/evaluaciones`,
    GRADES: `${API_ENDPOINTS.EVALUACION}/calificaciones`,
    STUDENT_GRADES: (studentId) => `${API_ENDPOINTS.EVALUACION}/calificaciones/estudiante/${studentId}`
  },
  
  // Horarios y asistencia
  SCHEDULE: {
    LIST: `${API_ENDPOINTS.EJECUCION}/horarios`,
    ATTENDANCE: `${API_ENDPOINTS.EJECUCION}/asistencia`,
    STUDENT_SCHEDULE: (studentId) => `${API_ENDPOINTS.EJECUCION}/horarios/estudiante/${studentId}`
  },
  
  // Comunicaciones y servicios
  COMMUNICATIONS: {
    ANNOUNCEMENTS: `${API_ENDPOINTS.COMUNICACION}/comunicaciones`,
    SERVICES: `${API_ENDPOINTS.COMUNICACION}/servicios-estudiantiles`
  }
};

// Configuración para requests
export const API_CONFIG = {
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
};

// Helper para construir URLs con parámetros
export const buildUrl = (baseUrl, params = {}) => {
  const url = new URL(baseUrl);
  Object.keys(params).forEach(key => {
    if (params[key] !== null && params[key] !== undefined) {
      url.searchParams.append(key, params[key]);
    }
  });
  return url.toString();
};

export default {
  API_ENDPOINTS,
  ENDPOINTS,
  API_CONFIG,
  buildUrl
};