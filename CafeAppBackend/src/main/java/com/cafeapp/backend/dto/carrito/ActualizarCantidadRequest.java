package com.cafeapp.backend.dto.carrito;

import jakarta.validation.constraints.Min;

/**
 * DTO para actualizar la cantidad de un producto en el carrito.
 */
public record ActualizarCantidadRequest(
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        int cantidad
) {}
