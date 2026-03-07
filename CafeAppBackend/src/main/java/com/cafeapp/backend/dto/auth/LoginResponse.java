package com.cafeapp.backend.dto.auth;
import com.cafeapp.backend.dto.usuario.UsuarioResponse;

/**
 * DTO de respuesta tras un inicio de sesión exitoso.
 */
public record LoginResponse(
        String token,
        UsuarioResponse usuario
) {}
