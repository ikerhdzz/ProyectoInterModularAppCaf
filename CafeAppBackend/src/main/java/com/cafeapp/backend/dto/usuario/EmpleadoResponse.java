package com.cafeapp.backend.dto.usuario;

public record EmpleadoResponse(
        Long id,
        String nombre,
        String email,
        String rol,
        Long centroId
) {}
