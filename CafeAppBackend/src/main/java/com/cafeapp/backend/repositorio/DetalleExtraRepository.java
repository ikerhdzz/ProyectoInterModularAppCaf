package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.DetalleExtra;
import com.cafeapp.backend.modelo.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad {@link DetalleExtra}.
 *
 * Permite obtener los extras asociados a un detalle de pedido.
 */
public interface DetalleExtraRepository extends JpaRepository<DetalleExtra, Long> {

    /**
     * Obtiene todos los extras asociados a un detalle de pedido.
     */
    List<DetalleExtra> findByDetallePedido(DetallePedido detallePedido);
}
