package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.DetalleExtra;
import com.cafeapp.backend.modelo.DetalleExtraId;
import com.cafeapp.backend.modelo.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleExtraRepository extends JpaRepository<DetalleExtra, DetalleExtraId> {
    List<DetalleExtra> findByDetalle(DetallePedido detalle);
}

