package com.cafeapp.backend.dto.carrito;

import com.cafeapp.backend.dto.producto.ExtraProductoResponse;
import java.util.List;

/**
 * DTO que representa un ítem dentro del carrito.
 */
public record ItemCarritoResponse(
        Long itemId,
        Long productoId,
        String nombreProducto,
        double precioBase,
        int cantidad,
        List<ExtraProductoResponse> extras
) {
    /**
     * Calcula el subtotal del ítem considerando extras.
     */
    public double subtotal() {
        double extrasTotal = extras.stream().mapToDouble(ExtraProductoResponse::precio).sum();
        return (precioBase * cantidad) + extrasTotal;
    }
}
