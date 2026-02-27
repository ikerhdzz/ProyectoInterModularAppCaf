package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.DetallePedidoResponse;
import com.cafeapp.backend.dto.PedidoFrontendRequest;
import com.cafeapp.backend.dto.PedidoTotales;
import com.cafeapp.backend.modelo.DetallePedido;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Ticket;

import java.util.List;

public interface PedidoService {

    Pedido crearPedidoDesdeCarrito(Integer turnoId);

    Pedido crearPedidoDesdeFrontend(PedidoFrontendRequest request);

    List<Pedido> listarPedidosUsuario();

    List<DetallePedidoResponse> obtenerDetallesPedido(Long pedidoId);

    Pedido cambiarEstado(Long pedidoId, String nuevoEstado);

    Ticket generarTicket(Long pedidoId);

    double calcularTotalPedido(Long pedidoId);

    PedidoTotales calcularTotalesPedido(Long pedidoId);

    void expirarPedidos();

    List<Pedido> listarPedidosPorCentro(Integer centroId);

    void agregarExtras(DetallePedido detalle, List<Long> extrasIds);
}
