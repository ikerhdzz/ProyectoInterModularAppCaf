package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.DetallePedido;
import com.cafeapp.backend.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedido(Pedido pedido);
}

