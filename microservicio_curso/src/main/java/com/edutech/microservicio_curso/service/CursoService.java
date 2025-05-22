package com.edutech.microservicio_curso.service;

import com.edutech.microservicio_curso.model.Curso;
import com.edutech.microservicio_curso.model.TipoCurso;
import com.edutech.microservicio_curso.repository.CursoRepository;
import com.edutech.microservicio_curso.repository.TipoCursoRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CursoService {
    
    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private TipoCursoRepository tipoCursoRepository;
    
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }
    
    public Optional<Curso> findById(Integer id) {
        return cursoRepository.findById(id);
    }
    
    public List<Curso> findByCategoria(String categoria) {
        return cursoRepository.findByCategoriaIgnoreCase(categoria);
    }
    
    public List<Curso> searchByNombre(String nombre) {
        return cursoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public List<Curso> findByTipoCurso(Integer idTipoCurso) {
        return cursoRepository.findByTipoCursoId(idTipoCurso);
    }
    
    public Curso save(Curso curso) {
        // Si es un nuevo curso, asegúrate de que el ID sea null
        if (curso.getIdCurso() != null && curso.getIdCurso() == 0) {
            curso.setIdCurso(null);
        }
        return cursoRepository.save(curso);
    }
    
    public void deleteById(Integer id) {
        cursoRepository.deleteById(id);
    }
    
    @Transactional
    @PostConstruct
    public void init() {
        try {
            // Verificar si ya existen datos
            long count = cursoRepository.count();
            if (count == 0) {
                logger.info("Inicializando datos de cursos...");
                
                // Primero, crear tipos de curso si no existen
                TipoCurso tipoCursoPresencial = new TipoCurso();
                tipoCursoPresencial.setNombre("Presencial");
                tipoCursoPresencial = tipoCursoRepository.save(tipoCursoPresencial);
                
                TipoCurso tipoCursoOnline = new TipoCurso();
                tipoCursoOnline.setNombre("Online");
                tipoCursoOnline = tipoCursoRepository.save(tipoCursoOnline);
                
                // Crear cursos iniciales con precio dentro del rango aceptable
                Curso curso1 = new Curso();
                curso1.setNombre("Programación Java");
                curso1.setDescripcion("Curso de fundamentos de Java");
                curso1.setDuracionHoras(40);
                curso1.setNivel("Intermedio");
                curso1.setCategoria("Programación");
                curso1.setPrecio(new BigDecimal("9999.99")); // Mantén el precio dentro del rango
                curso1.setSence("S");
                curso1.setFechaCreacion(LocalDate.now());
                curso1.setFechaPublicacion(LocalDate.now().plusDays(5));
                curso1.setTipoCurso(tipoCursoPresencial);
                cursoRepository.save(curso1);
                
                logger.info("Datos de cursos inicializados correctamente");
            }
        } catch (Exception e) {
            logger.error("Error al inicializar datos: " + e.getMessage(), e);
        }
    }
}
