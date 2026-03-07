package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.stock.StockCentroRequest;
import com.cafeapp.backend.dto.stock.StockCentroResponse;
import com.cafeapp.backend.modelo.StockCentro;
import com.cafeapp.backend.servicio.StockCentroService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del stock por centro.
 */
@RestController
@RequestMapping("/stock-centro")
public class StockCentroController {

    private final StockCentroService stockService;

    public StockCentroController(StockCentroService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Listar stock de todos los centros")
    @GetMapping
    public ResponseEntity<List<StockCentroResponse>> listar() {
        List<StockCentroResponse> lista = stockService.listar()
                .stream()
                .map(this::convertir)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener stock por ID")
    @GetMapping("/{id}")
    public ResponseEntity<StockCentroResponse> obtener(@PathVariable Long id) {
        StockCentro stock = stockService.obtener(id);
        return ResponseEntity.ok(convertir(stock));
    }

    @Operation(summary = "Crear registro de stock")
    @PostMapping
    public ResponseEntity<StockCentroResponse> crear(@Valid @RequestBody StockCentroRequest request) {
        StockCentro creado = stockService.crear(request);
        return ResponseEntity.ok(convertir(creado));
    }

    @Operation(summary = "Actualizar stock")
    @PutMapping("/{id}")
    public ResponseEntity<StockCentroResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody StockCentroRequest request
    ) {
        StockCentro actualizado = stockService.actualizar(id, request);
        return ResponseEntity.ok(convertir(actualizado));
    }

    @Operation(summary = "Eliminar registro de stock")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        stockService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private StockCentroResponse convertir(StockCentro s) {
        return new StockCentroResponse(
                s.getId(),
                s.getCentro().getNombre(),
                s.getProducto().getNombre(),
                s.getStockActual(),
                s.getAlertaStock()
        );
    }
}
