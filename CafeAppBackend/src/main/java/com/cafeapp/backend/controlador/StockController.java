package com.cafeapp.backend.controlador;

import com.cafeapp.backend.servicio.ProductoStockService;
import com.cafeapp.backend.modelo.ProductoStock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class StockController {

    private final ProductoStockService stockService;

    public StockController(ProductoStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stock")
    public List<Map<String, Object>> listarStocks() {
        List<ProductoStock> lista = stockService.listarTodos();
        return lista.stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id_producto", s.getProductoId());
            m.put("stock", s.getStock());
            return m;
        }).collect(Collectors.toList());
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> setStock(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Integer stock = 0;
        if (body != null && body.containsKey("stock")) {
            try { stock = Integer.parseInt(body.get("stock").toString()); } catch (Exception e) { stock = 0; }
        }
        stockService.setStock(id, stock);
        return ResponseEntity.noContent().build();
    }
}
