package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.categoria.CategoriaRequest;
import com.cafeapp.backend.dto.categoria.CategoriaResponse;
import com.cafeapp.backend.modelo.Categoria;
import com.cafeapp.backend.servicio.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de categorías de productos.
 * Incluye CRUD estándar y endpoints específicos por tipo de categoría.
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // ---------- CRUD BÁSICO ----------

    @Operation(summary = "Listar categorías", description = "Devuelve todas las categorías registradas.")
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        List<CategoriaResponse> categorias = categoriaService.listar()
                .stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre(), c.getIconoUrl()))
                .toList();
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(
                new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getIconoUrl())
        );
    }

    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría.")
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        categoria.setIconoUrl(request.iconoUrl());

        Categoria guardada = categoriaService.guardar(categoria);

        return ResponseEntity.ok(
                new CategoriaResponse(guardada.getId(), guardada.getNombre(), guardada.getIconoUrl())
        );
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente.")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request
    ) {
        Categoria categoria = categoriaService.obtenerPorId(id);
        categoria.setNombre(request.nombre());
        categoria.setIconoUrl(request.iconoUrl());

        Categoria actualizada = categoriaService.guardar(categoria);

        return ResponseEntity.ok(
                new CategoriaResponse(actualizada.getId(), actualizada.getNombre(), actualizada.getIconoUrl())
        );
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- ENDPOINTS POR TIPO ----------

    @GetMapping("/bebidas-frias")
    public ResponseEntity<CategoriaResponse> bebidasFrias() {
        return ResponseEntity.ok(convertir(categoriaService.obtenerPorNombre("Bebidas frías")));
    }

    @GetMapping("/bebidas-calientes")
    public ResponseEntity<CategoriaResponse> bebidasCalientes() {
        return ResponseEntity.ok(convertir(categoriaService.obtenerPorNombre("Bebidas calientes")));
    }

    @GetMapping("/bocadillos")
    public ResponseEntity<CategoriaResponse> bocadillos() {
        return ResponseEntity.ok(convertir(categoriaService.obtenerPorNombre("Bocadillos")));
    }

    @GetMapping("/golosinas")
    public ResponseEntity<CategoriaResponse> golosinas() {
        return ResponseEntity.ok(convertir(categoriaService.obtenerPorNombre("Golosinas")));
    }

    @GetMapping("/sandwiches")
    public ResponseEntity<CategoriaResponse> sandwiches() {
        return ResponseEntity.ok(convertir(categoriaService.obtenerPorNombre("Sándwiches")));
    }

    private CategoriaResponse convertir(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getIconoUrl());
    }
}
