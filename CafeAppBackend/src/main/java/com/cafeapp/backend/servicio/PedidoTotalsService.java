package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.pedido.DetallePedidoResponse;
import com.cafeapp.backend.dto.pedido.PedidoTotales;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.repositorio.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de calcular los totales de un pedido:
 *
 * - Subtotal de productos
 * - Subtotal de extras
 * - Total final
 *
 * Utiliza los DTOs generados por PedidoDetailService.
 */
@Service
public class PedidoTotalsService {

    private final PedidoRepository pedidoRepository;
    private final PedidoDetailService pedidoDetailService;

    /**
     * Constructor con inyección de dependencias.
     */
    public PedidoTotalsService(
            PedidoRepository pedidoRepository,
            PedidoDetailService pedidoDetailService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoDetailService = pedidoDetailService;
    }

    /**
     * Calcula los totales de un pedido.
     *
     * @param pedidoId ID del pedido
     * @return objeto PedidoTotales con todos los importes
     */
    public PedidoTotales calcularTotales(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedidoResponse> detalles = pedidoDetailService.obtenerDetalles(pedidoId);

        double subtotalProductos = detalles.stream()
                .mapToDouble(d -> d.precioUnitarioProducto() * d.cantidad())
                .sum();

        double subtotalExtras = detalles.stream()
                .flatMap(d -> d.extras() != null ? d.extras().stream() : java.util.stream.Stream.empty())
                .mapToDouble(e -> e.precioUnitarioExtra())
                .sum();

        double totalFinal = subtotalProductos + subtotalExtras;

        return new PedidoTotales(
                pedidoId,
                subtotalProductos,
                subtotalExtras,
                totalFinal
        );
    }
}
