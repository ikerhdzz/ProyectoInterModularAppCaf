package com.cafeapp.backend.dto.usuario;

import com.cafeapp.backend.modelo.Usuario;

/**
 * DTO unificado para devolver información pública del usuario.
 *
 * Se utiliza en:
 * - Login
 * - /auth/me
 * - Listado de usuarios
 * - Detalles de usuario
 * - Cualquier endpoint que devuelva información del usuario
 */
public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String dni,
        String clase,
        String imagenUrl,
        String rol,
        String curso,
        String centro,
        boolean perfilCompletado
) {

    /**
     * Constructor que mapea automáticamente desde la entidad Usuario.
     */
    public UsuarioResponse(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getDni(),
                usuario.getClase(),
                usuario.getImagenUrl(),
                usuario.getRol() != null ? usuario.getRol().getNombre() : null,
                usuario.getCurso() != null ? usuario.getCurso().getNombre() : null,
                usuario.getCentro() != null ? usuario.getCentro().getNombre() : null,
                usuario.isPerfilCompletado()
        );
    }
}
