package com.cafeapp.backend.dto.producto;

/**
 * DTO que representa un extra asociado a un producto o ítem del carrito.
 */
public record ExtraProductoResponse(
        Long id,
        String nombre,
        double precio
) {}
