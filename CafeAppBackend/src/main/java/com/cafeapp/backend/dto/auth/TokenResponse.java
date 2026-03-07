package com.cafeapp.backend.dto.auth;

/**
 * DTO que representa una respuesta que contiene únicamente un token JWT.
 */
public record TokenResponse(
        String token
) {}
