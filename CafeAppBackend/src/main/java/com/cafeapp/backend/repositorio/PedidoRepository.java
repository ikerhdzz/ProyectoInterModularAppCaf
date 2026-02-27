package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Integer usuarioId);

    List<Pedido> findByCentroIdAndEstadoIn(Integer centroId, List<EstadoPedido> estados);
}

