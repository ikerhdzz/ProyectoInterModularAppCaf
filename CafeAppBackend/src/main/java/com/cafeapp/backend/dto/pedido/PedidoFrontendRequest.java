package com.cafeapp.backend.dto.pedido;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoFrontendRequest(
        @NotNull Long turnoId,
        @NotNull Long beneficiarioId,
        @NotNull List<ItemPedidoFrontend> items
) {

    public record ItemPedidoFrontend(
            @NotNull Long productoId,
            @NotNull Integer cantidad,
            String notasPersonalizacion,
            List<Long> extras
    ) {}
}
