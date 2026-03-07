package com.cafeapp.backend.dto.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para crear o actualizar un producto.
 */
public record ProductoRequest(
        @NotBlank(message = "El nombre del producto es obligatorio")
        String nombre,

        @Positive(message = "El precio debe ser mayor que 0")
        double precioBase,

        String descripcion,

        String imagenUrl,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        boolean esModificable
) {}
