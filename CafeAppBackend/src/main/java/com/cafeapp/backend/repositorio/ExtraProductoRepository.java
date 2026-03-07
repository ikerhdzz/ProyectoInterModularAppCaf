package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.ExtraProducto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link ExtraProducto}.
 */
public interface ExtraProductoRepository extends JpaRepository<ExtraProducto, Long> {
}
