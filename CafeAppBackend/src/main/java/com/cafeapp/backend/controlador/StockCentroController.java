package com.cafeapp.backend.controlador;

import com.cafeapp.backend.modelo.StockCentro;
import com.cafeapp.backend.servicio.StockCentroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockCentroController {

    private final StockCentroService stockService;

    public StockCentroController(StockCentroService stockService) {
        this.stockService = stockService;
    }

    // ============================================================
    // LISTAR STOCK DE UN CENTRO
    // ============================================================
    @GetMapping("/centro/{centroId}")
    public List<StockCentro> obtenerStockCentro(@PathVariable Integer centroId) {
        return stockService.obtenerStockCentro(centroId);
    }

    // ============================================================
    // OBTENER STOCK DE UN PRODUCTO
    // ============================================================
    @GetMapping("/{centroId}/{productoId}")
    public StockCentro obtenerStockProducto(
            @PathVariable Integer centroId,
            @PathVariable Long productoId
    ) {
        return stockService.obtenerStockProducto(centroId, productoId);
    }

    // ============================================================
    // ACTUALIZAR STOCK
    // ============================================================
    @PutMapping("/{centroId}/{productoId}")
    public StockCentro actualizarStock(
            @PathVariable Integer centroId,
            @PathVariable Long productoId,
            @RequestParam int stock
    ) {
        return stockService.actualizarStock(centroId, productoId, stock);
    }

    // ============================================================
    // CREAR STOCK INICIAL
    // ============================================================
    @PostMapping("/{centroId}/{productoId}")
    public StockCentro crearStock(
            @PathVariable Integer centroId,
            @PathVariable Long productoId,
            @RequestParam int stockInicial,
            @RequestParam int alerta
    ) {
        return stockService.crearStock(centroId, productoId, stockInicial, alerta);
    }
}
