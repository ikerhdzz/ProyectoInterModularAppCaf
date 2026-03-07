package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.StockCentro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link StockCentro}.
 */
public interface StockCentroRepository extends JpaRepository<StockCentro, Long> {

    /**
     * Obtiene todos los registros de stock de un centro.
     */
    List<StockCentro> findByCentroId(Long centroId);

    /**
     * Obtiene el stock de un producto en un centro específico.
     */
    Optional<StockCentro> findByCentroIdAndProductoId(Long centroId, Long productoId);
}
