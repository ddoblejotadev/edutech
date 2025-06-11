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
import java.util.Locale;
import java.util.Random;

@Component
@Profile("disabled") // Deshabilitado temporalmente - cambiar a "dev" para habilitar
@Transactional // Agregar esta anotación para manejar la sesión de Hibernate
public class DataLoader implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    
    // ===== CONFIGURACIÓN PARA DATOS CHILENOS =====
    // Usar locale chileno para generar datos más realistas
    private final Faker faker = new Faker(new Locale("es", "CL"));
    private final Random random = new Random();
    
    // Apellidos chilenos comunes para hacer la clase más educativa
    private final String[] APELLIDOS_CHILENOS = {
        "González", "Rodríguez", "Muñoz", "Rojas", "López", "Díaz", "Pérez", "Soto",
        "Contreras", "Silva", "Martínez", "Sepúlveda", "Morales", "Jiménez", "Herrera",
        "Medina", "Guzmán", "Núñez", "Tapia", "Castillo", "Ramos", "Espinoza", "Araya",
        "Flores", "Espínola", "Fuentes", "Torres", "Álvarez", "Poblete", "Cáceres"
    };

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
        }
    }
    
    private void createPersonas() {
        List<TipoPersona> tipos = tipoPersonaRepository.findAll();
        
        // Crear estudiantes
        TipoPersona tipoEstudiante = tipos.stream()
            .filter(t -> "ESTUDIANTE".equals(t.getNombre()))
            .findFirst().orElse(tipos.get(0));
            
        logger.info("=== Generando estudiantes con nombres chilenos ===");
        for (int i = 0; i < 15; i++) {
            Persona estudiante = new Persona();
            
            // Usar generador de RUT chileno válido
            estudiante.setRut(generarRut());
            
            // Usar nombres chilenos realistas
            String nombre = generarNombreChileno();
            String apellidoPaterno = generateChileanLastName();
            String apellidoMaterno = generateChileanLastName();
            
            estudiante.setNombres(nombre);
            estudiante.setApellidoPaterno(apellidoPaterno);
            estudiante.setApellidoMaterno(apellidoMaterno);
            
            // Generar correo basado en el nombre (más realista)
            String correo = generateEmailFromName(nombre, apellidoPaterno);
            estudiante.setCorreo(correo);
            
            estudiante.setTelefono(generaNumero());
            estudiante.setDireccion(generarDireccion());
            estudiante.setFechaNacimiento(faker.date().birthday(18, 25).toLocalDateTime().toLocalDate());
            estudiante.setTipoPersona(tipoEstudiante);
            estudiante.setActivo(true);
            
            personaRepository.save(estudiante);
            
            // Log para mostrar en clase cómo se generan los datos
            logger.info("Estudiante creado: {} {} - RUT: {}", nombre, apellidoPaterno, estudiante.getRut());
        }
        
        // Crear profesores
        TipoPersona tipoProfesor = tipos.stream()
            .filter(t -> "PROFESOR".equals(t.getNombre()))
            .findFirst().orElse(tipos.get(1));
            
        logger.info("=== Generando profesores con nombres chilenos ===");
        for (int i = 0; i < 5; i++) {
            Persona profesor = new Persona();
            
            // Usar generador de RUT chileno válido
            profesor.setRut(generarRut());
            
            // Usar nombres chilenos realistas
            String nombre = generarNombreChileno();
            String apellidoPaterno = generateChileanLastName();
            String apellidoMaterno = generateChileanLastName();
            
            profesor.setNombres(nombre);
            profesor.setApellidoPaterno(apellidoPaterno);
            profesor.setApellidoMaterno(apellidoMaterno);
            
            // Generar correo basado en el nombre
            String correo = generateEmailFromName(nombre, apellidoPaterno);
            profesor.setCorreo(correo);
            
            profesor.setTelefono(generaNumero());
            profesor.setDireccion(generarDireccion());
            profesor.setFechaNacimiento(faker.date().birthday(25, 55).toLocalDateTime().toLocalDate());
            profesor.setTipoPersona(tipoProfesor);
            profesor.setActivo(true);
            
            personaRepository.save(profesor);
            
            // Log para mostrar en clase cómo se generan los datos
            logger.info("Profesor creado: {} {} - RUT: {}", nombre, apellidoPaterno, profesor.getRut());
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
                    
                    inscripcionRepository.save(inscripcion);
                }
            }
        }
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    /**
     * Genera un RUT chileno simple válido
      */
    private String generarRut() {
        // Generar número simple entre 10.000.000 y 25.000.000
        int numero = faker.number().numberBetween(10000000, 25000000);
        
        // Usar dígitos verificadores simples (solo números)
        int dv = numero % 10;
        
        // Formatear como RUT chileno estándar
        return String.format("%d.%03d.%03d-%d", 
            numero / 1000000, 
            (numero / 1000) % 1000, 
            numero % 1000, 
            dv);
    }
    
    /**
     * Genera un nombre chileno simple
     * Toma un nombre al azar de las listas predefinidas
     */
    private String generarNombreChileno() {
        // Combinar todos los nombres en una sola lista
        String[] todosLosNombres = {
            "Carlos", "Luis", "Jorge", "Francisco", "Miguel", "Juan", "Pedro", "José",
            "María", "Carmen", "Rosa", "Ana", "Patricia", "Claudia", "Sandra", "Mónica"
        };
        
        return faker.options().option(todosLosNombres);
    }
    
    /**
     * Genera un apellido chileno simple
     */
    private String generateChileanLastName() {
        return faker.options().option(APELLIDOS_CHILENOS);
    }
    
    /**
     * Genera un email simple basado en nombre y apellido
     */
    private String generateEmailFromName(String nombre, String apellido) {
        // Crear email simple: nombre.apellido@dominio.com
        String nombreSimple = nombre.toLowerCase().replace(" ", "");
        String apellidoSimple = apellido.toLowerCase();
        String[] dominios = {"gmail.com", "hotmail.com", "edutech.cl"};
        String dominio = faker.options().option(dominios);
        
        return nombreSimple + "." + apellidoSimple + "@" + dominio;
    }
    
    /**
     * Genera un teléfono chileno simple
     */
    private String generaNumero() {
        // Formato simple: +56 9 XXXXXXXX (solo móviles)
        String numero = String.valueOf(faker.number().numberBetween(10000000, 99999999));
        return "+56 9 " + numero;
    }
    
    /**
     * Genera una dirección chilena simple
     */
    private String generarDireccion() {
        // Formato simple: Calle Nombre Número, Comuna
        String calle = faker.address().streetName();
        int numero = faker.number().numberBetween(100, 9999);
        String[] comunas = {"Santiago", "Las Condes", "Providencia", "Ñuñoa", "Maipú"};
        String comuna = faker.options().option(comunas);
        
        return calle + " " + numero + ", " + comuna;
    }
    
    /**
     * Genera un horario simple para las clases
     */
    private String generateRandomHorario() {
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        String[] horas = {"08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00"};
        
        String dia = faker.options().option(dias);
        String hora = faker.options().option(horas);
        
        return dia + " " + hora;
    }
}
