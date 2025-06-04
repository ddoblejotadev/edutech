package com.edutech.service;

import com.edutech.model.*;
import com.edutech.repository.*;
import net.datafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("dev") // Solo se ejecuta en perfil de desarrollo
public class DataFakerService implements CommandLineRunner {

    private final TipoPersonaRepository tipoPersonaRepository;
    private final PersonaRepository personaRepository;
    private final CursoRepository cursoRepository;
    private final EjecucionRepository ejecucionRepository;
    private final InscripcionRepository inscripcionRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final CalificacionRepository calificacionRepository;

    @Value("${edutech.datafaker.enabled:false}")
    private boolean dataFakerEnabled;
    
    @Value("${edutech.dev.auto-populate:false}")
    private boolean autoPopulate;
    
    @Value("${edutech.datafaker.personas.cantidad:50}")
    private int cantidadPersonas;
    
    @Value("${edutech.datafaker.cursos.cantidad:20}")
    private int cantidadCursos;

    private final Faker faker = new Faker(new Locale("es", "CL"));
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!dataFakerEnabled || !autoPopulate) {
            log.info("🔧 DataFaker está deshabilitado o auto-populate está en false.");
            log.info("   Para habilitar datos automáticos:");
            log.info("   - edutech.datafaker.enabled=true");
            log.info("   - edutech.dev.auto-populate=true");
            log.info("📊 Base de datos de desarrollo está VACÍA y lista para usar.");
            return;
        }

        log.info("🚀 Iniciando generación de datos de prueba con DataFaker...");
        log.info("📝 Esta es la BD de DESARROLLO - se poblará automáticamente");
        
        // Solo crear tipos básicos si no existen
        if (tipoPersonaRepository.count() == 0) {
            log.info("📋 Creando tipos de persona básicos...");
            generarTiposPersona();
        }

        // Verificar si ya hay datos antes de generar más
        if (personaRepository.count() > 0) {
            log.info("📊 Ya existen {} personas en la BD de desarrollo.", personaRepository.count());
            log.info("🔄 Para regenerar datos, reinicia la aplicación (ddl-auto=create-drop)");
            mostrarResumen();
            return;
        }

        // Generar datos completos
        generarPersonas();
        generarCursos();
        generarEjecuciones();
        generarInscripciones();
        generarEvaluaciones();
        generarCalificaciones();

        log.info("✅ Generación de datos completada exitosamente!");
        log.info("🎯 BD de desarrollo poblada con datos de prueba");
        mostrarResumen();
    }

    private void generarTiposPersona() {
        log.info("📝 Generando tipos de persona...");
        
        if (tipoPersonaRepository.count() == 0) {
            TipoPersona estudiante = new TipoPersona();
            estudiante.setNombre("ESTUDIANTE");
            estudiante.setDescripcion("Estudiante regular de la institución");
            estudiante.setActivo(true);
            tipoPersonaRepository.save(estudiante);

            TipoPersona profesor = new TipoPersona();
            profesor.setNombre("PROFESOR");
            profesor.setDescripcion("Docente de la institución");
            profesor.setActivo(true);
            tipoPersonaRepository.save(profesor);

            TipoPersona administrativo = new TipoPersona();
            administrativo.setNombre("ADMINISTRATIVO");
            administrativo.setDescripcion("Personal administrativo");
            administrativo.setActivo(true);
            tipoPersonaRepository.save(administrativo);
            
            log.info("✅ Tipos de persona creados");
        }
    }

    private void generarPersonas() {
        log.info("👥 Generando {} personas...", cantidadPersonas);
        
        List<TipoPersona> tipos = tipoPersonaRepository.findAll();
        TipoPersona tipoEstudiante = tipos.stream()
            .filter(t -> "ESTUDIANTE".equals(t.getNombre()))
            .findFirst().orElse(tipos.get(0));
        TipoPersona tipoProfesor = tipos.stream()
            .filter(t -> "PROFESOR".equals(t.getNombre()))
            .findFirst().orElse(tipos.get(0));

        // Generar profesores (20% del total)
        int cantidadProfesores = Math.max(5, cantidadPersonas / 5);
        for (int i = 0; i < cantidadProfesores; i++) {
            Persona profesor = new Persona();
            profesor.setRut(generarRutChileno());
            profesor.setNombres(faker.name().firstName());
            profesor.setApellidoPaterno(faker.name().lastName());
            profesor.setApellidoMaterno(faker.name().lastName());
            profesor.setCorreo(faker.internet().emailAddress());
            profesor.setTelefono("+569" + faker.number().digits(8));
            profesor.setDireccion(faker.address().streetAddress() + ", " + faker.address().city());
            profesor.setFechaNacimiento(LocalDate.now().minusYears(25 + random.nextInt(20)));
            profesor.setTipoPersona(tipoProfesor);
            profesor.setActivo(true);
            
            personaRepository.save(profesor);
        }

        // Generar estudiantes (80% del total)
        int cantidadEstudiantes = cantidadPersonas - cantidadProfesores;
        for (int i = 0; i < cantidadEstudiantes; i++) {
            Persona estudiante = new Persona();
            estudiante.setRut(generarRutChileno());
            estudiante.setNombres(faker.name().firstName());
            estudiante.setApellidoPaterno(faker.name().lastName());
            estudiante.setApellidoMaterno(faker.name().lastName());
            estudiante.setCorreo(faker.internet().emailAddress());
            estudiante.setTelefono("+569" + faker.number().digits(8));
            estudiante.setDireccion(faker.address().streetAddress() + ", " + faker.address().city());
            estudiante.setFechaNacimiento(LocalDate.now().minusYears(18 + random.nextInt(8)));
            estudiante.setTipoPersona(tipoEstudiante);
            estudiante.setActivo(true);
            
            personaRepository.save(estudiante);
        }
        
        log.info("✅ {} personas generadas", cantidadPersonas);
    }

    private void generarCursos() {
        log.info("📚 Generando {} cursos...", cantidadCursos);
        
        String[] nombresCursos = {
            "Algoritmos y Estructuras de Datos", "Cálculo Diferencial e Integral", 
            "Programación Orientada a Objetos", "Base de Datos", "Redes de Computadores",
            "Ingeniería de Software", "Sistemas Operativos", "Matemáticas Discretas",
            "Estadística y Probabilidades", "Física General", "Química General",
            "Arquitectura de Computadores", "Compiladores", "Inteligencia Artificial",
            "Desarrollo Web", "Programación Móvil", "Seguridad Informática",
            "Gestión de Proyectos", "Emprendimiento", "Ética Profesional"
        };

        String[] modalidades = {"PRESENCIAL", "ONLINE", "HIBRIDA"};
        String[] ciclos = {"2024-1", "2024-2"};

        for (int i = 0; i < cantidadCursos && i < nombresCursos.length; i++) {
            Curso curso = new Curso();
            curso.setCodigo("CC" + String.format("%04d", 1001 + i));
            curso.setNombre(nombresCursos[i]);
            curso.setDescripcion(faker.lorem().sentence(10));
            curso.setCreditos(3 + random.nextInt(5)); // 3-7 créditos
            curso.setHorasTeoricas(30 + random.nextInt(40)); // 30-70 horas
            curso.setHorasPracticas(15 + random.nextInt(25)); // 15-40 horas
            curso.setTotalHoras(curso.getHorasTeoricas() + curso.getHorasPracticas());
            curso.setCiclo(ciclos[random.nextInt(ciclos.length)]);
            curso.setModalidad(modalidades[random.nextInt(modalidades.length)]);
            curso.setActivo(true);
            
            cursoRepository.save(curso);
        }
        
        log.info("✅ {} cursos generados", cantidadCursos);
    }

    private void generarEjecuciones() {
        log.info("🏫 Generando ejecuciones de cursos...");
        
        List<Curso> cursos = cursoRepository.findAll();
        List<Persona> profesores = personaRepository.findProfesores();
        
        String[] aulas = {"Sala 101", "Sala 102", "Sala 201", "Lab 301", "Lab 302", "Auditorio A"};
        String[] horarios = {
            "Lun-Mie 08:00-09:30", "Mar-Jue 10:00-11:30", "Mie-Vie 14:00-15:30",
            "Lun-Mie 15:30-17:00", "Mar-Jue 08:00-09:30", "Vie 09:00-12:00"
        };

        for (Curso curso : cursos) {
            // Crear 1-2 ejecuciones por curso
            int cantidadEjecuciones = 1 + random.nextInt(2);
            
            for (int i = 0; i < cantidadEjecuciones; i++) {
                Ejecucion ejecucion = new Ejecucion();
                ejecucion.setCurso(curso);
                ejecucion.setPeriodo("2024-1");
                ejecucion.setSeccion(String.valueOf((char)('A' + i)));
                ejecucion.setFechaInicio(LocalDate.of(2024, 3, 15));
                ejecucion.setFechaFin(LocalDate.of(2024, 7, 15));
                ejecucion.setAula(aulas[random.nextInt(aulas.length)]);
                ejecucion.setHorario(horarios[random.nextInt(horarios.length)]);
                ejecucion.setCapacidadMaxima(20 + random.nextInt(21)); // 20-40 estudiantes
                ejecucion.setInscritosActuales(0);
                ejecucion.setEstado("ACTIVO");
                
                if (!profesores.isEmpty()) {
                    ejecucion.setProfesor(profesores.get(random.nextInt(profesores.size())));
                }
                
                ejecucionRepository.save(ejecucion);
            }
        }
        
        log.info("✅ Ejecuciones generadas");
    }

    private void generarInscripciones() {
        log.info("📋 Generando inscripciones...");
        
        List<Persona> estudiantes = personaRepository.findEstudiantes();
        List<Ejecucion> ejecuciones = ejecucionRepository.findAll();
        
        for (Persona estudiante : estudiantes) {
            // Cada estudiante se inscribe en 2-5 cursos
            int cantidadInscripciones = 2 + random.nextInt(4);
            
            for (int i = 0; i < cantidadInscripciones && i < ejecuciones.size(); i++) {
                Ejecucion ejecucion = ejecuciones.get(random.nextInt(ejecuciones.size()));
                
                // Verificar que no esté ya inscrito
                if (!inscripcionRepository.existsByPersonaIdAndEjecucionId(estudiante.getId(), ejecucion.getId())) {
                    Inscripcion inscripcion = new Inscripcion();
                    inscripcion.setPersona(estudiante);
                    inscripcion.setEjecucion(ejecucion);
                    inscripcion.setFechaInscripcion(LocalDateTime.now().minusDays(random.nextInt(30)));
                    inscripcion.setEstado("ACTIVO");
                    inscripcion.setActivo(true);
                    
                    inscripcionRepository.save(inscripcion);
                    
                    // Actualizar contador de inscritos
                    ejecucion.setInscritosActuales(ejecucion.getInscritosActuales() + 1);
                    ejecucionRepository.save(ejecucion);
                }
            }
        }
        
        log.info("✅ Inscripciones generadas");
    }

    private void generarEvaluaciones() {
        log.info("📝 Generando evaluaciones...");
        
        List<Ejecucion> ejecuciones = ejecucionRepository.findAll();
        String[] tiposEvaluacion = {"CERTAMEN", "TAREA", "PROYECTO", "LABORATORIO", "QUIZ"};
        
        for (Ejecucion ejecucion : ejecuciones) {
            // 2-4 evaluaciones por ejecución
            int cantidadEvaluaciones = 2 + random.nextInt(3);
            
            for (int i = 0; i < cantidadEvaluaciones; i++) {
                Evaluacion evaluacion = new Evaluacion();
                evaluacion.setEjecucion(ejecucion);
                evaluacion.setTitulo(tiposEvaluacion[random.nextInt(tiposEvaluacion.length)] + " " + (i + 1));
                evaluacion.setDescripcion(faker.lorem().sentence(8));
                evaluacion.setTipo(tiposEvaluacion[random.nextInt(tiposEvaluacion.length)]);
                evaluacion.setPuntajeTotal(50.0 + random.nextDouble() * 50); // 50-100 puntos
                
                LocalDateTime fechaBase = LocalDateTime.now().minusDays(random.nextInt(60));
                evaluacion.setFechaInicio(fechaBase);
                evaluacion.setFechaFin(fechaBase.plusHours(2));
                
                evaluacion.setIntentosPermitidos(1 + random.nextInt(3));
                evaluacion.setActivo(true);
                evaluacion.setPublicada(true);
                evaluacion.setNotaMinimaAprobacion(4.0);
                evaluacion.setNotaMaxima(7.0);
                evaluacion.setExigenciaPorcentual(60.0);
                
                evaluacionRepository.save(evaluacion);
            }
        }
        
        log.info("✅ Evaluaciones generadas");
    }

    private void generarCalificaciones() {
        log.info("📊 Generando calificaciones...");
        
        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        
        for (Evaluacion evaluacion : evaluaciones) {
            List<Inscripcion> inscripciones = inscripcionRepository.findByEjecucionId(evaluacion.getEjecucion().getId());
            
            for (Inscripcion inscripcion : inscripciones) {
                // 80% de los estudiantes tienen calificación
                if (random.nextDouble() < 0.8) {
                    Calificacion calificacion = new Calificacion();
                    calificacion.setPersona(inscripcion.getPersona());
                    calificacion.setEvaluacion(evaluacion);
                    calificacion.setNumeroIntento(1);
                    
                    // Generar puntaje con distribución normal (promedio 75%)
                    double porcentaje = Math.max(0, Math.min(100, 
                        75 + (random.nextGaussian() * 15))); // Distribución normal
                    calificacion.setPuntajeObtenido(evaluacion.getPuntajeTotal() * porcentaje / 100);
                    calificacion.setPuntajeMaximo(evaluacion.getPuntajeTotal());
                    
                    calificacion.setFechaRealizacion(evaluacion.getFechaInicio().plusMinutes(random.nextInt(120)));
                    calificacion.setTiempoEmpleado(30 + random.nextInt(90)); // 30-120 minutos
                    calificacion.setEstado("COMPLETADA");
                    
                    // Calcular nota chilena
                    double notaChilena = 1.0 + (porcentaje / 100.0) * 6.0;
                    calificacion.setNotaChilena(Math.round(notaChilena * 10) / 10.0);
                    
                    calificacionRepository.save(calificacion);
                }
            }
        }
        
        log.info("✅ Calificaciones generadas");
    }

    private String generarRutChileno() {
        int numero = 10000000 + random.nextInt(20000000);
        return numero + "-" + calcularDigitoVerificador(numero);
    }

    private char calcularDigitoVerificador(int rut) {
        int suma = 0;
        int multiplicador = 2;
        
        while (rut > 0) {
            suma += (rut % 10) * multiplicador;
            rut /= 10;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }
        
        int resto = suma % 11;
        int dv = 11 - resto;
        
        if (dv == 11) return '0';
        if (dv == 10) return 'K';
        return (char) ('0' + dv);
    }

    private void mostrarResumen() {
        log.info("📈 RESUMEN DE DATOS GENERADOS:");
        log.info("👥 Personas: {}", personaRepository.count());
        log.info("📚 Cursos: {}", cursoRepository.count());
        log.info("🏫 Ejecuciones: {}", ejecucionRepository.count());
        log.info("📋 Inscripciones: {}", inscripcionRepository.count());
        log.info("📝 Evaluaciones: {}", evaluacionRepository.count());
        log.info("📊 Calificaciones: {}", calificacionRepository.count());
    }
}