package com.cafeapp.backend.dto.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para agregar un producto al carrito.
 */
public record AgregarProductoRequest(
        @NotNull(message = "El ID del producto es obligatorio")
        Long productoId,

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        int cantidad
) {}
