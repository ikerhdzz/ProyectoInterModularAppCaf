package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Ticket;
import com.cafeapp.backend.repositorio.PedidoRepository;
import com.cafeapp.backend.repositorio.TicketRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Servicio encargado de gestionar los tickets asociados a pedidos.
 *
 * Funcionalidades:
 * - Crear ticket (si no existe)
 * - Obtener ticket por pedido
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoTotalsService pedidoTotalsService;

    /**
     * Constructor con inyección de dependencias.
     */
    public TicketService(
            TicketRepository ticketRepository,
            PedidoRepository pedidoRepository,
            PedidoTotalsService pedidoTotalsService
    ) {
        this.ticketRepository = ticketRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoTotalsService = pedidoTotalsService;
    }

    /**
     * Crea un ticket para un pedido si no existe.
     *
     * @param pedidoId ID del pedido
     * @return ticket creado o existente
     */
    public Ticket crearTicket(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return ticketRepository.findByPedidoId(pedidoId)
                .orElseGet(() -> {

                    Ticket ticket = new Ticket();

                    ticket.setPedido(pedido);
                    ticket.setNombreCentroCopia(pedido.getCentro().getNombre());
                    ticket.setNombreClienteCopia(pedido.getUsuario().getNombre());
                    ticket.setCursoClienteCopia(pedido.getUsuario().getClase());
                    ticket.setTipoTicket("CONSUMO");

                    // Fecha real de emisión
                    ticket.setFechaEmision(LocalDateTime.now());

                    // Total pagado usando precios históricos
                    double total = pedidoTotalsService.calcularTotales(pedidoId).totalFinal();
                    ticket.setTotalPagado(BigDecimal.valueOf(total));

                    return ticketRepository.save(ticket);
                });
    }

    /**
     * Obtiene un ticket asociado a un pedido.
     *
     * @param pedidoId ID del pedido
     * @return ticket encontrado
     */
    public Ticket obtenerPorPedido(Long pedidoId) {

        pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return ticketRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
    }
}
