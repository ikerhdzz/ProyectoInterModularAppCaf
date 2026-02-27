package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.CarritoResponse;
import com.cafeapp.backend.servicio.CarritoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // ============================================================
    // OBTENER CARRITO DEL USUARIO
    // ============================================================
    @GetMapping
    public CarritoResponse obtenerCarrito() {
        return carritoService.obtenerCarritoUsuario();
    }

    // ============================================================
    // AGREGAR PRODUCTO
    // ============================================================
    @PostMapping("/agregar")
    public void agregarProducto(
            @RequestParam Long productoId,
            @RequestParam int cantidad
    ) {
        carritoService.agregarProducto(productoId, cantidad);
    }

    // ============================================================
    // ACTUALIZAR CANTIDAD
    // ============================================================
    @PutMapping("/cantidad")
    public void actualizarCantidad(
            @RequestParam Long productoId,
            @RequestParam int cantidad
    ) {
        carritoService.actualizarCantidad(productoId, cantidad);
    }

    // ============================================================
    // ELIMINAR PRODUCTO
    // ============================================================
    @DeleteMapping("/eliminar")
    public void eliminarProducto(@RequestParam Long productoId) {
        carritoService.eliminarProducto(productoId);
    }

    // ============================================================
    // VACIAR CARRITO
    // ============================================================
    @DeleteMapping("/vaciar")
    public void vaciarCarrito() {
        carritoService.vaciarCarrito();
    }

    // ============================================================
    // AGREGAR EXTRA A ITEM
    // ============================================================
    @PostMapping("/extra/agregar")
    public void agregarExtra(
            @RequestParam Integer itemId,
            @RequestParam Long extraId
    ) {
        carritoService.agregarExtraItem(itemId, extraId);
    }

    // ============================================================
    // QUITAR EXTRA DE ITEM
    // ============================================================
    @DeleteMapping("/extra/quitar")
    public void quitarExtra(
            @RequestParam Long itemId,
            @RequestParam Long extraId
    ) {
        carritoService.quitarExtraItem(itemId, extraId);
    }
}
