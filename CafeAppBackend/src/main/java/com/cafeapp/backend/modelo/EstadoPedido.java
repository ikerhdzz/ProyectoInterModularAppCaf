package com.cafeapp.backend.modelo;

/**
 * Estados posibles de un pedido dentro del sistema.
 */
public enum EstadoPedido {
    PENDIENTE,
    PREPARANDO,
    LISTO,
    ENTREGADO,
    CANCELADO
}
