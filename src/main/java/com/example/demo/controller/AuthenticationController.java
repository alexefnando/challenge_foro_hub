package com.example.demo.controller;

import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private TokenService tokenService;
    
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.correoElectronico(), data.contrasena());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByCorreoElectronico(data.correoElectronico()) != null) {
            return ResponseEntity.badRequest().build();
        }
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.contrasena());
        Usuario newUser = new Usuario();
        newUser.setNombre(data.nombre());
        newUser.setCorreoElectronico(data.correoElectronico());
        newUser.setContrasena(encryptedPassword);
        
        this.repository.save(newUser);
        
        return ResponseEntity.ok().build();
    }
}
