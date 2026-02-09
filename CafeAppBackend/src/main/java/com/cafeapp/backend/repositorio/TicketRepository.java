package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Ticket;
import com.cafeapp.backend.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByPedido(Pedido pedido);
}
