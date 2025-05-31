// Servicio API completo para funcionalidades estudiantiles
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

// Manejo de errores mejorado con logging silencioso
const handleApiError = (error, fallbackData = null) => {
  // Solo logear errores importantes, no timeouts esperados
  if (!error.message.includes('Timeout') && !error.message.includes('NetworkError')) {
    console.warn('API Warning:', error.message || error);
  }
  
  if (DEMO_MODE || ENABLE_OFFLINE_MODE) {
    return fallbackData;
  }
  
  throw error;
};

// Función helper para hacer llamadas fetch con timeout
const fetchWithTimeout = async (url, options = {}, timeout = API_TIMEOUT) => {
  if (DEMO_MODE) {
    // En modo demo, no hacer llamadas reales
    throw new Error('Demo mode - no real API calls');
  }

  const fetchPromise = fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    }
  });

  try {
    const response = await Promise.race([
      fetchPromise,
      createTimeoutPromise(timeout)
    ]);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    
    return await response.json();
  } catch (error) {
    if (error.message.includes('Timeout')) {
      throw new Error('Conexión lenta. Inténtalo más tarde.');
    }
    throw error;
  }
};

// Servicio de Autenticación
export const AuthService = {
  login: async (email, password) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      const user = DEMO_USERS.find(u => u.email === email);
      if (user && password === "demo123") {
        return {
          success: true,
          token: "demo-token-12345",
          user: user
        };
      } else {
        throw new Error('Credenciales inválidas. Usa: juan.perez@alumno.edu / demo123');
      }
    }
    
    try {
      // Aquí iría la llamada real a la API
      const response = await fetchWithTimeout(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      return response;
    } catch (error) {
      return handleApiError(error, null);
    }
  },

  logout: async () => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return { success: true };
    }
    return { success: true };
  },

  register: async (userData) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      // Simular registro exitoso en modo demo
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
      // Aquí iría la llamada real a la API
      const response = await fetchWithTimeout(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      });
      return response;
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
  },

  getUserProfile: async (token) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        data: DEMO_USERS[0] // Usuario demo
      };
    }
    
    try {
      const response = await fetchWithTimeout(`${API_URL}/user/profile`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      return response;
    } catch (error) {
      return handleApiError(error, {
        success: true,
        data: DEMO_USERS[0]
      });
    }
  }
};

// Servicio de Usuario
export const UserService = {
  getProfile: async (token) => {
    if (DEMO_MODE) {
      await simulateNetworkDelay();
      return {
        success: true,
        data: DEMO_USERS[0] // Usuario demo
      };
    }
    
    try {
      const response = await fetchWithTimeout(`${API_URL}/user/profile`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      return response;
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
      const response = await fetchWithTimeout(`${API_URL}/user/profile`, {
        method: 'PUT',
        headers: { 
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
      });
      return response;
    } catch (error) {
      return handleApiError(error, {
        success: true,
        message: 'Perfil actualizado exitosamente (modo demo)',
        data: { ...DEMO_USERS[0], ...userData }
      });
    }
  }
};

// Datos demo consolidados adaptados para Chile
const DEMO_DATA = {
  courses: [
    {
      id: 1,
      title: 'Algoritmos y Estructuras de Datos',
      code: 'CC3001',
      description: 'Fundamentos de algoritmos, estructuras de datos lineales y no lineales, análisis de complejidad.',
      instructor: 'Prof. María González López',
      duration: '1 semestre',
      level: 'Intermedio',
      rating: 4.5,
      reviews: 128,
      students: 850,
      category: 'programming',
      isEnrolled: true,
      progress: 65,
      credits: 6,
      schedule: 'Lun-Mié-Vie 10:00-11:30',
      modules: [
        {
          id: 1,
          title: 'Introducción a Algoritmos',
          lessons: [
            { id: 1, title: 'Conceptos básicos de algoritmos', duration: '45 min', completed: true },
            { id: 2, title: 'Análisis de complejidad temporal', duration: '60 min', completed: true },
            { id: 3, title: 'Notación Big O', duration: '45 min', completed: false }
          ]
        },
        {
          id: 2,
          title: 'Estructuras de Datos Lineales',
          lessons: [
            { id: 4, title: 'Arrays y listas', duration: '50 min', completed: false },
            { id: 5, title: 'Pilas y colas', duration: '45 min', completed: false }
          ]
        }
      ],
      materials: [
        { id: 1, title: 'Apuntes Algoritmos - Unidad 1', type: 'pdf', size: '2.5 MB' },
        { id: 2, title: 'Ejercicios Estructuras de Datos', type: 'pdf', size: '1.8 MB' }
      ],
      evaluations: [
        { 
          id: 1, 
          title: 'Certamen 1 - Algoritmos Básicos', 
          dueDate: '2024-06-15', 
          completed: true, 
          score: 5.8 
        },
        { 
          id: 2, 
          title: 'Tarea 1 - Implementación Lista Enlazada', 
          dueDate: '2024-06-30', 
          completed: false, 
          score: null 
        }
      ]
    },
    {
      id: 2,
      title: 'Cálculo Diferencial e Integral',
      code: 'MA1001',
      description: 'Fundamentos del cálculo diferencial e integral, límites, derivadas y aplicaciones.',
      instructor: 'Prof. Carlos Ruiz Mendoza',
      duration: '1 semestre',
      level: 'Básico',
      rating: 4.2,
      reviews: 215,
      students: 1200,
      category: 'mathematics',
      isEnrolled: true,
      progress: 45,
      credits: 6,
      schedule: 'Mar-Jue 08:00-09:30',
      modules: [],
      materials: [],
      evaluations: []
    },
    {
      id: 3,
      title: 'Química General',
      code: 'QU1001',
      description: 'Principios fundamentales de la química: estructura atómica, enlaces químicos y reacciones.',
      instructor: 'Dra. Ana Torres Silva',
      duration: '1 semestre',
      level: 'Básico',
      rating: 4.0,
      reviews: 89,
      students: 950,
      category: 'science',
      isEnrolled: false,
      progress: 0,
      credits: 6,
      schedule: 'Lun-Mié 14:00-15:30',
      modules: [],
      materials: [],
      evaluations: []
    },
    {
      id: 4,
      title: 'Inglés Técnico I',
      code: 'ID0001',
      description: 'Desarrollo de habilidades comunicativas en inglés aplicado a contextos técnicos y académicos.',
      instructor: 'Prof. Jennifer Martinez',
      duration: '1 semestre',
      level: 'Básico',
      rating: 4.4,
      reviews: 156,
      students: 680,
      category: 'languages',
      isEnrolled: true,
      progress: 80,
      credits: 3,
      schedule: 'Vie 10:00-12:30',
      modules: [],
      materials: [],
      evaluations: []
    },
    {
      id: 5,
      title: 'Base de Datos',
      code: 'CC3002',
      description: 'Diseño, implementación y administración de bases de datos relacionales y NoSQL.',
      instructor: 'Prof. Roberto Silva Castro',
      duration: '1 semestre',
      level: 'Avanzado',
      rating: 4.6,
      reviews: 92,
      students: 420,
      category: 'programming',
      isEnrolled: false,
      progress: 0,
      credits: 6,
      schedule: 'Mar-Jue 15:00-16:30',
      modules: [],
      materials: [],
      evaluations: []
    }
  ],
  
  student: {
    id: 1,
    name: 'Juan Pérez García',
    email: 'juan.perez@ug.uchile.cl',
    phone: '+56 9 8765 4321',
    enrollmentDate: '2021-03-01',
    studentId: '21.123.456-7',
    career: 'Ingeniería Civil en Computación',
    year: 4,
    semester: 7,
    status: 'Regular',
    ppa: 5.5, // Promedio Ponderado Acumulado
    completedCredits: 142,
    totalCredits: 240,
    university: 'Universidad de Chile',
    faculty: 'Facultad de Ciencias Físicas y Matemáticas'
  },

  grades: {
    grades: [
      {
        id: 1,
        courseCode: 'CC3001',
        courseName: 'Algoritmos y Estructuras de Datos',
        grade: 5.8,
        credits: 6,
        semester: '2024-1',
        current: true,
        status: 'Aprobado'
      },
      {
        id: 2,
        courseCode: 'MA1001',
        courseName: 'Cálculo Diferencial e Integral',
        grade: 6.2,
        credits: 6,
        semester: '2024-1',
        current: true,
        status: 'Aprobado'
      },
      {
        id: 3,
        courseCode: 'ID0001',
        courseName: 'Inglés Técnico I',
        grade: 6.5,
        credits: 3,
        semester: '2023-2',
        current: false,
        status: 'Aprobado'
      },
      {
        id: 4,
        courseCode: 'FI1000',
        courseName: 'Introducción a la Física',
        grade: 4.8,
        credits: 6,
        semester: '2023-2',
        current: false,
        status: 'Aprobado'
      }
    ],
    summary: {
      ppa: 5.5,
      totalCredits: 142,
      completedCourses: 28,
      currentSemester: '7mo',
      approvedCredits: 135,
      failedCredits: 7
    }
  },

  announcements: [
    {
      id: 1,
      title: 'Inscripción de Asignaturas 2024-2',
      content: 'Período de inscripción: 1 al 15 de julio de 2024',
      date: '2024-06-25',
      type: 'academic',
      urgent: true
    },
    {
      id: 2,
      title: 'Mantención Sistema U-Campus',
      content: 'El sistema estará en mantención el sábado 29 de junio de 14:00 a 18:00 hrs',
      date: '2024-06-27',
      type: 'system',
      urgent: false
    },
    {
      id: 3,
      title: 'Postulación Becas de Alimentación',
      content: 'Abierta postulación para becas JUNAEB período 2024-2',
      date: '2024-06-20',
      type: 'benefits',
      urgent: false
    }
  ]
};

// Funciones helper únicas - eliminando duplicados
const createResponse = (success, data, message) => ({ success, data, message });

const handleError = (error, fallbackData, fallbackMessage) => {
  console.error('API Error:', error);
  return createResponse(false, fallbackData, fallbackMessage);
};

// Servicio unificado para todas las operaciones
export const StudentApiService = {
  async getCourses(token) {
    console.log('📚 Cargando cursos...');
    
    if (DEMO_MODE) {
      // Simular carga
      await new Promise(resolve => setTimeout(resolve, 500));
      console.log('✅ Cursos cargados exitosamente (modo demo)');
      return createResponse(true, DEMO_DATA.courses, 'Cursos cargados desde datos demo');
    }

    try {
      const response = await fetch(`${API_URL}/courses`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      return createResponse(true, data, 'Cursos cargados exitosamente');
    } catch (error) {
      console.error('❌ Error al cargar cursos:', error.message);
      // En caso de error, devolver datos demo
      return createResponse(true, DEMO_DATA.courses, 'Datos demo por error de conexión');
    }
  },

  async getCourseById(token, courseId) {
    console.log(`📖 Cargando detalles del curso ${courseId}...`);
    
    if (DEMO_MODE) {
      await new Promise(resolve => setTimeout(resolve, 300));
      const course = DEMO_DATA.courses.find(c => c.id == courseId);
      if (course) {
        console.log('✅ Detalles del curso cargados (modo demo)');
        return createResponse(true, course, 'Detalles del curso cargados');
      } else {
        return createResponse(false, null, 'Curso no encontrado');
      }
    }

    try {
      const response = await fetch(`${API_URL}/courses/${courseId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      return createResponse(true, data, 'Detalles del curso cargados');
    } catch (error) {
      console.error('❌ Error al cargar detalles del curso:', error.message);
      // Devolver datos demo del curso
      const course = DEMO_DATA.courses.find(c => c.id == courseId);
      if (course) {
        return createResponse(true, course, 'Datos demo por error de conexión');
      }
      return createResponse(false, null, 'Error al cargar el curso');
    }
  },

  async enrollInCourse(token, courseId) {
    if (DEMO_MODE) {
      const courseIndex = DEMO_DATA.courses.findIndex(c => c.id === parseInt(courseId));
      if (courseIndex !== -1) {
        DEMO_DATA.courses[courseIndex].isEnrolled = true;
        DEMO_DATA.courses[courseIndex].progress = 0;
      }
      return createResponse(true, null, 'Te has inscrito al curso exitosamente');
    }

    try {
      const response = await fetch(`${API_URL}/courses/${courseId}/enroll`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      const data = await response.json();
      return createResponse(response.ok, null, data.message || 'Inscripción completada');
    } catch (error) {
      return handleError(error, null, 'Error de conexión. Inténtalo más tarde.');
    }
  },

  async getStudentProfile(token) {
    if (DEMO_MODE) {
      return createResponse(true, DEMO_DATA.student, 'Perfil cargado desde datos demo');
    }

    try {
      const response = await fetch(`${API_URL}/student/profile`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      const data = await response.json();
      return createResponse(response.ok, data.student, data.message);
    } catch (error) {
      return handleError(error, DEMO_DATA.student, 'Error de conexión, mostrando datos locales');
    }
  },

  async getGrades(token) {
    console.log('📊 Cargando calificaciones...');
    
    if (DEMO_MODE) {
      await new Promise(resolve => setTimeout(resolve, 400));
      console.log('✅ Calificaciones cargadas (modo demo)');
      return DEMO_DATA.grades;
    }

    try {
      const response = await fetch(`${API_URL}/grades`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('❌ Error al cargar calificaciones:', error.message);
      return DEMO_DATA.grades;
    }
  },

  async getEvaluation(token, evaluationId) {
    if (DEMO_MODE) {
      const evaluation = DEMO_DATA.evaluations.find(e => e.id === parseInt(evaluationId));
      return createResponse(!!evaluation, evaluation, evaluation ? 'Evaluación encontrada' : 'Evaluación no encontrada');
    }

    try {
      const response = await fetch(`${API_URL}/evaluations/${evaluationId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      const data = await response.json();
      return createResponse(response.ok, data.evaluation, data.message);
    } catch (error) {
      return handleError(error, null, 'Error de conexión');
    }
  },

  async submitEvaluation(token, evaluationId, answers) {
    if (DEMO_MODE) {
      const evaluation = DEMO_DATA.evaluations.find(e => e.id === parseInt(evaluationId));
      if (!evaluation) {
        return createResponse(false, null, 'Evaluación no encontrada');
      }

      let correctAnswers = 0;
      const results = evaluation.questions.map((question) => {
        const userAnswer = answers[question.id];
        const isCorrect = userAnswer === question.correctAnswer;
        if (isCorrect) correctAnswers++;

        return {
          questionId: question.id,
          userAnswer,
          correctAnswer: question.correctAnswer,
          isCorrect,
          points: isCorrect ? question.points : 0
        };
      });

      const totalPoints = results.reduce((sum, result) => sum + result.points, 0);
      const percentage = Math.round((totalPoints / (evaluation.questions.length * 10)) * 100);

      return createResponse(true, {
        score: percentage,
        totalQuestions: evaluation.questions.length,
        correctAnswers,
        passed: percentage >= evaluation.passingScore,
        results,
        evaluationTitle: evaluation.title
      }, 'Evaluación enviada correctamente');
    }

    try {
      const response = await fetch(`${API_URL}/evaluations/${evaluationId}/submit`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ answers })
      });
      const data = await response.json();
      return createResponse(response.ok, data.result, data.message || 'Evaluación enviada');
    } catch (error) {
      return handleError(error, null, 'Error de conexión. Inténtalo más tarde.');
    }
  }
};

// Exports para compatibilidad con código existente
export const CourseService = {
  getAllCourses: StudentApiService.getCourses,
  getCourseById: StudentApiService.getCourseById,
  enrollInCourse: StudentApiService.enrollInCourse
};

export const StudentService = {
  getProfile: StudentApiService.getStudentProfile
};

export const EvaluationService = {
  getEvaluation: StudentApiService.getEvaluation,
  submitEvaluation: StudentApiService.submitEvaluation
};

export const studentApiService = {
  getGrades: StudentApiService.getGrades
};
