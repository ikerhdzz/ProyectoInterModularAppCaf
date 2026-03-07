package com.cafeapp.backend.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para crear o actualizar un usuario.
 */
public record UsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "El DNI es obligatorio")
        String dni,

        String clase,

        Long cursoId,

        @NotNull(message = "El rol es obligatorio")
        Long rolId,

        Long centroId,

        Long idPadre,

        boolean perfilCompletado
) {}
