package com.cafeapp.backend.dto.pago;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para representar un item de pago.
 * Utilizado al crear un PaymentIntent de Stripe.
 */
public record PaymentItemRequest(
        @NotNull(message = "El ID del producto es requerido")
        Long productoId,

        @Min(value = 1, message = "La cantidad mínima es 1")
        Integer cantidad
) {}
