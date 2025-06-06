// Servicio API mejorado con mejor manejo de errores
import { DEMO_MODE, ENABLE_OFFLINE_MODE, API_URL, API_TIMEOUT } from '../config/api';
import { 
  DEMO_USERS, 
  DEMO_COURSES, 
  DEMO_EVALUATIONS, 
  DEMO_COMMUNICATIONS, 
  DEMO_SCHEDULE,
  DEMO_ASSIGNMENTS,
  DEMO_GRADES,
  DEMO_RESOURCES
} from '../data/demoData';

// Función para simular delay de red más realista
const simulateNetworkDelay = () => {
  return new Promise(resolve => setTimeout(resolve, 200 + Math.random() * 300));
};

// Función para crear timeout con manejo mejorado
const createTimeoutPromise = (timeout = API_TIMEOUT) => {
  return new Promise((_, reject) => 
    setTimeout(() => reject(new Error('Timeout de conexión')), timeout)
  );
};

// Manejo de errores silencioso para reducir warnings
const handleApiError = (error, fallbackData = null) => {
  // Reducir el logging de errores esperados en modo demo
  if (DEMO_MODE) {
    // En modo demo, los errores son esperados, no logear
    return fallbackData;
  }
  
  // Solo logear errores verdaderamente inesperados
  if (!error.message.includes('Timeout') && 
      !error.message.includes('NetworkError') && 
      !error.message.includes('Demo mode')) {
    console.warn('API Warning:', error.message);
  }
  
  if (ENABLE_OFFLINE_MODE) {
    return fallbackData;
  }
  
  throw error;
};

// Función helper mejorada para llamadas fetch
const fetchWithTimeout = async (url, options = {}, timeout = API_TIMEOUT) => {
  if (DEMO_MODE) {
    // En modo demo, simular falla de red silenciosamente
    throw new Error('Demo mode - usando datos locales');
  }

  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), timeout);

  try {
    const response = await fetch(url, {
      ...options,
      signal: controller.signal,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      }
    });
    
    clearTimeout(timeoutId);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    
    return await response.json();
  } catch (error) {
    clearTimeout(timeoutId);
    
    if (error.name === 'AbortError') {
      throw new Error('Conexión lenta. Inténtalo más tarde.');
    }
    
    throw error;
  }
};

// Datos demo para autenticación
const DEMO_USER_DATA = {
  'carlos.mendoza@duocuc.cl': {
    password: 'duoc2024',
    user: {
      id: 1,
      name: 'Carlos Andrés Mendoza Vargas',
      nombre: 'Carlos Andrés Mendoza Vargas',
      email: 'carlos.mendoza@duocuc.cl',
      phone: '+56 9 8765 4321',
      bio: 'Estudiante de Ingeniería en Informática - DUOC UC',
      role: 'Estudiante',
      joinDate: 'Marzo 2021',
      totalCourses: 4,
      completedCourses: 2,
      avatarUrl: null,
      carrera: 'Ingeniería en Informática',
      sede: 'Plaza Vespucio',
      año: 4,
      semestre: 8,
      rut: '19.234.567-8',
      achievements: [
        { id: 1, title: 'Primer semestre completado', icon: 'trophy', date: 'Julio 2021' },
        { id: 2, title: 'Beca de Excelencia Académica', icon: 'medal', date: 'Marzo 2022' }
      ],
      certificates: [
        { id: 1, title: 'Fundamentos de Programación', date: 'Diciembre 2022' },
        { id: 2, title: 'Base de Datos I', date: 'Julio 2023' }
      ],
      preferences: {
        notifications: true,
        darkMode: false,
        language: 'Español'
      }
    },
    token: 'demo-token-123456'
  }
};

// Servicio de Autenticación Mejorado
export const ImprovedAuthService = {
  login: async (email, password) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      const userData = DEMO_USER_DATA[email];
      if (userData && password === "demo123") {
        return {
          success: true,
          token: userData.token,
          user: userData.user
        };
      } else {
        return {
          success: false,
          message: "Credenciales incorrectas. Use carlos.sanchez@duocuc.cl / demo123"
        };
      }
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/auth/login`, {
        method: 'POST',
        body: JSON.stringify({ email, password })
      });
    } catch (error) {
      return handleApiError(error, null);
    }
  },

  logout: async () => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return { success: true };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/auth/logout`, {
        method: 'POST'
      });
    } catch (error) {
      return handleApiError(error, { success: true });
    }
  },

  register: async (userData) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        message: 'Usuario registrado exitosamente',
        user: {
          id: Date.now(),
          email: userData.email,
          name: userData.name,
          username: userData.username,
          role: 'student'
        }
      };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/auth/register`, {
        method: 'POST',
        body: JSON.stringify(userData)
      });
    } catch (error) {
      return handleApiError(error, {
        success: true,
        message: 'Usuario registrado exitosamente (modo demo)',
        user: {
          id: Date.now(),
          email: userData.email,
          name: userData.name,
          username: userData.username,
          role: 'student'
        }
      });
    }
  }
};

// Servicio de Usuario Mejorado
export const ImprovedUserService = {
  getProfile: async (token) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        data: DEMO_USERS[0]
      };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/user/profile`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (error) {
      return handleApiError(error, {
        success: true,
        data: DEMO_USERS[0]
      });
    }
  },

  updateProfile: async (token, userData) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        message: 'Perfil actualizado exitosamente',
        data: { ...DEMO_USERS[0], ...userData }
      };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/user/profile`, {
        method: 'PUT',
        headers: { 'Authorization': `Bearer ${token}` },
        body: JSON.stringify(userData)
      });
    } catch (error) {
      return handleApiError(error, {
        success: true,
        message: 'Perfil actualizado exitosamente (modo demo)',
        data: { ...DEMO_USERS[0], ...userData }
      });
    }
  }
};

// Servicio de Cursos Mejorado
export const ImprovedCourseService = {
  getAllCourses: async (token) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        data: DEMO_COURSES
      };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/courses`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (error) {
      return handleApiError(error, {
        success: true,
        data: DEMO_COURSES
      });
    }
  },

  getCourseById: async (token, courseId) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      const course = DEMO_COURSES.find(c => c.id === parseInt(courseId));
      return {
        success: true,
        data: course
      };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/courses/${courseId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (error) {
      const course = DEMO_COURSES.find(c => c.id === parseInt(courseId));
      return handleApiError(error, {
        success: true,
        data: course
      });
    }
  },

  enrollInCourse: async (token, courseId) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        message: 'Te has inscrito exitosamente en el curso'
      };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/courses/${courseId}/enroll`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (error) {
      return handleApiError(error, {
        success: true,
        message: 'Te has inscrito exitosamente en el curso'
      });
    }
  }
};

// Servicio de Horarios Mejorado
export const ImprovedScheduleService = {
  getSchedule: async (token) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        data: DEMO_SCHEDULE
      };
    }
    
    try {
      return await fetchWithTimeout(`${API_URL}/schedule`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (error) {
      return handleApiError(error, {
        success: true,
        data: DEMO_SCHEDULE
      });
    }
  }
};

// Exportación de servicios mejorados
export default {
  AuthService: ImprovedAuthService,
  UserService: ImprovedUserService,
  CourseService: ImprovedCourseService,
  ScheduleService: ImprovedScheduleService
};
