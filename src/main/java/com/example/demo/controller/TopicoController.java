package com.example.demo.controller;

import com.example.demo.dto.TopicoRequestDTO;
import com.example.demo.dto.TopicoResponseDTO;
import com.example.demo.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    
    @Autowired
    private TopicoService topicoService;
    
    @PostMapping
    public ResponseEntity<TopicoResponseDTO> crearTopico(@Valid @RequestBody TopicoRequestDTO topicoRequest) {
        TopicoResponseDTO topicoCreado = topicoService.crearTopico(topicoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(topicoCreado);
    }
    
    @GetMapping
    public ResponseEntity<Page<TopicoResponseDTO>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable pageable,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer anio) {
        
        Page<TopicoResponseDTO> topicos;
        
        // Aplicar filtros según los parámetros recibidos
        if (curso != null && anio != null) {
            topicos = topicoService.buscarPorCursoYAnio(curso, anio, pageable);
        } else if (curso != null) {
            topicos = topicoService.buscarPorCurso(curso, pageable);
        } else if (anio != null) {
            topicos = topicoService.buscarPorAnio(anio, pageable);
        } else {
            topicos = topicoService.listarTopicos(pageable);
        }
        
        return ResponseEntity.ok(topicos);
    }
    
    @GetMapping("/primeros10")
    public ResponseEntity<List<TopicoResponseDTO>> listarPrimeros10Topicos() {
        List<TopicoResponseDTO> topicos = topicoService.listarPrimeros10Topicos();
        return ResponseEntity.ok(topicos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> obtenerTopicoPorId(@PathVariable Long id) {
        TopicoResponseDTO topico = topicoService.obtenerTopicoPorId(id);
        return ResponseEntity.ok(topico);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> actualizarTopico(
            @PathVariable Long id, 
            @Valid @RequestBody TopicoRequestDTO topicoRequest) {
        TopicoResponseDTO topicoActualizado = topicoService.actualizarTopico(id, topicoRequest);
        return ResponseEntity.ok(topicoActualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.noContent().build();
    }
}
