package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.DetallePedido;
import com.cafeapp.backend.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad {@link DetallePedido}.
 */
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    /**
     * Obtiene todos los detalles asociados a un pedido.
     */
    List<DetallePedido> findByPedido(Pedido pedido);
}
