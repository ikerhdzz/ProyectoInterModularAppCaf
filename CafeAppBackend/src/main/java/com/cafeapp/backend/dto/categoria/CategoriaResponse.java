package com.cafeapp.backend.dto.categoria;

/**
 * DTO de respuesta para categoría.
 */
public record CategoriaResponse(
        Long id,
        String nombre,
        String iconoUrl
) {}
