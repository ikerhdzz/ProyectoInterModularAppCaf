package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.DetallePedidoResponse;
import com.cafeapp.backend.dto.PedidoResponse;
import com.cafeapp.backend.modelo.DetallePedido;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Ticket;
import com.cafeapp.backend.repositorio.DetallePedidoRepository;
import com.cafeapp.backend.servicio.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoController(PedidoService pedidoService,
                            DetallePedidoRepository detallePedidoRepository) {
        this.pedidoService = pedidoService;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @PostMapping("/crear")
    public ResponseEntity<Pedido> crearPedido(@RequestParam Integer turnoId) {
        return ResponseEntity.ok(pedidoService.crearPedidoDesdeCarrito(turnoId));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidosUsuario());
    }

    @GetMapping("/{pedidoId}/detalles")
    public ResponseEntity<List<DetallePedidoResponse>> detalles(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.obtenerDetallesPedido(pedidoId));
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> obtenerPedidoCompleto(@PathVariable Long pedidoId) {

        Pedido pedido = pedidoService.listarPedidosUsuario()
                .stream()
                .filter(p -> p.getId().equals(pedidoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedidoResponse> detalles = pedidoService.obtenerDetallesPedido(pedidoId);

        double total = pedidoService.calcularTotalPedido(pedidoId);

        PedidoResponse response = new PedidoResponse(pedido, detalles, total);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{pedidoId}/estado")
    public ResponseEntity<Pedido> cambiarEstado(
            @PathVariable Long pedidoId,
            @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(pedidoId, estado));
    }

    @PostMapping("/{pedidoId}/ticket")
    public ResponseEntity<Ticket> generarTicket(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.generarTicket(pedidoId));
    }

    @PostMapping("/{detalleId}/extras")
    public ResponseEntity<?> agregarExtras(
            @PathVariable Long detalleId,
            @RequestBody List<Long> extrasIds) {

        DetallePedido detalle = detallePedidoRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        pedidoService.agregarExtras(detalle, extrasIds);

        return ResponseEntity.ok("Extras agregados");
    }
}
