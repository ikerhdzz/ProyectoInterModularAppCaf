package com.cafeapp.backend.excepciones;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 *
 * Esta clase captura y transforma excepciones comunes en respuestas
 * JSON uniformes mediante la clase ApiError.
 *
 * Ventajas:
 * - Respuestas consistentes para el frontend
 * - Manejo centralizado de errores
 * - Mensajes más claros para el usuario
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación provenientes de @Valid.
     *
     * Ejemplo: campos vacíos, formatos incorrectos, etc.
     *
     * @param ex excepción capturada
     * @param request información de la petición
     * @return ApiError con detalles de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidaciones(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validación fallida",
                errores.toString(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    /**
     * Maneja RuntimeException, que son las más comunes en la lógica de negocio.
     *
     * Ejemplos:
     * - "Producto no encontrado"
     * - "Stock insuficiente"
     * - "Turno no disponible"
     *
     * @param ex excepción capturada
     * @param request información de la petición
     * @return ApiError con mensaje de negocio
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> manejarRuntime(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Error de negocio",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    /**
     * Maneja cualquier excepción no contemplada por los métodos anteriores.
     *
     * Esto evita que errores inesperados rompan la aplicación.
     *
     * @param ex excepción capturada
     * @param request información de la petición
     * @return ApiError con mensaje genérico
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGeneral(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
