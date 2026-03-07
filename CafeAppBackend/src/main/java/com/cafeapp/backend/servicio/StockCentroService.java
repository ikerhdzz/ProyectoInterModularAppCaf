package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.stock.StockCentroRequest;
import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.modelo.StockCentro;
import com.cafeapp.backend.repositorio.CentroRepository;
import com.cafeapp.backend.repositorio.ProductoRepository;
import com.cafeapp.backend.repositorio.StockCentroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar el stock de productos por centro.
 *
 * Funcionalidades:
 * - Listar stock
 * - Obtener stock por ID
 * - Obtener stock por centro
 * - Obtener stock por centro y producto
 * - Crear registros de stock
 * - Actualizar stock
 * - Eliminar stock
 * - Restar stock (usado por pedidos)
 */
@Service
public class StockCentroService {

    private final StockCentroRepository stockRepo;
    private final CentroRepository centroRepo;
    private final ProductoRepository productoRepo;

    /**
     * Constructor con inyección de dependencias.
     */
    public StockCentroService(
            StockCentroRepository stockRepo,
            CentroRepository centroRepo,
            ProductoRepository productoRepo
    ) {
        this.stockRepo = stockRepo;
        this.centroRepo = centroRepo;
        this.productoRepo = productoRepo;
    }

    // ============================================================
    // LISTAR / OBTENER
    // ============================================================

    /**
     * Lista todos los registros de stock.
     */
    public List<StockCentro> listar() {
        return stockRepo.findAll();
    }

    /**
     * Obtiene un registro de stock por ID.
     */
    public StockCentro obtener(Long id) {
        return stockRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado"));
    }

    /**
     * Lista el stock de un centro específico.
     */
    public List<StockCentro> listarPorCentro(Long centroId) {
        return stockRepo.findByCentroId(centroId);
    }

    /**
     * Obtiene el stock de un producto en un centro.
     */
    public StockCentro obtenerPorCentroYProducto(Long centroId, Long productoId) {
        return stockRepo.findByCentroIdAndProductoId(centroId, productoId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado para ese centro y producto"));
    }

    // ============================================================
    // CREAR
    // ============================================================

    /**
     * Crea un registro de stock para un centro y producto.
     *
     * @throws RuntimeException si ya existe un registro para esa combinación
     */
    public StockCentro crear(StockCentroRequest request) {

        stockRepo.findByCentroIdAndProductoId(request.centroId(), request.productoId())
                .ifPresent(s -> {
                    throw new RuntimeException("Ya existe un registro de stock para este centro y producto");
                });

        StockCentro stock = new StockCentro();
        mapearDatos(stock, request);
        return stockRepo.save(stock);
    }

    // ============================================================
    // ACTUALIZAR
    // ============================================================

    /**
     * Actualiza un registro de stock existente.
     */
    public StockCentro actualizar(Long id, StockCentroRequest request) {
        StockCentro stock = obtener(id);
        mapearDatos(stock, request);
        return stockRepo.save(stock);
    }

    // ============================================================
    // ELIMINAR
    // ============================================================

    /**
     * Elimina un registro de stock por ID.
     */
    public void eliminar(Long id) {
        if (!stockRepo.existsById(id)) {
            throw new RuntimeException("Stock no encontrado");
        }
        stockRepo.deleteById(id);
    }

    // ============================================================
    // RESTAR STOCK (USADO POR PEDIDOS)
    // ============================================================

    /**
     * Resta stock de un producto en un centro.
     *
     * @throws RuntimeException si no existe stock o si es insuficiente
     */
    public void restarStock(Long centroId, Long productoId, Integer cantidad) {

        StockCentro stock = stockRepo.findByCentroIdAndProductoId(centroId, productoId)
                .orElseThrow(() -> new RuntimeException(
                        "No existe stock para el producto " + productoId + " en el centro " + centroId
                ));

        int nuevoStock = stock.getStockActual() - cantidad;

        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente para el producto " + productoId);
        }

        stock.setStockActual(nuevoStock);
        stockRepo.save(stock);
    }

    // ============================================================
    // MÉTODO AUXILIAR
    // ============================================================

    /**
     * Mapea los datos del DTO al modelo StockCentro.
     */
    private void mapearDatos(StockCentro stock, StockCentroRequest request) {

        Centro centro = centroRepo.findById(request.centroId())
                .orElseThrow(() -> new RuntimeException("Centro no encontrado"));

        Producto producto = productoRepo.findById(request.productoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        stock.setCentro(centro);
        stock.setProducto(producto);
        stock.setStockActual(request.stockActual());
        stock.setAlertaStock(request.alertaStock());
    }
}
