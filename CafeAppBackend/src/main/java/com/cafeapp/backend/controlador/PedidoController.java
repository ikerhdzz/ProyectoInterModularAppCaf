package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.DetallePedidoResponse;
import com.cafeapp.backend.dto.PedidoFrontendRequest;
import com.cafeapp.backend.dto.PedidoTotales;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.servicio.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // ============================================================
    // CREAR PEDIDO DESDE CARRITO
    // ============================================================
    @PostMapping("/carrito")
    public Pedido crearPedidoDesdeCarrito(@RequestParam Integer turnoId) {
        return pedidoService.crearPedidoDesdeCarrito(turnoId);
    }

    // ============================================================
    // CREAR PEDIDO DESDE FRONTEND DIRECTO
    // ============================================================
    @PostMapping("/frontend")
    public Pedido crearPedidoDesdeFrontend(@RequestBody PedidoFrontendRequest request) {
        return pedidoService.crearPedidoDesdeFrontend(request);
    }

    // ============================================================
    // LISTAR PEDIDOS DEL USUARIO
    // ============================================================
    @GetMapping("/usuario")
    public List<Pedido> listarPedidosUsuario() {
        return pedidoService.listarPedidosUsuario();
    }

    // ============================================================
    // DETALLES DE UN PEDIDO
    // ============================================================
    @GetMapping("/{pedidoId}/detalles")
    public List<DetallePedidoResponse> obtenerDetalles(@PathVariable Long pedidoId) {
        return pedidoService.obtenerDetallesPedido(pedidoId);
    }

    // ============================================================
    // CAMBIAR ESTADO
    // ============================================================
    @PutMapping("/{pedidoId}/estado")
    public Pedido cambiarEstado(
            @PathVariable Long pedidoId,
            @RequestParam String estado
    ) {
        return pedidoService.cambiarEstado(pedidoId, estado);
    }

    // ============================================================
    // GENERAR TICKET
    // ============================================================
    @PostMapping("/{pedidoId}/ticket")
    public void generarTicket(@PathVariable Long pedidoId) {
        pedidoService.generarTicket(pedidoId);
    }

    // ============================================================
    // TOTALES
    // ============================================================
    @GetMapping("/{pedidoId}/totales")
    public PedidoTotales calcularTotales(@PathVariable Long pedidoId) {
        return pedidoService.calcularTotalesPedido(pedidoId);
    }

    // ============================================================
    // LISTAR PEDIDOS POR CENTRO
    // ============================================================
    @GetMapping("/centro/{centroId}")
    public List<Pedido> listarPedidosPorCentro(@PathVariable Integer centroId) {
        return pedidoService.listarPedidosPorCentro(centroId);
    }
}
