package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.modelo.StockCentro;
import com.cafeapp.backend.repositorio.CentroRepository;
import com.cafeapp.backend.repositorio.ProductoRepository;
import com.cafeapp.backend.repositorio.StockCentroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockCentroService {

    private final StockCentroRepository stockCentroRepository;
    private final CentroRepository centroRepository;
    private final ProductoRepository productoRepository;

    public StockCentroService(
            StockCentroRepository stockCentroRepository,
            CentroRepository centroRepository,
            ProductoRepository productoRepository
    ) {
        this.stockCentroRepository = stockCentroRepository;
        this.centroRepository = centroRepository;
        this.productoRepository = productoRepository;
    }

    // ============================================================
    // OBTENER TODO EL STOCK DE UN CENTRO
    // ============================================================
    public List<StockCentro> obtenerStockCentro(Integer centroId) {
        return stockCentroRepository.findByCentroId(centroId);
    }

    // ============================================================
    // OBTENER STOCK DE UN PRODUCTO EN UN CENTRO
    // ============================================================
    public StockCentro obtenerStockProducto(Integer centroId, Long productoId) {
        return stockCentroRepository.findByCentroIdAndProductoId(centroId, productoId)
                .orElseThrow(() -> new RuntimeException("No existe stock configurado"));
    }

    // ============================================================
    // ACTUALIZAR STOCK (SET ABSOLUTO)
    // ============================================================
    public StockCentro actualizarStock(Integer centroId, Long productoId, int nuevoStock) {

        StockCentro stock = obtenerStockProducto(centroId, productoId);
        stock.setStockActual(nuevoStock);

        return stockCentroRepository.save(stock);
    }

    // ============================================================
    // CREAR STOCK INICIAL
    // ============================================================
    public StockCentro crearStock(Integer centroId, Long productoId, int stockInicial, int alerta) {

        Centro centro = centroRepository.findById(centroId)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        StockCentro stock = new StockCentro(centro, producto, stockInicial, alerta);

        return stockCentroRepository.save(stock);
    }

    // ============================================================
    // AUMENTAR STOCK
    // ============================================================
    public StockCentro aumentarStock(Integer centroId, Long productoId, int cantidad) {

        StockCentro stock = obtenerStockProducto(centroId, productoId);
        stock.setStockActual(stock.getStockActual() + cantidad);

        return stockCentroRepository.save(stock);
    }

    // ============================================================
    // RESTAR STOCK (VALIDANDO DISPONIBILIDAD)
    // ============================================================
    public StockCentro restarStock(Integer centroId, Long productoId, int cantidad) {

        StockCentro stock = obtenerStockProducto(centroId, productoId);

        if (stock.getStockActual() < cantidad) {
            throw new RuntimeException(
                    "Stock insuficiente para producto " + productoId +
                            " en centro " + centroId +
                            ". Disponible: " + stock.getStockActual() +
                            ", solicitado: " + cantidad
            );
        }

        stock.setStockActual(stock.getStockActual() - cantidad);

        return stockCentroRepository.save(stock);
    }
}
