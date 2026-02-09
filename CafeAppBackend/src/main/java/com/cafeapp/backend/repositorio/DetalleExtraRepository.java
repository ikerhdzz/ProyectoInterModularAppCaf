package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.DetalleExtra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleExtraRepository extends JpaRepository<DetalleExtra, Long> {
    List<DetalleExtra> findByIdDetalle(Long idDetalle);
}
