package com.edutech.config;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Importar entidades del proyecto
import com.edutech.model.*;
import com.edutech.repository.*;

// Importaciones Java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@Profile("prod") // Solo se ejecutará cuando el perfil activo sea 'dev'
@Transactional // Agregar esta anotación para manejar la sesión de Hibernate
public class DataLoader implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final Faker faker = new Faker();
    private final Random random = new Random();
    
    // Inyectar repositorios del proyecto
    @Autowired
    private TipoPersonaRepository tipoPersonaRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private EjecucionRepository ejecucionRepository;
    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Cargando datos falsos para EduTech...");
        
        // Verificar si ya existen datos para evitar duplicados
        if (tipoPersonaRepository.count() == 0) {
            createTiposPersona();
        }
        
        if (personaRepository.count() == 0) {
            createPersonas();
        }
        
        if (cursoRepository.count() == 0) {
            createCursos();
        }
        
        if (ejecucionRepository.count() == 0) {
            createEjecuciones();
        }
        
        if (inscripcionRepository.count() == 0) {
            createInscripciones();
        }
        
        if (evaluacionRepository.count() == 0) {
            createEvaluaciones();
        }
        
        if (calificacionRepository.count() == 0) {
            createCalificaciones();
        }
        
        logger.info("Datos falsos cargados exitosamente para EduTech");
    }
    
    private void createTiposPersona() {
        // Crear tipos de persona básicos
        String[] tipos = {"ESTUDIANTE", "PROFESOR", "ADMINISTRADOR"};
        String[] descripciones = {
            "Estudiante inscrito en cursos",
            "Profesor que imparte cursos",
            "Administrador del sistema"
        };
        
        for (int i = 0; i < tipos.length; i++) {
            TipoPersona tipo = new TipoPersona();
            tipo.setNombre(tipos[i]);
            tipo.setDescripcion(descripciones[i]);
            tipo.setActivo(true);
            
            tipoPersonaRepository.save(tipo);
            logger.info("Tipo de persona creado: {}", tipos[i]);
        }
    }
    
    private void createPersonas() {
        List<TipoPersona> tipos = tipoPersonaRepository.findAll();
        
        // Crear estudiantes
        TipoPersona tipoEstudiante = tipos.stream()
            .filter(t -> "ESTUDIANTE".equals(t.getNombre()))
            .findFirst().orElse(tipos.get(0));
            
        for (int i = 0; i < 15; i++) {
            Persona estudiante = new Persona();
            estudiante.setRut(generateRandomRut());
            estudiante.setNombres(faker.name().firstName());
            estudiante.setApellidoPaterno(faker.name().lastName());
            estudiante.setApellidoMaterno(faker.name().lastName());
            estudiante.setCorreo(faker.internet().emailAddress());
            estudiante.setTelefono(generateRandomPhone());
            estudiante.setDireccion(faker.address().streetAddress());
            estudiante.setFechaNacimiento(faker.date().birthday(18, 25).toLocalDateTime().toLocalDate());
            estudiante.setTipoPersona(tipoEstudiante);
            estudiante.setActivo(true);
            
            personaRepository.save(estudiante);
            logger.info("Estudiante creado: {} {} - {}", estudiante.getNombres(), estudiante.getApellidoPaterno(), estudiante.getCorreo());
        }
        
        // Crear profesores
        TipoPersona tipoProfesor = tipos.stream()
            .filter(t -> "PROFESOR".equals(t.getNombre()))
            .findFirst().orElse(tipos.get(1));
            
        for (int i = 0; i < 5; i++) {
            Persona profesor = new Persona();
            profesor.setRut(generateRandomRut());
            profesor.setNombres(faker.name().firstName());
            profesor.setApellidoPaterno(faker.name().lastName());
            profesor.setApellidoMaterno(faker.name().lastName());
            profesor.setCorreo(faker.internet().emailAddress());
            profesor.setTelefono(generateRandomPhone());
            profesor.setDireccion(faker.address().streetAddress());
            profesor.setFechaNacimiento(faker.date().birthday(25, 55).toLocalDateTime().toLocalDate());
            profesor.setTipoPersona(tipoProfesor);
            profesor.setActivo(true);
            
            personaRepository.save(profesor);
            logger.info("Profesor creado: {} {} - {}", profesor.getNombres(), profesor.getApellidoPaterno(), profesor.getCorreo());
        }
    }
    
    private void createCursos() {
        String[] materias = {"Matemáticas", "Física", "Química", "Historia", "Literatura", "Inglés", "Programación", "Biología"};
        String[] modalidades = {"PRESENCIAL", "VIRTUAL", "HIBRIDA"};
        String[] ciclos = {"2024-1", "2024-2"};
        
        for (int i = 0; i < 8; i++) {
            Curso curso = new Curso();
            curso.setCodigo("EDU" + String.format("%03d", i + 1));
            curso.setNombre(materias[i] + " " + faker.options().option("Básica", "Intermedia", "Avanzada"));
            curso.setDescripcion(faker.lorem().paragraph());
            curso.setCreditos(faker.number().numberBetween(2, 6));
            curso.setHorasTeoricas(faker.number().numberBetween(2, 4));
            curso.setHorasPracticas(faker.number().numberBetween(1, 3));
            curso.setTotalHoras(curso.getHorasTeoricas() + curso.getHorasPracticas());
            curso.setCiclo(faker.options().option(ciclos));
            curso.setModalidad(faker.options().option(modalidades));
            curso.setActivo(true);
            
            cursoRepository.save(curso);
            logger.info("Curso creado: {} - {}", curso.getCodigo(), curso.getNombre());
        }
    }
    
    private void createEjecuciones() {
        List<Curso> cursos = cursoRepository.findAll();
        List<Persona> profesores = personaRepository.findProfesores();
        
        String[] periodos = {"2024-1", "2024-2"};
        String[] secciones = {"A", "B", "C"};
        String[] estados = {"PROGRAMADA", "EN_CURSO", "FINALIZADA"};
        String[] salas = {"Aula 101", "Lab 201", "Sala Virtual", "Auditorio"};
        
        for (Curso curso : cursos) {
            for (int i = 0; i < 2; i++) { // 2 ejecuciones por curso
                Ejecucion ejecucion = new Ejecucion();
                ejecucion.setCurso(curso);
                ejecucion.setPeriodo(faker.options().option(periodos));
                ejecucion.setSeccion(faker.options().option(secciones));
                ejecucion.setFechaInicio(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)));
                ejecucion.setFechaFin(ejecucion.getFechaInicio().plusDays(faker.number().numberBetween(60, 120)));
                ejecucion.setAula(faker.options().option(salas));
                ejecucion.setSala(ejecucion.getAula());
                ejecucion.setHorario(generateRandomHorario());
                ejecucion.setCapacidadMaxima(faker.number().numberBetween(20, 40));
                ejecucion.setInscritosActuales(0);
                ejecucion.setEstado(faker.options().option(estados));
                
                if (!profesores.isEmpty()) {
                    ejecucion.setProfesor(profesores.get(random.nextInt(profesores.size())));
                }
                
                ejecucionRepository.save(ejecucion);
                logger.info("Ejecución creada: {} - Sección {} - {}", curso.getNombre(), ejecucion.getSeccion(), ejecucion.getPeriodo());
            }
        }
    }
    
    private void createInscripciones() {
        List<Persona> estudiantes = personaRepository.findEstudiantes();
        List<Ejecucion> ejecuciones = ejecucionRepository.findAll();
        
        for (Persona estudiante : estudiantes) {
            // Cada estudiante se inscribe en 2-4 ejecuciones
            int numInscripciones = faker.number().numberBetween(2, 5);
            
            for (int i = 0; i < numInscripciones && i < ejecuciones.size(); i++) {
                Ejecucion ejecucion = ejecuciones.get(random.nextInt(ejecuciones.size()));
                
                // Verificar que no esté ya inscrito
                if (!inscripcionRepository.existsByPersonaIdAndEjecucionId(estudiante.getId(), ejecucion.getId())) {
                    Inscripcion inscripcion = new Inscripcion();
                    inscripcion.setPersona(estudiante);
                    inscripcion.setEjecucion(ejecucion);
                    inscripcion.setFechaInscripcion(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                    inscripcion.setEstado(faker.options().option("ACTIVA", "COMPLETADA", "CANCELADA"));
                    inscripcion.setActivo(!"CANCELADA".equals(inscripcion.getEstado()));
                    
                    if ("COMPLETADA".equals(inscripcion.getEstado())) {
                        inscripcion.setNotaFinal(faker.number().randomDouble(1, 1, 7));
                    }
                    
                    inscripcionRepository.save(inscripcion);
                    logger.info("Inscripción creada: {} en {}", estudiante.getNombres(), ejecucion.getCurso().getNombre());
                }
            }
        }
    }
    
    public void createEvaluaciones() {
        List<Ejecucion> ejecuciones = ejecucionRepository.findAll();
        String[] tiposEvaluacion = {"PRUEBA", "TAREA", "PROYECTO", "EXAMEN"};
        
        for (Ejecucion ejecucion : ejecuciones) {
            // Crear 3-5 evaluaciones por ejecución
            int numEvaluaciones = faker.number().numberBetween(3, 6);
            
            for (int i = 0; i < numEvaluaciones; i++) {
                Evaluacion evaluacion = new Evaluacion();
                evaluacion.setEjecucion(ejecucion);
                evaluacion.setTitulo(faker.options().option(tiposEvaluacion) + " " + (i + 1));
                evaluacion.setDescripcion(faker.lorem().sentence());
                evaluacion.setTipo(faker.options().option(tiposEvaluacion));
                evaluacion.setFechaInicio(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30)));
                evaluacion.setFechaFin(evaluacion.getFechaInicio().plusHours(faker.number().numberBetween(1, 24)));
                evaluacion.setDuracionMinutos(faker.number().numberBetween(60, 180));
                evaluacion.setPuntajeTotal(Double.valueOf(faker.number().numberBetween(50, 100)));
                evaluacion.setNotaMinimaAprobacion(4.0);
                evaluacion.setNotaMaxima(7.0);
                evaluacion.setExigenciaPorcentual(60.0);
                evaluacion.setIntentosPermitidos(faker.number().numberBetween(1, 3));
                evaluacion.setPonderacion(faker.number().randomDouble(1, 10, 30));
                evaluacion.setActivo(true);
                evaluacion.setPublicada(faker.bool().bool());
                
                evaluacionRepository.save(evaluacion);
                logger.info("Evaluación creada: {} para {}", evaluacion.getTitulo(), ejecucion.getCurso().getNombre());
            }
        }
    }
    
    private void createCalificaciones() {
        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        
        for (Evaluacion evaluacion : evaluaciones) {
            List<Inscripcion> inscripciones = inscripcionRepository.findByEjecucionId(evaluacion.getEjecucion().getId());
            
            for (Inscripcion inscripcion : inscripciones) {
                if ("ACTIVA".equals(inscripcion.getEstado()) || "COMPLETADA".equals(inscripcion.getEstado())) {
                    Calificacion calificacion = new Calificacion();
                    calificacion.setPersona(inscripcion.getPersona());
                    calificacion.setEvaluacion(evaluacion);
                    calificacion.setNumeroIntento(1);
                    calificacion.setPuntajeObtenido(faker.number().randomDouble(1, 0, evaluacion.getPuntajeTotal().intValue()));
                    calificacion.setPuntajeMaximo(evaluacion.getPuntajeTotal());
                    calificacion.setFechaRealizacion(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 15)));
                    calificacion.setTiempoEmpleado(faker.number().numberBetween(30, evaluacion.getDuracionMinutos()));
                    calificacion.setEstado("FINALIZADA");
                    calificacion.setObservaciones(faker.lorem().sentence());
                    
                    // Calcular nota chilena automáticamente
                    calificacion.calcularNotaChilena();
                    
                    calificacionRepository.save(calificacion);
                    logger.info("Calificación creada: {} - Nota: {}", 
                        inscripcion.getPersona().getNombres(), 
                        String.format("%.1f", calificacion.getNotaChilena()));
                }
            }
        }
    }
    
    // Métodos auxiliares
    private String generateRandomRut() {
        int numero = faker.number().numberBetween(10000000, 25000000);
        return numero + "-" + faker.options().option("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "K");
    }
    
    private String generateRandomPhone() {
        // Generar números de teléfono chilenos válidos con máximo 15 caracteres
        String[] prefijos = {"+56 9", "+56 2", "09", "02"};
        String prefijo = faker.options().option(prefijos);
        
        if (prefijo.startsWith("+56")) {
            // Formato internacional: +56 9 XXXX XXXX o +56 2 XXXX XXXX
            String numero = String.format("%04d %04d", 
                faker.number().numberBetween(1000, 9999),
                faker.number().numberBetween(1000, 9999));
            return prefijo + " " + numero; // Máximo 14 caracteres
        } else {
            // Formato nacional: 09 XXXX XXXX o 02 XXXX XXXX
            String numero = String.format("%04d %04d", 
                faker.number().numberBetween(1000, 9999),
                faker.number().numberBetween(1000, 9999));
            return prefijo + " " + numero; // Máximo 12 caracteres
        }
    }
    
    private String generateRandomHorario() {
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        String[] horas = {"08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00"};
        
        return faker.options().option(dias) + " y " + faker.options().option(dias) + " " + faker.options().option(horas);
    }
}
