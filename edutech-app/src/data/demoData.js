// Datos de demostración para la aplicación EduTech - DUOC UC
export const DEMO_USERS = [
  {
    id: 1,
    username: 'carlos.mendoza@duocuc.cl',
    email: 'carlos.mendoza@duocuc.cl',
    password: 'duoc2024',
    name: 'Carlos Andrés Mendoza Vargas',
    rut: '19.234.567-8',
    role: 'student',
    sede: 'Plaza Vespucio',
    carrera: 'Ingeniería en Informática',
    año: 4,
    semestre: 8,
    modalidad: 'Presencial',
    jornada: 'Diurna',
    cohorte: '2021',
    estadoAcademico: 'Regular',
    promedio: 6.2,
    creditosAprobados: 198,
    creditosTotales: 240,
    telefono: '+56 9 8765 4321',
    fechaNacimiento: '1999-03-15',
    fechaIngreso: '2021-03-01',
    direccion: 'Av. Vicuña Mackenna 3939, La Florida',
    region: 'Región Metropolitana',
    foto: null,
    beneficios: [
      {
        nombre: 'Beca de Excelencia Académica DUOC UC',
        estado: 'ACTIVA',
        porcentaje: 50,
        descripcion: 'Beca por rendimiento académico destacado'
      },
      {
        nombre: 'Gratuidad',
        estado: 'ACTIVA',
        porcentaje: 100,
        descripcion: 'Beneficio estatal de gratuidad'
      }
    ],
    historialAcademico: {
      asignaturasAprobadas: 42,
      asignaturasReprobadas: 2,
      asignaturasPendientes: 6
    }
  }
];

// Datos de cursos DUOC UC
export const DEMO_COURSES = [
  {
    id: 1,
    title: 'Programación Orientada a Objetos',
    code: 'INFO1166',
    description: 'Fundamentos de programación orientada a objetos usando Java',
    instructor: 'Prof. María González Silva',
    credits: 6,
    semester: '2024-2',
    schedule: 'Lun-Mié-Vie 08:00-09:30',
    classroom: 'Lab. Informática A',
    isEnrolled: true,
    progress: 75,
    rating: 4.5,
    students: 32,
    level: 'Intermedio',
    category: 'programming',
    status: 'active',
    modules: [
      { id: 1, name: 'Conceptos básicos de POO', completed: true },
      { id: 2, name: 'Clases y objetos en Java', completed: true },
      { id: 3, name: 'Herencia y polimorfismo', completed: false },
      { id: 4, name: 'Proyecto final', completed: false }
    ]
  },
  {
    id: 2,
    title: 'Base de Datos II',
    code: 'INFO1277',
    description: 'Diseño y administración avanzada de bases de datos',
    instructor: 'Prof. Roberto Silva Herrera',
    credits: 5,
    semester: '2024-2',
    schedule: 'Mar-Jue 10:00-11:30',
    classroom: 'Aula 205',
    isEnrolled: true,
    progress: 68,
    rating: 4.3,
    students: 28,
    level: 'Avanzado',
    category: 'database',
    status: 'active',
    modules: [
      { id: 1, name: 'Normalización avanzada', completed: true },
      { id: 2, name: 'Procedimientos almacenados', completed: true },
      { id: 3, name: 'Optimización de consultas', completed: false },
      { id: 4, name: 'Administración de BD', completed: false }
    ]
  },
  {
    id: 3,
    title: 'Inglés Técnico II',
    code: 'INGL2201',
    description: 'Inglés aplicado al área técnica e informática',
    instructor: 'Prof. Ana Torres Mitchell',
    credits: 3,
    semester: '2024-2',
    schedule: 'Vie 14:00-16:30',
    classroom: 'Aula 301',
    isEnrolled: true,
    progress: 82,
    rating: 4.7,
    students: 35,
    level: 'Intermedio',
    category: 'language',
    status: 'active',
    modules: [
      { id: 1, name: 'Technical vocabulary', completed: true },
      { id: 2, name: 'Documentation writing', completed: true },
      { id: 3, name: 'Presentations skills', completed: true },
      { id: 4, name: 'Final project', completed: false }
    ]
  }
];

// Evaluaciones y tareas
export const DEMO_EVALUATIONS = [
  {
    id: 1,
    title: 'Examen Parcial - POO',
    courseId: 1,
    courseName: 'Programación Orientada a Objetos',
    type: 'exam',
    date: '2024-06-15',
    time: '09:00',
    duration: '120 minutos',
    location: 'Lab. Informática A',
    weight: 30,
    status: 'scheduled',
    description: 'Examen sobre conceptos fundamentales de POO y Java'
  },
  {
    id: 2,
    title: 'Proyecto Base de Datos',
    courseId: 2,
    courseName: 'Base de Datos II',
    type: 'project',
    date: '2024-06-20',
    time: '23:59',
    duration: 'Entrega final',
    location: 'Plataforma virtual',
    weight: 40,
    status: 'in_progress',
    description: 'Diseño e implementación de sistema de BD para empresa'
  }
];

// Comunicaciones y anuncios
export const DEMO_COMMUNICATIONS = [
  {
    id: 1,
    title: 'Proceso de Matrícula 2025',
    content: 'El proceso de matrícula para el año académico 2025 estará disponible desde el 15 de diciembre de 2024 hasta el 15 de enero de 2025.',
    date: '2024-05-25',
    type: 'announcement',
    priority: 'high',
    department: 'Secretaría Académica',
    read: false
  },
  {
    id: 2,
    title: 'Evaluación Docente 2024-2',
    content: 'Ya está disponible la evaluación docente del segundo semestre 2024. Tu opinión es importante para mejorar la calidad académica.',
    date: '2024-05-23',
    type: 'notification',
    priority: 'medium',
    department: 'Dirección Académica',
    read: true
  }
];

// Horario semanal
export const DEMO_SCHEDULE = {
  'Lunes': [
    {
      id: 1,
      subject: 'Programación Orientada a Objetos',
      code: 'INFO1166',
      time: '08:00 - 09:30',
      professor: 'Prof. María González',
      classroom: 'Lab. Informática A',
      building: 'Edificio Tecnológico',
      type: 'Laboratorio'
    },
    {
      id: 2,
      subject: 'Arquitectura de Software',
      code: 'INFO1288',
      time: '10:00 - 11:30',
      professor: 'Prof. Luis Vargas',
      classroom: 'Aula 102',
      building: 'Edificio Tecnológico',
      type: 'Teórica'
    }
  ],
  'Martes': [
    {
      id: 3,
      subject: 'Base de Datos II',
      code: 'INFO1277',
      time: '10:00 - 11:30',
      professor: 'Prof. Roberto Silva',
      classroom: 'Aula 205',
      building: 'Edificio Central',
      type: 'Teórica'
    }
  ],
  'Miércoles': [
    {
      id: 4,
      subject: 'Programación Orientada a Objetos',
      code: 'INFO1166',
      time: '08:00 - 09:30',
      professor: 'Prof. María González',
      classroom: 'Lab. Informática A',
      building: 'Edificio Tecnológico',
      type: 'Laboratorio'
    }
  ],
  'Jueves': [
    {
      id: 5,
      subject: 'Base de Datos II',
      code: 'INFO1277',
      time: '10:00 - 11:30',
      professor: 'Prof. Roberto Silva',
      classroom: 'Lab. Informática B',
      building: 'Edificio Tecnológico',
      type: 'Laboratorio'
    }
  ],
  'Viernes': [
    {
      id: 6,
      subject: 'Inglés Técnico II',
      code: 'INGL2201',
      time: '14:00 - 16:30',
      professor: 'Prof. Ana Torres',
      classroom: 'Aula 301',
      building: 'Edificio de Idiomas',
      type: 'Práctica'
    }
  ]
};

// Tareas y asignaciones
export const DEMO_ASSIGNMENTS = [
  {
    id: 1,
    titulo: 'Implementación de Sistema POO',
    descripcion: 'Desarrollar un sistema completo usando los principios de POO aprendidos en clase',
    cursoId: 1,
    cursoNombre: 'Programación Orientada a Objetos',
    fechaAsignacion: '2024-05-10',
    fechaEntrega: '2024-06-10',
    estado: 'EN_PROGRESO',
    calificacion: null,
    comentarios: null,
    peso: 35
  },
  {
    id: 2,
    titulo: 'Ensayo Técnico en Inglés',
    descripcion: 'Redactar un ensayo técnico sobre tendencias en tecnología',
    cursoId: 3,
    cursoNombre: 'Inglés Técnico II',
    fechaAsignacion: '2024-05-15',
    fechaEntrega: '2024-05-30',
    estado: 'ENTREGADO',
    calificacion: 6.8,
    comentarios: 'Excelente uso del vocabulario técnico. Mejorar estructura de párrafos.',
    peso: 25
  }
];

// Notas y calificaciones
export const DEMO_GRADES = {
  summary: {
    gpa: 6.2,
    totalCredits: 198,
    completedCourses: 42,
    currentSemester: '8° Semestre'
  },
  grades: [
    {
      id: 1,
      courseCode: 'INFO1166',
      courseName: 'Programación Orientada a Objetos',
      grade: 6.8,
      credits: 6,
      semester: '2024-2',
      current: true
    },
    {
      id: 2,
      courseCode: 'INFO1277',
      courseName: 'Base de Datos II',
      grade: 6.2,
      credits: 5,
      semester: '2024-2',
      current: true
    },
    {
      id: 3,
      courseCode: 'INGL2201',
      courseName: 'Inglés Técnico II',
      grade: 6.9,
      credits: 3,
      semester: '2024-2',
      current: true
    },
    {
      id: 4,
      courseCode: 'INFO1155',
      courseName: 'Programación de Aplicaciones',
      grade: 5.8,
      credits: 6,
      semester: '2024-1',
      current: false
    }
  ]
};

// Recursos académicos
export const DEMO_RESOURCES = [
  {
    id: 1,
    title: 'Manual de Java - Programación POO',
    course: 'Programación Orientada a Objetos',
    type: 'document',
    size: 2048000,
    createdAt: '2024-05-15',
    downloads: 156,
    description: 'Guía completa de programación en Java con ejemplos prácticos'
  },
  {
    id: 2,
    title: 'Video: Normalización de Bases de Datos',
    course: 'Base de Datos II',
    type: 'video',
    createdAt: '2024-05-10',
    downloads: 89,
    description: 'Tutorial sobre normalización hasta 3FN con casos prácticos'
  }
];

// Estado financiero
export const DEMO_FINANCIAL_STATUS = {
  estadoCuenta: {
    saldoTotal: -125000,
    proximoVencimiento: {
      fecha: '2024-06-15',
      monto: 450000,
      concepto: 'Arancel Junio 2024'
    },
    cuotasPendientes: 2,
    ultimoPago: {
      fecha: '2024-05-15',
      monto: 450000,
      metodo: 'Transferencia bancaria'
    }
  },
  historialPagos: [
    {
      id: 1,
      fecha: '2024-05-15',
      concepto: 'Arancel Mayo 2024',
      monto: 450000,
      estado: 'Pagado',
      metodoPago: 'Transferencia bancaria'
    },
    {
      id: 2,
      fecha: '2024-04-15',
      concepto: 'Arancel Abril 2024',
      monto: 450000,
      estado: 'Pagado',
      metodoPago: 'Tarjeta de crédito'
    }
  ]
};

// Servicios estudiantiles
export const DEMO_STUDENT_SERVICES = [
  {
    id: 1,
    nombre: 'Certificado de Alumno Regular',
    descripcion: 'Certificado que acredita la condición de estudiante activo',
    categoria: 'CERTIFICADOS',
    precio: 0,
    tiempoEntrega: '24 horas',
    formato: ['DIGITAL', 'FÍSICO'],
    requisitos: ['Estar matriculado', 'Sin deudas pendientes'],
    estado: 'DISPONIBLE'
  },
  {
    id: 2,
    nombre: 'Concentración de Notas',
    descripcion: 'Documento oficial con historial académico completo',
    categoria: 'CERTIFICADOS',
    precio: 3500,
    tiempoEntrega: '3-5 días hábiles',
    formato: ['DIGITAL', 'FÍSICO'],
    requisitos: ['Solicitud formal', 'Pago de arancel'],
    estado: 'DISPONIBLE'
  },
  {
    id: 3,
    nombre: 'Reposición de Credencial',
    descripcion: 'Nueva credencial universitaria por pérdida o daño',
    categoria: 'CREDENCIALES',
    precio: 8500,
    tiempoEntrega: '5-7 días hábiles',
    formato: ['FÍSICO'],
    requisitos: ['Denuncia de pérdida', 'Foto tamaño carnet', 'Pago de arancel'],
    estado: 'DISPONIBLE'
  },
  {
    id: 4,
    nombre: 'Duplicado de Diploma',
    descripcion: 'Copia oficial del título profesional',
    categoria: 'CERTIFICADOS',
    precio: 15000,
    tiempoEntrega: '10-15 días hábiles',
    formato: ['FÍSICO'],
    requisitos: ['Solicitud notarial', 'Motivo justificado', 'Pago de arancel'],
    estado: 'DISPONIBLE'
  },
  {
    id: 5,
    nombre: 'Cambio de Carrera',
    descripcion: 'Trámite para cambio de programa académico',
    categoria: 'TRAMITES',
    precio: 25000,
    tiempoEntrega: '15-20 días hábiles',
    formato: ['PRESENCIAL'],
    requisitos: ['Cumplir requisitos académicos', 'Entrevista', 'Documentación completa'],
    estado: 'DISPONIBLE'
  },
  {
    id: 6,
    nombre: 'Retiro Temporal',
    descripcion: 'Suspensión temporal de estudios',
    categoria: 'TRAMITES',
    precio: 0,
    tiempoEntrega: '5-10 días hábiles',
    formato: ['PRESENCIAL', 'DIGITAL'],
    requisitos: ['Solicitud fundamentada', 'Estar al día en pagos'],
    estado: 'DISPONIBLE'
  },
  {
    id: 7,
    nombre: 'Homologación de Ramos',
    descripcion: 'Reconocimiento de asignaturas de otras instituciones',
    categoria: 'TRAMITES',
    precio: 12000,
    tiempoEntrega: '20-30 días hábiles',
    formato: ['PRESENCIAL'],
    requisitos: ['Programas de asignaturas', 'Certificado de notas', 'Evaluación académica'],
    estado: 'DISPONIBLE'
  }
];
