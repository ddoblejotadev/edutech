package com.edutech.microservicio_curso.service;

import com.edutech.microservicio_curso.model.TipoCurso;
import com.edutech.microservicio_curso.repository.TipoCursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoCursoService {

    private final TipoCursoRepository tipoCursoRepository;

    public TipoCursoService(TipoCursoRepository tipoCursoRepository) {
        this.tipoCursoRepository = tipoCursoRepository;
    }

    public List<TipoCurso> findAll() {
        return tipoCursoRepository.findAll();
    }

    public Optional<TipoCurso> findById(Integer id) {
        return tipoCursoRepository.findById(id);
    }

    public Optional<TipoCurso> findByNombre(String nombre) {
        return tipoCursoRepository.findByNombreIgnoreCase(nombre);
    }
    
    @Transactional
    public TipoCurso save(TipoCurso tipoCurso) {
        return tipoCursoRepository.save(tipoCurso);
    }
    
    @Transactional
    public boolean deleteById(Integer id) {
        if (tipoCursoRepository.existsById(id)) {
            tipoCursoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsByNombre(String nombre) {
        return tipoCursoRepository.existsByNombreIgnoreCase(nombre);
    }
}
