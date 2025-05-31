// Datos de demostración para la presentación

// Estudiante demo actualizado
export const DEMO_USERS = [
  {
    id: 1,
    nombre: "Juan Pérez",
    email: "juan.perez@alumno.edu",
    rol: "ESTUDIANTE",
    telefono: "+34 600 123 456",
    fechaNacimiento: "2000-05-15",
    carrera: "Ingeniería en Sistemas",
    semestre: 6,
    promedio: 8.5,
    creditos: 85,
    foto: "https://via.placeholder.com/150"
  },
  {
    id: 2,
    nombre: "María García",
    email: "maria.garcia@profesor.edu", 
    rol: "PROFESOR",
    telefono: "+34 600 789 012",
    departamento: "Ciencias de la Computación",
    especializacion: "Desarrollo de Software"
  }
];

// Cursos expandidos con más información
export const DEMO_COURSES = [
  {
    id: 1,
    nombre: "Programación en Java",
    descripcion: "Curso completo de programación orientada a objetos con Java. Aprende desde conceptos básicos hasta patrones de diseño avanzados.",
    duracion: 120,
    modalidad: "PRESENCIAL",
    fechaInicio: "2025-06-01",
    fechaFin: "2025-08-30",
    profesorId: 2,
    profesorNombre: "María García",
    estado: "ACTIVO",
    participantes: 25,
    maxParticipantes: 30,
    progreso: 65,
    calificacion: 8.5,
    creditos: 4,
    horario: "Lunes y Miércoles 9:00-11:00",
    aula: "Lab. Computación 301",
    modulos: [
      { id: 1, nombre: "Fundamentos de Java", completado: true, duracion: "2 semanas" },
      { id: 2, nombre: "POO en Java", completado: true, duracion: "3 semanas" },
      { id: 3, nombre: "Estructuras de Datos", completado: false, duracion: "3 semanas" },
      { id: 4, nombre: "Interfaces y Herencia", completado: false, duracion: "2 semanas" }
    ]
  },
  {
    id: 2,
    nombre: "Desarrollo Web Frontend",
    descripcion: "React, JavaScript ES6+ y desarrollo de interfaces modernas. Crea aplicaciones web interactivas y responsivas.",
    duracion: 80,
    modalidad: "VIRTUAL",
    fechaInicio: "2025-06-15",
    fechaFin: "2025-08-15",
    profesorId: 2,
    profesorNombre: "María García",
    estado: "ACTIVO",
    participantes: 18,
    maxParticipantes: 20,
    progreso: 40,
    calificacion: 9.0,
    creditos: 3,
    horario: "Martes y Jueves 14:00-16:00",
    aula: "Virtual - Zoom",
    modulos: [
      { id: 1, nombre: "JavaScript ES6+", completado: true, duracion: "2 semanas" },
      { id: 2, nombre: "React Básico", completado: true, duracion: "3 semanas" },
      { id: 3, nombre: "React Hooks", completado: false, duracion: "2 semanas" },
      { id: 4, nombre: "Proyecto Final", completado: false, duracion: "1 semana" }
    ]
  },
  {
    id: 3,
    nombre: "Base de Datos MySQL",
    descripcion: "Diseño y administración de bases de datos relacionales. Aprende SQL desde lo básico hasta consultas avanzadas.",
    duracion: 60,
    modalidad: "HIBRIDA",
    fechaInicio: "2025-07-01",
    fechaFin: "2025-08-15",
    profesorId: 2,
    profesorNombre: "María García",
    estado: "ACTIVO",
    participantes: 22,
    maxParticipantes: 25,
    progreso: 20,
    calificacion: 7.8,
    creditos: 3,
    horario: "Viernes 10:00-14:00",
    aula: "Lab. Redes 205",
    modulos: [
      { id: 1, nombre: "Fundamentos de BD", completado: true, duracion: "1 semana" },
      { id: 2, nombre: "SQL Básico", completado: false, duracion: "2 semanas" },
      { id: 3, nombre: "SQL Avanzado", completado: false, duracion: "2 semanas" },
      { id: 4, nombre: "Optimización", completado: false, duracion: "1 semana" }
    ]
  }
];

// Evaluaciones con resultados detallados
export const DEMO_EVALUATIONS = [
  {
    id: 1,
    cursoId: 1,
    cursoNombre: "Programación en Java",
    titulo: "Examen Parcial - POO",
    descripcion: "Evaluación de conceptos de programación orientada a objetos",
    fechaCreacion: "2025-05-20",
    fechaLimite: "2025-06-01",
    duracion: 90,
    preguntas: 20,
    puntajeTotal: 100,
    estado: "COMPLETADO",
    puntajeObtenido: 85,
    porcentajeAprobacion: 70,
    tiempoUtilizado: 75,
    fechaCompletado: "2025-05-28",
    intentos: 1,
    maxIntentos: 2
  },
  {
    id: 2,
    cursoId: 2,
    cursoNombre: "Desarrollo Web Frontend",
    titulo: "Quiz - JavaScript ES6",
    descripcion: "Evaluación rápida de sintaxis moderna de JavaScript",
    fechaCreacion: "2025-05-25",
    fechaLimite: "2025-06-05",
    duracion: 30,
    preguntas: 10,
    puntajeTotal: 50,
    estado: "COMPLETADO",
    puntajeObtenido: 45,
    porcentajeAprobacion: 60,
    tiempoUtilizado: 28,
    fechaCompletado: "2025-05-30",
    intentos: 1,
    maxIntentos: 3
  },
  {
    id: 3,
    cursoId: 1,
    cursoNombre: "Programación en Java",
    titulo: "Examen Final",
    descripcion: "Evaluación integral del curso de Java",
    fechaCreacion: "2025-05-30",
    fechaLimite: "2025-06-15",
    duracion: 120,
    preguntas: 30,
    puntajeTotal: 100,
    estado: "PENDIENTE",
    porcentajeAprobacion: 70,
    intentos: 0,
    maxIntentos: 1
  }
];

// Tareas y asignaciones
export const DEMO_ASSIGNMENTS = [
  {
    id: 1,
    cursoId: 1,
    cursoNombre: "Programación en Java",
    titulo: "Proyecto: Sistema de Inventario",
    descripcion: "Desarrollar un sistema básico de inventario usando conceptos POO",
    fechaAsignacion: "2025-05-20",
    fechaEntrega: "2025-06-10",
    estado: "EN_PROGRESO",
    calificacion: null,
    comentarios: null,
    archivo: null
  },
  {
    id: 2,
    cursoId: 2,
    cursoNombre: "Desarrollo Web Frontend",
    titulo: "Práctica: Componentes React",
    descripcion: "Crear una aplicación de lista de tareas con React Hooks",
    fechaAsignacion: "2025-05-25",
    fechaEntrega: "2025-06-05",
    estado: "ENTREGADO",
    calificacion: 9.2,
    comentarios: "Excelente implementación de hooks. Código limpio y bien documentado.",
    archivo: "lista-tareas-react.zip"
  }
];

// Horario académico
export const DEMO_SCHEDULE = [
  {
    dia: "Lunes",
    clases: [
      {
        hora: "09:00-11:00",
        curso: "Programación en Java",
        profesor: "María García",
        aula: "Lab. Computación 301",
        tipo: "TEORIA"
      },
      {
        hora: "14:00-16:00",
        curso: "Matemáticas Discretas", 
        profesor: "Dr. López",
        aula: "Aula 204",
        tipo: "TEORIA"
      }
    ]
  },
  {
    dia: "Martes",
    clases: [
      {
        hora: "14:00-16:00",
        curso: "Desarrollo Web Frontend",
        profesor: "María García", 
        aula: "Virtual - Zoom",
        tipo: "PRACTICA"
      }
    ]
  },
  {
    dia: "Miércoles",
    clases: [
      {
        hora: "09:00-11:00",
        curso: "Programación en Java",
        profesor: "María García",
        aula: "Lab. Computación 301", 
        tipo: "PRACTICA"
      },
      {
        hora: "16:00-18:00",
        curso: "Inglés Técnico",
        profesor: "Prof. Smith",
        aula: "Aula 105",
        tipo: "TEORIA"
      }
    ]
  },
  {
    dia: "Jueves", 
    clases: [
      {
        hora: "14:00-16:00",
        curso: "Desarrollo Web Frontend",
        profesor: "María García",
        aula: "Virtual - Zoom",
        tipo: "TEORIA"
      }
    ]
  },
  {
    dia: "Viernes",
    clases: [
      {
        hora: "10:00-14:00",
        curso: "Base de Datos MySQL",
        profesor: "María García",
        aula: "Lab. Redes 205",
        tipo: "PRACTICA"
      }
    ]
  }
];

// Comunicaciones y anuncios
export const DEMO_COMMUNICATIONS = [
  {
    id: 1,
    tipo: "ANUNCIO",
    titulo: "Suspensión de clases - Día feriado",
    contenido: "Les informamos que el próximo lunes 2 de junio no habrá clases por ser día feriado nacional.",
    fechaPublicacion: "2025-05-30",
    remitente: "Dirección Académica",
    cursoId: null, // Anuncio general
    leido: false,
    importante: true
  },
  {
    id: 2,
    tipo: "MENSAJE_CURSO",
    titulo: "Material adicional disponible",
    contenido: "He subido ejemplos adicionales de código Java al aula virtual. Revisen la sección de recursos.",
    fechaPublicacion: "2025-05-29",
    remitente: "María García",
    cursoId: 1,
    leido: true,
    importante: false
  },
  {
    id: 3,
    tipo: "RECORDATORIO",
    titulo: "Examen Final próximo",
    contenido: "Recuerden que el examen final de Java será el 15 de junio. Repasen los temas de herencia y polimorfismo.",
    fechaPublicacion: "2025-05-28",
    remitente: "María García", 
    cursoId: 1,
    leido: false,
    importante: true
  }
];

// Calificaciones detalladas
export const DEMO_GRADES = [
  {
    id: 1,
    cursoId: 1,
    cursoNombre: "Programación en Java",
    tipo: "EXAMEN",
    nombre: "Examen Parcial - POO",
    calificacion: 8.5,
    calificacionMaxima: 10,
    peso: 30,
    fecha: "2025-05-28",
    comentarios: "Buen dominio de conceptos. Mejorar en implementación de interfaces."
  },
  {
    id: 2,
    cursoId: 1,
    cursoNombre: "Programación en Java", 
    tipo: "TAREA",
    nombre: "Ejercicios POO",
    calificacion: 9.0,
    calificacionMaxima: 10,
    peso: 20,
    fecha: "2025-05-20",
    comentarios: "Excelente trabajo. Código bien estructurado."
  },
  {
    id: 3,
    cursoId: 2,
    cursoNombre: "Desarrollo Web Frontend",
    tipo: "PROYECTO",
    nombre: "Lista de Tareas React",
    calificacion: 9.2,
    calificacionMaxima: 10,
    peso: 40,
    fecha: "2025-05-30",
    comentarios: "Implementación sobresaliente. Uso correcto de hooks."
  }
];

// Recursos y materiales
export const DEMO_RESOURCES = [
  {
    id: 1,
    cursoId: 1,
    nombre: "Manual de Java - Capítulo 5",
    tipo: "PDF",
    tamaño: "2.5 MB",
    fechaSubida: "2025-05-15",
    descargas: 45,
    url: "demo-java-manual-cap5.pdf"
  },
  {
    id: 2,
    cursoId: 1,
    nombre: "Ejemplos de código POO",
    tipo: "ZIP",
    tamaño: "1.2 MB", 
    fechaSubida: "2025-05-29",
    descargas: 23,
    url: "demo-ejemplos-poo.zip"
  },
  {
    id: 3,
    cursoId: 2,
    nombre: "Guía de React Hooks",
    tipo: "PDF",
    tamaño: "800 KB",
    fechaSubida: "2025-05-25",
    descargas: 18,
    url: "demo-react-hooks-guide.pdf"
  }
];
