package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
}
