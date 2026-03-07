package com.cafeapp.backend.dto.centro;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear o actualizar un centro.
 */
public record CentroRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El código es obligatorio")
        String codigo,

        String direccion,
        String telefono
) {}
