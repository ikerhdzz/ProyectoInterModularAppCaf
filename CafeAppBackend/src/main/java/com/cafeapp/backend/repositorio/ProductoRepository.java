package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad {@link Producto}.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Obtiene productos filtrados por categoría.
     */
    List<Producto> findByCategoriaId(Long categoriaId);
}
