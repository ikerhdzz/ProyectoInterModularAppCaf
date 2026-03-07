package com.cafeapp.backend.dto.pedido;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequest(
        @NotNull Long usuarioId,
        @NotNull Long turnoId,
        @NotNull List<PedidoProductoRequest> productos
) {}
