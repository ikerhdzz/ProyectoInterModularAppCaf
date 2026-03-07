package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.pedido.DetallePedidoResponse;
import com.cafeapp.backend.dto.pedido.PedidoTotales;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Ticket;
import com.cafeapp.backend.repositorio.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de generar el ticket en formato texto para un pedido.
 *
 * Funcionalidades:
 * - Crear ticket en BD (delegado a TicketService)
 * - Obtener detalles del pedido
 * - Obtener totales del pedido
 * - Construir un ticket legible en formato texto
 */
@Service
public class PedidoTicketService {

    private final PedidoRepository pedidoRepository;
    private final PedidoDetailService pedidoDetailService;
    private final PedidoTotalsService pedidoTotalsService;
    private final TicketService ticketService;

    /**
     * Constructor con inyección de dependencias.
     */
    public PedidoTicketService(
            PedidoRepository pedidoRepository,
            PedidoDetailService pedidoDetailService,
            PedidoTotalsService pedidoTotalsService,
            TicketService ticketService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoDetailService = pedidoDetailService;
        this.pedidoTotalsService = pedidoTotalsService;
        this.ticketService = ticketService;
    }

    /**
     * Genera un ticket en formato texto para un pedido.
     *
     * Pasos:
     * 1. Crear ticket en BD (TicketService)
     * 2. Obtener detalles del pedido
     * 3. Obtener totales
     * 4. Construir texto del ticket
     *
     * @param pedidoId ID del pedido
     * @return ticket en formato String
     */
    public String generarTicket(Long pedidoId) {

        Ticket ticketBD = ticketService.crearTicket(pedidoId);

        Pedido pedido = ticketBD.getPedido();
        List<DetallePedidoResponse> detalles = pedidoDetailService.obtenerDetalles(pedidoId);
        PedidoTotales totales = pedidoTotalsService.calcularTotales(pedidoId);

        StringBuilder ticket = new StringBuilder();

        ticket.append("=========== TICKET ===========\n");
        ticket.append("Ticket Nº: ").append(ticketBD.getNumeroTicket()).append("\n");
        ticket.append("Pedido Nº: ").append(pedido.getId()).append("\n");
        ticket.append("Fecha emisión: ").append(ticketBD.getFechaEmision()).append("\n");
        ticket.append("Centro: ").append(ticketBD.getNombreCentroCopia()).append("\n");
        ticket.append("Cliente: ").append(ticketBD.getNombreClienteCopia()).append("\n");
        ticket.append("----------------------------------------\n");

        for (DetallePedidoResponse d : detalles) {

            ticket.append(d.cantidad())
                    .append("x ")
                    .append(d.nombreProducto())
                    .append(" (")
                    .append(String.format("%.2f", d.precioPagadoUnidad()))
                    .append("€)\n");

            if (d.extras() != null && !d.extras().isEmpty()) {
                d.extras().forEach(extra -> {
                    ticket.append("   + ")
                            .append(extra.nombreExtra())
                            .append(" (")
                            .append(String.format("%.2f", extra.precioPagadoExtra()))
                            .append("€)\n");
                });
            }

            ticket.append("   Subtotal: ")
                    .append(String.format("%.2f", d.subtotal()))
                    .append("€\n\n");
        }

        ticket.append("----------------------------------------\n");
        ticket.append("Subtotal productos: ")
                .append(String.format("%.2f", totales.subtotalProductos()))
                .append("€\n");
        ticket.append("Subtotal extras: ")
                .append(String.format("%.2f", totales.subtotalExtras()))
                .append("€\n");
        ticket.append("TOTAL: ")
                .append(String.format("%.2f", totales.totalFinal()))
                .append("€\n");
        ticket.append("========================================\n");

        return ticket.toString();
    }
}
