// Constantes para la configuración de la API
const API_BASE_URL = 'http://192.168.1.100:8080'; // Cambiar según la configuración de tu red local
const API_TIMEOUT = 15000; // 15 segundos

// Función para manejar respuestas de la API
const handleResponse = async (response) => {
  if (!response.ok) {
    // Intentar obtener el mensaje de error del backend
    let errorMessage;
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || `Error ${response.status}`;
    } catch (e) {
      errorMessage = `Error ${response.status}: ${response.statusText}`;
    }
    
    throw new Error(errorMessage);
  }
  
  // Para respuestas 204 No Content
  if (response.status === 204) {
    return null;
  }
  
  // Para otras respuestas exitosas
  return response.json();
};

// Función para realizar solicitudes a la API
const fetchWithTimeout = (url, options = {}) => {
  const controller = new AbortController();
  const { signal } = controller;
  
  const timeout = setTimeout(() => controller.abort(), API_TIMEOUT);
  
  return fetch(url, {
    ...options,
    signal,
  })
    .then(response => {
      clearTimeout(timeout);
      return response;
    })
    .catch(error => {
      clearTimeout(timeout);
      if (error.name === 'AbortError') {
        throw new Error('La solicitud ha excedido el tiempo de espera');
      }
      throw error;
    });
};

// Servicio de autenticación
export const authService = {
  // Iniciar sesión
  login: async (username, password) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error('Error en login:', error);
      throw error;
    }
  },
  
  // Registrar usuario
  register: async (userData) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error('Error en register:', error);
      throw error;
    }
  },
};

// Servicio de cursos
export const courseService = {
  // Obtener lista de cursos
  getCourses: async (token, params = {}) => {
    const queryParams = new URLSearchParams(params).toString();
    const url = `${API_BASE_URL}/api/courses${queryParams ? `?${queryParams}` : ''}`;
    
    try {
      const response = await fetchWithTimeout(url, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error('Error en getCourses:', error);
      throw error;
    }
  },
  
  // Obtener detalles de un curso
  getCourseDetails: async (token, courseId) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/api/courses/${courseId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error(`Error en getCourseDetails para curso ${courseId}:`, error);
      throw error;
    }
  },
  
  // Inscribirse en un curso
  enrollCourse: async (token, courseId) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/api/courses/${courseId}/enroll`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error(`Error en enrollCourse para curso ${courseId}:`, error);
      throw error;
    }
  },
};

// Servicio de evaluaciones
export const evaluationService = {
  // Obtener evaluación
  getEvaluation: async (token, evaluationId) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/api/evaluations/${evaluationId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error(`Error en getEvaluation para evaluación ${evaluationId}:`, error);
      throw error;
    }
  },
  
  // Enviar respuestas de evaluación
  submitEvaluation: async (token, evaluationId, answers) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/api/evaluations/${evaluationId}/submit`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ answers }),
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error(`Error en submitEvaluation para evaluación ${evaluationId}:`, error);
      throw error;
    }
  },
};

// Servicio de perfil de usuario
export const userService = {
  // Obtener perfil del usuario
  getUserProfile: async (token) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/api/users/profile`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error('Error en getUserProfile:', error);
      throw error;
    }
  },
  
  // Actualizar perfil de usuario
  updateUserProfile: async (token, profileData) => {
    try {
      const response = await fetchWithTimeout(`${API_BASE_URL}/api/users/profile`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(profileData),
      });
      
      return handleResponse(response);
    } catch (error) {
      console.error('Error en updateUserProfile:', error);
      throw error;
    }
  },
};

// Exportación de todos los servicios
export default {
  auth: authService,
  courses: courseService,
  evaluations: evaluationService,
  users: userService,
};
