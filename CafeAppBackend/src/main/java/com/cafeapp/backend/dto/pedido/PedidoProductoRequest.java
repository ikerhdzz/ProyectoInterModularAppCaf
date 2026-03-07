package com.cafeapp.backend.dto.pedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoProductoRequest(
        @NotNull Long productoId,
        @Positive int cantidad
) {}
