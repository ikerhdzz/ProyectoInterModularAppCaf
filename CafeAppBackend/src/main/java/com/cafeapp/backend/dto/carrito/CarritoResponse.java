package com.cafeapp.backend.dto.carrito;

import java.util.List;

/**
 * DTO que representa el carrito completo del usuario.
 */
public record CarritoResponse(
        List<ItemCarritoResponse> items,
        double total
) {}
