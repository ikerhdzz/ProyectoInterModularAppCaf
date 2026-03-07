package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Centro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Centro}.
 */
public interface CentroRepository extends JpaRepository<Centro, Long> {
}
