package com.cafeapp.backend.dto.pedido;

import java.util.List;

public record DetallePedidoResponse(
        Long id,
        Long productoId,
        String nombreProducto,
        Integer cantidad,
        Double precioUnitarioProducto,
        Double precioPagadoUnidad,
        String notasPersonalizacion,
        List<DetalleExtraResponse> extras,
        Double subtotal
) {}
