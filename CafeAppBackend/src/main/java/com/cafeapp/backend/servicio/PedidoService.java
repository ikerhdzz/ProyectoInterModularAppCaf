package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.pedido.DetallePedidoResponse;
import com.cafeapp.backend.dto.pedido.PedidoFrontendRequest;
import com.cafeapp.backend.dto.pedido.PedidoTotales;
import com.cafeapp.backend.modelo.Pedido;

import java.util.List;

/**
 * Interfaz principal del módulo de pedidos.
 *
 * Define todas las operaciones disponibles:
 * - Crear pedidos (carrito o frontend)
 * - Listar pedidos del usuario
 * - Obtener detalles
 * - Cambiar estado
 * - Generar ticket
 * - Calcular totales
 * - Listar pedidos por centro
 */
public interface PedidoService {

    Pedido crearPedidoDesdeCarrito(Long turnoId);

    Pedido crearPedidoDesdeFrontend(PedidoFrontendRequest request);

    List<Pedido> listarPedidosUsuario();

    List<DetallePedidoResponse> obtenerDetallesPedido(Long pedidoId);

    Pedido cambiarEstado(Long pedidoId, String nuevoEstado);

    String generarTicket(Long pedidoId);

    PedidoTotales calcularTotalesPedido(Long pedidoId);

    List<Pedido> listarPedidosPorCentro(Long centroId);
}
