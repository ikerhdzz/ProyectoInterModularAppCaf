package com.cafeapp.backend.controlador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cafeapp.backend.dto.producto.ProductoRequest;
import com.cafeapp.backend.dto.producto.ProductoResponse;
import com.cafeapp.backend.modelo.Categoria;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.servicio.CategoriaService;
import com.cafeapp.backend.servicio.CloudinaryService;
import com.cafeapp.backend.servicio.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de productos.
 * Incluye CRUD, subida de imágenes, actualización de imágenes
 * y filtrado por categoría.
 */
@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final CloudinaryService cloudinaryService;

    public ProductoController(
            ProductoService productoService,
            CategoriaService categoriaService,
            CloudinaryService cloudinaryService
    ) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.cloudinaryService = cloudinaryService;
    }

    // ============================================================
    // LISTAR
    // ============================================================

    @Operation(summary = "Listar productos", description = "Devuelve todos los productos registrados.")
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar() {
        List<ProductoResponse> productos = productoService.listar()
                .stream()
                .map(this::convertir)
                .toList();

        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtener(@PathVariable Long id) {
        Producto producto = productoService.obtener(id);
        return ResponseEntity.ok(convertir(producto));
    }

    // ============================================================
    // SUBIR IMAGEN NORMAL
    // ============================================================

    @Operation(summary = "Subir imagen genérica",
            description = "Sube una imagen a Cloudinary y devuelve la URL resultante.")
    @PostMapping("/upload")
    public ResponseEntity<String> subirImagen(@RequestParam("imagen") MultipartFile imagen) {
        String url = cloudinaryService.subirImagen(imagen);
        return ResponseEntity.ok(url);
    }

    // ============================================================
    // SUBIR IMAGEN Y SOBRESCRIBIR LA EXISTENTE DEL PRODUCTO
    // ============================================================

    @Operation(summary = "Subir imagen de producto",
            description = "Sube una imagen y sobrescribe la existente del producto en Cloudinary.")
    @PostMapping("/upload/{productoId}")
    public ResponseEntity<String> subirImagenProducto(
            @PathVariable Long productoId,
            @RequestParam("imagen") MultipartFile imagen
    ) {
        String publicId = "producto_" + productoId;
        String url = cloudinaryService.subirImagenConOverwrite(imagen, publicId);
        return ResponseEntity.ok(url);
    }

    // ============================================================
    // ACTUALIZAR IMAGEN DEL PRODUCTO (GUARDAR EN BD)
    // ============================================================

    @Operation(summary = "Actualizar imagen del producto",
            description = "Sube una nueva imagen, sobrescribe la anterior en Cloudinary y actualiza la URL en la base de datos.")
    @PutMapping("/{id}/imagen")
    public ResponseEntity<ProductoResponse> actualizarImagen(
            @PathVariable Long id,
            @RequestParam("imagen") MultipartFile imagen
    ) {
        // Subir a Cloudinary con overwrite
        String publicId = "producto_" + id;
        String nuevaUrl = cloudinaryService.subirImagenConOverwrite(imagen, publicId);

        // Guardar la URL en la BD
        Producto actualizado = productoService.actualizarImagen(id, nuevaUrl);

        return ResponseEntity.ok(convertir(actualizado));
    }

    // ============================================================
    // LISTAR POR CATEGORÍA
    // ============================================================

    @Operation(summary = "Listar productos por categoría",
            description = "Devuelve productos filtrados por nombre de categoría.")
    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<ProductoResponse>> listarPorCategoria(@PathVariable String nombre) {
        Categoria categoria = categoriaService.obtenerPorNombre(nombre);

        List<ProductoResponse> productos = productoService.listarPorCategoria(categoria.getId())
                .stream()
                .map(this::convertir)
                .toList();

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/golosinas") public ResponseEntity<List<ProductoResponse>> golosinas() { return listarPorCategoria("Golosinas"); }
    @GetMapping("/bebidas-frias") public ResponseEntity<List<ProductoResponse>> bebidasFrias() { return listarPorCategoria("Bebidas frías"); }
    @GetMapping("/bebidas-calientes") public ResponseEntity<List<ProductoResponse>> bebidasCalientes() { return listarPorCategoria("Bebidas calientes"); }
    @GetMapping("/bocadillos") public ResponseEntity<List<ProductoResponse>> bocadillos() { return listarPorCategoria("Bocadillos"); }
    @GetMapping("/snacks") public ResponseEntity<List<ProductoResponse>> snacks() { return listarPorCategoria("Snacks"); }
    @GetMapping("/postres") public ResponseEntity<List<ProductoResponse>> postres() { return listarPorCategoria("Postres"); }
    @GetMapping("/sandwiches") public ResponseEntity<List<ProductoResponse>> sandwiches() { return listarPorCategoria("Sándwiches"); }

    // ============================================================
    // CRUD
    // ============================================================

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto.")
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {

        Categoria categoria = categoriaService.obtenerPorId(request.categoriaId());

        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setPrecioBase(request.precioBase());
        producto.setDescripcion(request.descripcion());
        producto.setImagenUrl(request.imagenUrl());
        producto.setCategoria(categoria);
        producto.setEsModificable(request.esModificable());

        Producto creado = productoService.crear(producto);

        return ResponseEntity.ok(convertir(creado));
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request
    ) {
        Categoria categoria = categoriaService.obtenerPorId(request.categoriaId());

        Producto actualizado = productoService.actualizar(id, request, categoria);

        return ResponseEntity.ok(convertir(actualizado));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // MÉTODO AUXILIAR
    // ============================================================

    private ProductoResponse convertir(Producto p) {
        return new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getPrecioBase(),
                p.getDescripcion(),
                p.getImagenUrl(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                p.getEsModificable()
        );
    }
}
