package com.cafeapp.backend.dto.alergeno;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear un nuevo alérgeno.
 */
public record CrearAlergenoRequest(
        @NotBlank(message = "El nombre del alérgeno es obligatorio")
        String nombre
) {}
