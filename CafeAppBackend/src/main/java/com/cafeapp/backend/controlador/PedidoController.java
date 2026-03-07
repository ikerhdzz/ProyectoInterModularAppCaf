package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.pedido.DetallePedidoResponse;
import com.cafeapp.backend.dto.pedido.PedidoFrontendRequest;
import com.cafeapp.backend.dto.pedido.PedidoTotales;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.servicio.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Crear pedido desde carrito del usuario autenticado")
    @PostMapping("/carrito/{turnoId}")
    public ResponseEntity<Pedido> crearDesdeCarrito(@PathVariable Long turnoId) {
        return ResponseEntity.ok(pedidoService.crearPedidoDesdeCarrito(turnoId));
    }

    @Operation(summary = "Crear pedido desde el frontend (usuario autenticado)")
    @PostMapping("/frontend")
    public ResponseEntity<Pedido> crearDesdeFrontend(
            @Valid @RequestBody PedidoFrontendRequest request
    ) {
        return ResponseEntity.ok(pedidoService.crearPedidoDesdeFrontend(request));
    }

    @Operation(summary = "Listar pedidos del usuario autenticado")
    @GetMapping("/mis")
    public ResponseEntity<List<Pedido>> listarPedidosUsuario() {
        return ResponseEntity.ok(pedidoService.listarPedidosUsuario());
    }

    @Operation(summary = "Obtener detalles de un pedido")
    @GetMapping("/{pedidoId}/detalles")
    public ResponseEntity<List<DetallePedidoResponse>> obtenerDetalles(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.obtenerDetallesPedido(pedidoId));
    }

    @Operation(summary = "Cambiar estado del pedido")
    @PatchMapping("/{pedidoId}/estado")
    public ResponseEntity<Pedido> cambiarEstado(
            @PathVariable Long pedidoId,
            @RequestParam String estado
    ) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(pedidoId, estado));
    }

    @Operation(summary = "Generar ticket del pedido")
    @GetMapping("/{pedidoId}/ticket")
    public ResponseEntity<String> generarTicket(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.generarTicket(pedidoId));
    }

    @Operation(summary = "Calcular totales del pedido")
    @GetMapping("/{pedidoId}/totales")
    public ResponseEntity<PedidoTotales> calcularTotales(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.calcularTotalesPedido(pedidoId));
    }

    @Operation(summary = "Listar pedidos por centro")
    @GetMapping("/centro/{centroId}")
    public ResponseEntity<List<Pedido>> listarPorCentro(@PathVariable Long centroId) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorCentro(centroId));
    }
}
