package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Curso}.
 */
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
