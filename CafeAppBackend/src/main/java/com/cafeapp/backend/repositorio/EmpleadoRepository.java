package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List; public interface EmpleadoRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.rol.id = 2 AND u.centro.id = :centroId")
    List<Usuario> findEmpleadosByCentro(Integer centroId);

}
