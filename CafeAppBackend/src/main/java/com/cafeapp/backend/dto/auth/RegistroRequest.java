package com.cafeapp.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para registrar un nuevo usuario.
 * 
 * NOTA IMPORTANTE:
 * - El rol se asigna automáticamente como USUARIO (no puede ser modificado)
 * - La contraseña debe cumplir requisitos de seguridad
 */
public record RegistroRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales (@$!%*?&)"
        )
        String password,

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^\\d{8}[A-Z]$", message = "El DNI debe tener formato válido (8 dígitos + letra)")
        String dni,

        @NotBlank(message = "La clase es obligatoria")
        String clase,

        @NotNull(message = "El ID del curso es obligatorio")
        Long cursoId
) {}
