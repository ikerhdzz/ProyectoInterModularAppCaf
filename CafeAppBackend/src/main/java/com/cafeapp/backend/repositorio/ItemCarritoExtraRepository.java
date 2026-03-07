package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.ItemCarritoExtra;
import com.cafeapp.backend.modelo.ItemCarritoExtraId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link ItemCarritoExtra}.
 *
 * Usa clave primaria compuesta {@link ItemCarritoExtraId}.
 */
public interface ItemCarritoExtraRepository extends JpaRepository<ItemCarritoExtra, ItemCarritoExtraId> {
}
