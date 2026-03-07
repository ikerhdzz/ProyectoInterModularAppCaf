package com.cafeapp.backend.dto.categoria;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear o actualizar una categoría.
 */
public record CategoriaRequest(
        @NotBlank(message = "El nombre de la categoría es obligatorio")
        String nombre,
        String iconoUrl
) {}
