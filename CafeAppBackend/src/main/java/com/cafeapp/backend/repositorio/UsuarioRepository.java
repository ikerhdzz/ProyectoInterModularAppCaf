package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link Usuario}.
 *
 * Incluye consultas por email, rol, centro y curso.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRolNombreIgnoreCase(String rol);

    List<Usuario> findByCentroId(Long centroId);

    List<Usuario> findByCursoId(Long cursoId);

    List<Usuario> findByRolIdAndCentroId(Long rolId, Long centroId);
}
