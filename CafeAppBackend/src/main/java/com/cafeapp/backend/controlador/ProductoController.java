package com.cafeapp.backend.controlador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import org.springframework.http.MediaType;

import com.cafeapp.backend.dto.ProductoRequest;
import com.cafeapp.backend.dto.ProductoResponse;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.servicio.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
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
        // Si la petición no trae categoriaId, asignar una categoría por defecto (1).
        // Asegúrate de que la categoría 1 exista en la base de datos o ajústalo según tu esquema.
        producto.setCategoriaId(request.getCategoriaId() != null ? request.getCategoriaId() : 1L);

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

    @PostMapping("/supabase")
    public ResponseEntity<ProductoResponse> recibirDesdeSupabase(@RequestBody ProductoRequest request) {

        Producto guardado = productoService.upsertFromSupabase(request);

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
            @RequestBody ProductoRequest request) {

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

    @PostMapping(path = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponse> subirImagen(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Directorio de subida relativo al working dir
            java.nio.file.Path uploadDir = java.nio.file.Paths.get("uploads", "images");
            java.nio.file.Files.createDirectories(uploadDir);

            String original = java.nio.file.Paths.get(file.getOriginalFilename()).getFileName().toString();
            String filename = System.currentTimeMillis() + "_" + original;
            java.nio.file.Path target = uploadDir.resolve(filename);

            // Guardar fichero
            try (java.io.InputStream in = file.getInputStream()) {
                java.nio.file.Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            // Guardar referencia en la entidad (ruta pública)
            String publicPath = "/uploads/images/" + filename;

            com.cafeapp.backend.dto.ProductoRequest req = new com.cafeapp.backend.dto.ProductoRequest();
            req.setImagen(publicPath);

            Producto actualizado = productoService.actualizar(id, req);
            if (actualizado == null) return ResponseEntity.notFound().build();

            return ResponseEntity.ok(new ProductoResponse(
                    actualizado.getId(),
                    actualizado.getNombre(),
                    actualizado.getPrecio(),
                    actualizado.getImagen(),
                    actualizado.getCategoriaId(),
                    actualizado.getCategoria() != null ? actualizado.getCategoria().getNombre() : null
            ));

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
