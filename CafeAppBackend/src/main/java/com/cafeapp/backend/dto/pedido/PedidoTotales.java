package com.cafeapp.backend.dto.pedido;

public record PedidoTotales(
        Long pedidoId,
        Double subtotalProductos,
        Double subtotalExtras,
        Double totalFinal
) {}
