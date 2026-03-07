package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.ProductoAlergeno;
import com.cafeapp.backend.modelo.ProductoAlergenoId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link ProductoAlergeno}.
 */
public interface ProductoAlergenoRepository extends JpaRepository<ProductoAlergeno, ProductoAlergenoId> {
}
