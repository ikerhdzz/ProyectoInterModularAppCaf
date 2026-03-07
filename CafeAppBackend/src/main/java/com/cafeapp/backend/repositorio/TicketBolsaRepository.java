package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.TicketBolsa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link TicketBolsa}.
 */
public interface TicketBolsaRepository extends JpaRepository<TicketBolsa, Long> {
}
