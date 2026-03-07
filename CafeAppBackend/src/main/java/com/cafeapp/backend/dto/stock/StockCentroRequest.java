package com.cafeapp.backend.dto.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO para crear o actualizar stock de un producto en un centro.
 */
public record StockCentroRequest(
        @NotNull(message = "El centro es obligatorio")
        Long centroId,

        @NotNull(message = "El producto es obligatorio")
        Long productoId,

        @PositiveOrZero(message = "El stock debe ser 0 o mayor")
        int stockActual,

        @PositiveOrZero(message = "La alerta de stock debe ser 0 o mayor")
        int alertaStock
) {}
