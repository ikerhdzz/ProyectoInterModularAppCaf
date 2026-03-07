package com.cafeapp.backend.dto.stock;

/**
 * DTO de respuesta para stock por centro.
 */
public record StockCentroResponse(
        Long id,
        String centro,
        String producto,
        int stockActual,
        int alertaStock
) {}
