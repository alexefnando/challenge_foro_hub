package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico debe tener un formato válido")
        String correoElectronico,
        
        @NotBlank(message = "La contraseña es obligatoria")
        String contrasena
) {}
