package com.cafeapp.backend.dto.pago;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para solicitar la creación de un PaymentIntent.
 * Contiene los items que el usuario desea pagar.
 */
public record PaymentIntentRequest(
        @NotNull(message = "Items es requerido")
        @NotEmpty(message = "Debe haber al menos un item")
        List<PaymentItemRequest> items
) {}
