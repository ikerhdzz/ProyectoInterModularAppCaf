package com.cafeapp.backend.dto.pedido;

public record DetalleExtraResponse(
        Long extraId,
        String nombreExtra,
        Double precioUnitarioExtra,
        Double precioPagadoExtra
) {}
