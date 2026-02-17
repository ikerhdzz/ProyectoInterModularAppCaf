package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.ProductoStock;
import com.cafeapp.backend.repositorio.ProductoStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoStockService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoStockService.class);

    private final ProductoStockRepository repo;

    public ProductoStockService(ProductoStockRepository repo) {
        this.repo = repo;
    }

    public List<ProductoStock> listarTodos() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            // Si la tabla no existe u ocurre un error de BD, devolvemos lista vacía
            logger.warn("No se pudo listar producto_stock (posible ausencia de tabla): {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public int obtenerStockPorProducto(Long productoId) {
        try {
            Optional<ProductoStock> opt = repo.findByProductoId(productoId);
            return opt.map(ProductoStock::getStock).orElse(0);
        } catch (Exception e) {
            logger.warn("Error obteniendo stock para producto {}: {}", productoId, e.getMessage());
            return 0;
        }
    }

    public ProductoStock setStock(Long productoId, int stock) {
        try {
            Optional<ProductoStock> opt = repo.findByProductoId(productoId);
            ProductoStock ps;
            if (opt.isPresent()) {
                ps = opt.get();
                ps.setStock(stock);
            } else {
                ps = new ProductoStock();
                ps.setProductoId(productoId);
                ps.setStock(stock);
            }
            return repo.save(ps);
        } catch (Exception e) {
            logger.error("No se pudo setear stock para producto {}: {}", productoId, e.getMessage());
            return null;
        }
    }
}
