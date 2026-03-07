package com.cafeapp.backend.dto.pedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String usuario,
        String centro,
        String estado,
        LocalDateTime fecha,
        List<DetallePedidoResponse> detalles,
        double total
) {}
