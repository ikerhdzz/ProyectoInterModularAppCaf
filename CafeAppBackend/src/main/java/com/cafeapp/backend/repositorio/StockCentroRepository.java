package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.StockCentro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockCentroRepository extends JpaRepository<StockCentro, Integer> {

    Optional<StockCentro> findByCentroIdAndProductoId(Integer centroId, Long productoId);

    List<StockCentro> findByCentroId(Integer centroId);
}
