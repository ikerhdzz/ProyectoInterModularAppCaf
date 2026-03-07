package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link ItemCarrito}.
 */
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
}
