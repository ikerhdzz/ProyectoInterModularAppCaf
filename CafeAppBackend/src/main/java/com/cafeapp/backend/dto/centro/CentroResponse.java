package com.cafeapp.backend.dto.centro;

/**
 * DTO de respuesta para un centro.
 */
public record CentroResponse(
        Long id,
        String nombre,
        String codigo,
        String direccion,
        String telefono
) {}
