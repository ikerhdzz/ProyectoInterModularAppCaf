package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.EstadoPedido;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad {@link Pedido}.
 *
 * Incluye consultas ordenadas por fecha.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);

    List<Pedido> findByCentroIdOrderByFechaDesc(Long centroId);

    List<Pedido> findByEstadoOrderByFechaDesc(EstadoPedido estado);

    List<Pedido> findByTurnoIdOrderByFechaDesc(Long turnoId);

    List<Pedido> findByUsuarioAndEstadoOrderByFechaDesc(Usuario usuario, EstadoPedido estado);
}
