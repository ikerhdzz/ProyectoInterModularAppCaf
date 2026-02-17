package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.ProductoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductoStockRepository extends JpaRepository<ProductoStock, Long> {
    Optional<ProductoStock> findByProductoId(Long productoId);
}
