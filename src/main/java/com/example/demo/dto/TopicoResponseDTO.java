package com.example.demo.dto;

import com.example.demo.model.StatusTopico;
import java.time.LocalDateTime;

public record TopicoResponseDTO(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        String autorNombre,
        String cursoNombre
) {}
