// Archivo que centraliza las llamadas a la API para los componentes
import { authService, courseService, evaluationService, userService } from './api';

// Función para manejar errores de API de manera consistente
const handleApiError = (error, customMessage = 'Ha ocurrido un error') => {
  console.error('API Error:', error);
  // Determinar el mensaje basado en el error
  if (error.message && error.message.includes('tiempo de espera')) {
    return 'La conexión al servidor está tardando demasiado. Por favor, inténtalo más tarde.';
  }
  if (error.message === 'Network request failed') {
    return 'No hay conexión a internet. Verifica tu conexión y vuelve a intentarlo.';
  }
  return customMessage;
};

// Servicio de autenticación con manejo de errores
export const Auth = {
  login: async (username, password) => {
    try {
      return await authService.login(username, password);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al iniciar sesión'));
    }
  },
  
  register: async (userData) => {
    try {
      return await authService.register(userData);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al registrar usuario'));
    }
  }
};

// Servicio de cursos con manejo de errores
export const Courses = {
  getAll: async (token, filters = {}) => {
    try {
      return await courseService.getCourses(token, filters);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al obtener los cursos'));
    }
  },
  
  getDetails: async (token, courseId) => {
    try {
      return await courseService.getCourseDetails(token, courseId);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al obtener los detalles del curso'));
    }
  },
  
  enroll: async (token, courseId) => {
    try {
      return await courseService.enrollCourse(token, courseId);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al inscribirse en el curso'));
    }
  }
};

// Servicio de evaluaciones con manejo de errores
export const Evaluations = {
  get: async (token, evaluationId) => {
    try {
      return await evaluationService.getEvaluation(token, evaluationId);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al obtener la evaluación'));
    }
  },
  
  submit: async (token, evaluationId, answers) => {
    try {
      return await evaluationService.submitEvaluation(token, evaluationId, answers);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al enviar las respuestas'));
    }
  }
};

// Servicio de usuario con manejo de errores
export const User = {
  getProfile: async (token) => {
    try {
      return await userService.getUserProfile(token);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al obtener el perfil'));
    }
  },
  
  updateProfile: async (token, profileData) => {
    try {
      return await userService.updateUserProfile(token, profileData);
    } catch (error) {
      throw new Error(handleApiError(error, 'Error al actualizar el perfil'));
    }
  }
};

export default {
  Auth,
  Courses,
  Evaluations,
  User
};
