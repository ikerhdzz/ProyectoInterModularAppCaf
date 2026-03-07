package com.cafeapp.backend.dto.carrito;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para agregar un extra a un ítem del carrito.
 */
public record AgregarExtraRequest(
        @NotNull(message = "El ID del extra es obligatorio")
        Long extraId
) {}
