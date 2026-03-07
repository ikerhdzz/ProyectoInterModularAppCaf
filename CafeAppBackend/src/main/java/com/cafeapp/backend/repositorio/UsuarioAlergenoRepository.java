package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.UsuarioAlergeno;
import com.cafeapp.backend.modelo.UsuarioAlergenoId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link UsuarioAlergeno}.
 */
public interface UsuarioAlergenoRepository extends JpaRepository<UsuarioAlergeno, UsuarioAlergenoId> {
}
