// Configuración para diferentes entornos
export const API_URL = 'http://10.0.2.2:8762'; // Para emulador Android - API Gateway
// export const API_URL = 'http://localhost:8762'; // Para emulador iOS - API Gateway
// export const API_URL = 'http://192.168.1.100:8762'; // Para dispositivo físico, usa tu IP local

export const AUTH_ENDPOINTS = {
  LOGIN: '/api/personas/login',
  REGISTER: '/api/personas/register',
};

export const COURSE_ENDPOINTS = {
  GET_ALL: '/api/cursos',
  GET_BY_ID: (id) => `/api/cursos/${id}`,
};
