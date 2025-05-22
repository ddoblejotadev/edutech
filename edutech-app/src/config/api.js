// Configuración para diferentes entornos
export const API_URL = 'http://10.0.2.2:8081'; // Para emulador Android
// export const API_URL = 'http://localhost:8081'; // Para emulador iOS
// export const API_URL = 'http://192.168.1.100:8081'; // Para dispositivo físico, usa tu IP local

export const AUTH_ENDPOINTS = {
  LOGIN: '/api/auth/signin',
  REGISTER: '/api/auth/signup',
};

export const COURSE_ENDPOINTS = {
  GET_ALL: '/api/cursos',
  GET_BY_ID: (id) => `/api/cursos/${id}`,
};
