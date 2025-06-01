package com.edutech.microservicio_persona.controller;

import com.edutech.microservicio_persona.model.TipoPersona;
import com.edutech.microservicio_persona.service.TipoPersonaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tipospersona", produces = "application/json;charset=UTF-8")
public class TipoPersonaController {

    private final TipoPersonaService tipoPersonaService;

    @Autowired
    public TipoPersonaController(TipoPersonaService tipoPersonaService) {
        this.tipoPersonaService = tipoPersonaService;
    }

    @GetMapping
    public ResponseEntity<List<TipoPersona>> getAllTiposPersona() {
        List<TipoPersona> tiposPersona = tipoPersonaService.findAll();
        return new ResponseEntity<>(tiposPersona, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPersona> getTipoPersonaById(@PathVariable Integer id) {
        return tipoPersonaService.findById(id)
                .map(tipoPersona -> new ResponseEntity<>(tipoPersona, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TipoPersona> createTipoPersona(@RequestBody TipoPersona tipoPersona) {
        try {
            TipoPersona nuevoTipoPersona = tipoPersonaService.save(tipoPersona);
            return new ResponseEntity<>(nuevoTipoPersona, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPersona> updateTipoPersona(@PathVariable Integer id, @RequestBody TipoPersona tipoPersona) {
        return tipoPersonaService.findById(id)
                .map(existingTipoPersona -> {
                    tipoPersona.setIdTipoPersona(id);
                    TipoPersona updatedTipoPersona = tipoPersonaService.save(tipoPersona);
                    return new ResponseEntity<>(updatedTipoPersona, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoPersona(@PathVariable Integer id) {
        return tipoPersonaService.findById(id)
                .map(tipoPersona -> {
                    tipoPersonaService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
