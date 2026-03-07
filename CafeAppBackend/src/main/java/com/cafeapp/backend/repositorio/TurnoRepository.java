package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Turno}.
 */
public interface TurnoRepository extends JpaRepository<Turno, Long> {
}
