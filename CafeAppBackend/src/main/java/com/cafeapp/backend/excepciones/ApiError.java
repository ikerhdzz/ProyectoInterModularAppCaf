package com.cafeapp.backend.excepciones;

import java.time.LocalDateTime;

/**
 * Representa un error estructurado enviado al frontend.
 *
 * Campos:
 * - status: código HTTP
 * - error: tipo de error (validación, negocio, interno)
 * - message: mensaje detallado
 * - path: endpoint donde ocurrió
 * - timestamp: fecha y hora del error
 *
 * Se usa como respuesta estándar en GlobalExceptionHandler.
 */
public record ApiError(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {}
