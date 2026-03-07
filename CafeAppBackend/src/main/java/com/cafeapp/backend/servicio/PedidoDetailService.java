package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.pedido.DetalleExtraResponse;
import com.cafeapp.backend.dto.pedido.DetallePedidoResponse;
import com.cafeapp.backend.modelo.DetalleExtra;
import com.cafeapp.backend.modelo.DetallePedido;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.repositorio.DetalleExtraRepository;
import com.cafeapp.backend.repositorio.DetallePedidoRepository;
import com.cafeapp.backend.repositorio.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de obtener los detalles completos de un pedido:
 *
 * - Productos
 * - Cantidades
 * - Notas de personalización
 * - Extras asociados
 * - Subtotales
 */
@Service
public class PedidoDetailService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final DetalleExtraRepository detalleExtraRepository;

    /**
     * Constructor con inyección de dependencias.
     */
    public PedidoDetailService(
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            DetalleExtraRepository detalleExtraRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.detalleExtraRepository = detalleExtraRepository;
    }

    /**
     * Obtiene todos los detalles de un pedido y los transforma a DTOs.
     *
     * @param pedidoId ID del pedido
     * @return lista de detalles en formato DTO
     */
    public List<DetallePedidoResponse> obtenerDetalles(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);

        return detalles.stream().map(detalle -> {

            List<DetalleExtra> extras = detalleExtraRepository.findByDetallePedido(detalle);

            List<DetalleExtraResponse> extrasDto = extras.stream()
                    .map(e -> new DetalleExtraResponse(
                            e.getExtra().getId(),
                            e.getExtra().getNombre(),
                            e.getExtra().getPrecio(),
                            e.getExtra().getPrecio()
                    ))
                    .toList();

            double subtotal = calcularSubtotal(detalle, extras);

            return new DetallePedidoResponse(
                    detalle.getId(),
                    detalle.getProducto().getId(),
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getProducto().getPrecioBase(),
                    detalle.getProducto().getPrecioBase(),
                    detalle.getNotasPersonalizacion(),
                    extrasDto,
                    subtotal
            );

        }).toList();
    }

    /**
     * Calcula el subtotal de un detalle considerando:
     * - Precio del producto
     * - Precio de los extras
     * - Cantidad
     *
     * @param detalle detalle del pedido
     * @param extras extras asociados
     * @return subtotal total
     */
    public double calcularSubtotal(DetallePedido detalle, List<DetalleExtra> extras) {

        double subtotalProducto =
                detalle.getProducto().getPrecioBase() * detalle.getCantidad();

        double subtotalExtras =
                extras.stream()
                        .mapToDouble(e -> e.getExtra().getPrecio())
                        .sum() * detalle.getCantidad();

        return subtotalProducto + subtotalExtras;
    }
}
