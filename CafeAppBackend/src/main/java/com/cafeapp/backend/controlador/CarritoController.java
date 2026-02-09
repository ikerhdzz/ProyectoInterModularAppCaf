package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.CarritoRequest;
import com.cafeapp.backend.dto.CarritoResponse;
import com.cafeapp.backend.servicio.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody CarritoRequest req) {

        carritoService.agregarProducto(
                req.getUsuarioId(),
                req.getProductoId(),
                req.getCantidad()
        );

        return ResponseEntity.ok("Producto agregado al carrito");
    }


    @GetMapping
    public ResponseEntity<CarritoResponse> obtenerCarrito() {
        return ResponseEntity.ok(carritoService.obtenerCarritoUsuario());
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarCantidad(
            @RequestParam Long productoId,
            @RequestParam int cantidad) {

        carritoService.actualizarCantidad(productoId, cantidad);
        return ResponseEntity.ok("Cantidad actualizada");
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarProducto(@RequestParam Long productoId) {
        carritoService.eliminarProducto(productoId);
        return ResponseEntity.ok("Producto eliminado");
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<?> vaciarCarrito() {
        carritoService.vaciarCarrito();
        return ResponseEntity.ok("Carrito vaciado");
    }
}
