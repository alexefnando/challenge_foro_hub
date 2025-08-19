package com.example.demo.controller;

import com.example.demo.model.Curso;
import com.example.demo.model.Perfil;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @PostMapping("/usuario")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioGuardado);
    }
    
    @PostMapping("/curso")
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        Curso cursoGuardado = cursoRepository.save(curso);
        return ResponseEntity.ok(cursoGuardado);
    }
    
    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }
    
    @GetMapping("/cursos")
    public ResponseEntity<?> listarCursos() {
        return ResponseEntity.ok(cursoRepository.findAll());
    }
}
