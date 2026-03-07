package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.carrito.AgregarExtraRequest;
import com.cafeapp.backend.dto.carrito.AgregarProductoRequest;
import com.cafeapp.backend.dto.carrito.ActualizarCantidadRequest;
import com.cafeapp.backend.dto.carrito.CarritoResponse;
import com.cafeapp.backend.servicio.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión del carrito de compras.
 * Permite agregar productos, modificar cantidades, gestionar extras y vaciar el carrito.
 */
@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    /**
     * Obtiene el carrito del usuario autenticado.
     *
     * @return CarritoResponse con los ítems y totales.
     */
    @Operation(summary = "Obtener carrito", description = "Devuelve el carrito del usuario autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = CarritoResponse.class)))
    })
    @GetMapping
    public ResponseEntity<CarritoResponse> obtenerCarrito() {
        return ResponseEntity.ok(carritoService.obtenerCarritoUsuario());
    }

    /**
     * Agrega un producto al carrito.
     *
     * @param request DTO con ID del producto y cantidad.
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Agregar producto al carrito", description = "Agrega un producto al carrito del usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @PostMapping("/items")
    public ResponseEntity<Void> agregarProducto(@Valid @RequestBody AgregarProductoRequest request) {
        carritoService.agregarProducto(request.productoId(), request.cantidad());
        return ResponseEntity.ok().build();
    }

    /**
     * Actualiza la cantidad de un producto en el carrito.
     *
     * @param productoId ID del producto.
     * @param request DTO con la nueva cantidad.
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Actualizar cantidad", description = "Modifica la cantidad de un producto en el carrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @PutMapping("/items/{productoId}")
    public ResponseEntity<Void> actualizarCantidad(
            @PathVariable Long productoId,
            @Valid @RequestBody ActualizarCantidadRequest request
    ) {
        carritoService.actualizarCantidad(productoId, request.cantidad());
        return ResponseEntity.ok().build();
    }

    /**
     * Elimina un producto del carrito.
     *
     * @param productoId ID del producto.
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del carrito del usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente")
    })
    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long productoId) {
        carritoService.eliminarProducto(productoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Agrega un extra a un ítem del carrito.
     *
     * @param itemId ID del ítem del carrito.
     * @param request DTO con el ID del extra.
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Agregar extra", description = "Agrega un extra a un ítem del carrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Extra agregado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @PostMapping("/items/{itemId}/extras")
    public ResponseEntity<Void> agregarExtra(
            @PathVariable Long itemId,
            @Valid @RequestBody AgregarExtraRequest request
    ) {
        carritoService.agregarExtraItem(itemId, request.extraId());
        return ResponseEntity.ok().build();
    }

    /**
     * Quita un extra de un ítem del carrito.
     *
     * @param itemId ID del ítem.
     * @param extraId ID del extra.
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Quitar extra", description = "Elimina un extra de un ítem del carrito.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Extra eliminado correctamente")
    })
    @DeleteMapping("/items/{itemId}/extras/{extraId}")
    public ResponseEntity<Void> quitarExtra(
            @PathVariable Long itemId,
            @PathVariable Long extraId
    ) {
        carritoService.quitarExtraItem(itemId, extraId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vacía completamente el carrito del usuario.
     *
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Vaciar carrito", description = "Elimina todos los productos del carrito del usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carrito vaciado correctamente")
    })
    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito() {
        carritoService.vaciarCarrito();
        return ResponseEntity.noContent().build();
    }
}
