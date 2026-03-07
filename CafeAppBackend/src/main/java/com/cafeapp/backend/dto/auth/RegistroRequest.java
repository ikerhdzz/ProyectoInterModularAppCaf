package com.cafeapp.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para registrar un nuevo usuario.
 */
public record RegistroRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @NotBlank(message = "El DNI es obligatorio")
        String dni,

        @NotBlank(message = "La clase es obligatoria")
        String clase,

        @NotNull(message = "El ID del curso es obligatorio")
        Long cursoId,

        @NotNull(message = "El ID del rol es obligatorio")
        Long rolId
) {}
