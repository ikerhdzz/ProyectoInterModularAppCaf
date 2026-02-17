package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.ProductoRequest;
import com.cafeapp.backend.dto.ProductoResponse;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.servicio.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar() {
        List<ProductoResponse> lista = productoService.listar()
                .stream()
                .map(p -> new ProductoResponse(
                        p.getId(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getImagen(),
                        p.getCategoriaId(),
                        p.getCategoria() != null ? p.getCategoria().getNombre() : null
                ))
                .toList();

        return ResponseEntity.ok(lista);
    }


    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setImagen(request.getImagen());
        producto.setCategoriaId(request.getCategoriaId());

        Producto guardado = productoService.crear(producto);

        return ResponseEntity.ok( new ProductoResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getPrecio(),
                guardado.getImagen(),
                guardado.getCategoriaId(),
                guardado.getCategoria() != null ? guardado.getCategoria().getNombre() : null )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {

        Producto actualizado = productoService.actualizar(id, request);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok( new ProductoResponse(
                actualizado.getId(),
                actualizado.getNombre(),
                actualizado.getPrecio(),
                actualizado.getImagen(),
                actualizado.getCategoriaId(),
                actualizado.getCategoria() != null ? actualizado.getCategoria().getNombre() : null )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = productoService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
