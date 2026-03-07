package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Alergeno;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar operaciones CRUD sobre la entidad {@link Alergeno}.
 *
 * Tabla relacionada: alergeno
 */
public interface AlergenoRepository extends JpaRepository<Alergeno, Long> {
}
