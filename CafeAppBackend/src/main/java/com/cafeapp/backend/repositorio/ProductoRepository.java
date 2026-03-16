package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para la entidad {@link Producto}.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Obtiene productos por categoría trayendo los alérgenos 
     * en una sola consulta (Evita el error de lentitud N+1).
     */
    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.alergenos WHERE p.categoria.id = :categoriaId")
    List<Producto> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Para cuando pides TODOS los productos (el listado general).
     */
    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.alergenos")
    List<Producto> findAllWithAlergenos();
}
