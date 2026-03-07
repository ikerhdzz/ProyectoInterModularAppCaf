package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Carrito;
import com.cafeapp.backend.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Carrito}.
 *
 * Permite obtener el carrito asociado a un usuario.
 */
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    /**
     * Busca el carrito asociado a un usuario.
     *
     * @param usuario Usuario propietario del carrito
     * @return Optional con el carrito si existe
     */
    Optional<Carrito> findByUsuario(Usuario usuario);
}
