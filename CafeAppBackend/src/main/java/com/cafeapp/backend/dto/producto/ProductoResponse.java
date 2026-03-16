package com.cafeapp.backend.dto.producto;

import java.util.List;

/**
 * DTO de respuesta para un producto.
 */
public record ProductoResponse(
        Long id,
        String nombre,
        double precioBase,
        String descripcion,
        String imagenUrl,
        String categoria,
        boolean esModificable,
        List<Long> alergenosIds
) {}
