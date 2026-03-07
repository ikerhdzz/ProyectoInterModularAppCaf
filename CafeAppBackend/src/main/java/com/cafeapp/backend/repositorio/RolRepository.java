package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Rol}.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {
}
