package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRolIdAndCentroId(Integer rolId, Integer centroId);
}
