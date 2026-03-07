package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Ticket}.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Busca un ticket por ID de pedido.
     */
    Optional<Ticket> findByPedidoId(Long pedidoId);

    /**
     * Busca un ticket por su número único.
     */
    Optional<Ticket> findByNumeroTicket(String numeroTicket);
}
