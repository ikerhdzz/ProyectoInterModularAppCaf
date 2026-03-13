package com.cafeapp.backend.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafeapp.backend.modelo.Rol;

/**
 * Repositorio para la entidad {@link Rol}.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    /**
     * Busca un rol por su nombre.
     */
    Optional<Rol> findByNombre(String nombre);
}
