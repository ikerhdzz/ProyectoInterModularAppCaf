package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Categoria}.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca una categoría por nombre ignorando mayúsculas/minúsculas.
     */
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
