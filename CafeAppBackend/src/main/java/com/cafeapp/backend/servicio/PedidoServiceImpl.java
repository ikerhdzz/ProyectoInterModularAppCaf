package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.pedido.DetallePedidoResponse;
import com.cafeapp.backend.dto.pedido.PedidoFrontendRequest;
import com.cafeapp.backend.dto.pedido.PedidoTotales;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación principal de {@link PedidoService}.
 *
 * Esta clase actúa como fachada del módulo de pedidos,
 * delegando la lógica en servicios especializados:
 *
 * - PedidoCreationService → creación de pedidos
 * - PedidoDetailService → detalles de pedido
 * - PedidoTotalsService → totales
 * - PedidoStateService → cambio de estado
 * - PedidoTicketService → generación de tickets
 * - PedidoQueryService → consultas
 */
@Service
public class PedidoServiceImpl implements PedidoService {

    private final UsuarioService usuarioService;
    private final PedidoCreationService pedidoCreationService;
    private final PedidoDetailService pedidoDetailService;
    private final PedidoTotalsService pedidoTotalsService;
    private final PedidoStateService pedidoStateService;
    private final PedidoTicketService pedidoTicketService;
    private final PedidoQueryService pedidoQueryService;

    /**
     * Constructor con inyección de dependencias.
     */
    public PedidoServiceImpl(
            UsuarioService usuarioService,
            PedidoCreationService pedidoCreationService,
            PedidoDetailService pedidoDetailService,
            PedidoTotalsService pedidoTotalsService,
            PedidoStateService pedidoStateService,
            PedidoTicketService pedidoTicketService,
            PedidoQueryService pedidoQueryService
    ) {
        this.usuarioService = usuarioService;
        this.pedidoCreationService = pedidoCreationService;
        this.pedidoDetailService = pedidoDetailService;
        this.pedidoTotalsService = pedidoTotalsService;
        this.pedidoStateService = pedidoStateService;
        this.pedidoTicketService = pedidoTicketService;
        this.pedidoQueryService = pedidoQueryService;
    }

    /**
     * Obtiene el usuario autenticado.
     */
    private Usuario usuarioActual() {
        return usuarioService.obtenerUsuarioActual();
    }

    @Override
    public Pedido crearPedidoDesdeCarrito(Long turnoId) {
        Usuario usuario = usuarioActual();
        return pedidoCreationService.crearDesdeCarrito(usuario, turnoId);
    }

    @Override
    public Pedido crearPedidoDesdeFrontend(PedidoFrontendRequest request) {
        Usuario usuario = usuarioActual();
        return pedidoCreationService.crearDesdeFrontend(usuario, request);
    }

    @Override
    public List<Pedido> listarPedidosUsuario() {
        Usuario usuario = usuarioActual();
        return pedidoQueryService.listarPedidosUsuario(usuario.getId());
    }

    @Override
    public List<DetallePedidoResponse> obtenerDetallesPedido(Long pedidoId) {
        return pedidoDetailService.obtenerDetalles(pedidoId);
    }

    @Override
    public Pedido cambiarEstado(Long pedidoId, String nuevoEstado) {
        return pedidoStateService.cambiarEstado(pedidoId, nuevoEstado);
    }

    @Override
    public String generarTicket(Long pedidoId) {
        return pedidoTicketService.generarTicket(pedidoId);
    }

    @Override
    public PedidoTotales calcularTotalesPedido(Long pedidoId) {
        return pedidoTotalsService.calcularTotales(pedidoId);
    }

    @Override
    public List<Pedido> listarPedidosPorCentro(Long centroId) {
        return pedidoQueryService.listarPedidosPorCentro(centroId);
    }
}
