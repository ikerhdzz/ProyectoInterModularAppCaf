package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.centro.CentroRequest;
import com.cafeapp.backend.dto.centro.CentroResponse;
import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.servicio.CentroService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de centros.
 */
@RestController
@RequestMapping("/centros")
public class CentroController {

    private final CentroService centroService;

    public CentroController(CentroService centroService) {
        this.centroService = centroService;
    }

    @Operation(summary = "Listar centros")
    @GetMapping
    public ResponseEntity<List<CentroResponse>> listar() {
        List<CentroResponse> centros = centroService.listar()
                .stream()
                .map(this::convertir)
                .toList();
        return ResponseEntity.ok(centros);
    }

    @Operation(summary = "Obtener centro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CentroResponse> obtener(@PathVariable Long id) {
        Centro centro = centroService.obtener(id);
        return ResponseEntity.ok(convertir(centro));
    }

    @Operation(summary = "Crear centro")
    @PostMapping
    public ResponseEntity<CentroResponse> crear(@Valid @RequestBody CentroRequest request) {
        Centro creado = centroService.crear(request);
        return ResponseEntity.ok(convertir(creado));
    }

    @Operation(summary = "Actualizar centro")
    @PutMapping("/{id}")
    public ResponseEntity<CentroResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CentroRequest request
    ) {
        Centro actualizado = centroService.actualizar(id, request);
        return ResponseEntity.ok(convertir(actualizado));
    }

    @Operation(summary = "Eliminar centro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        centroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private CentroResponse convertir(Centro c) {
        return new CentroResponse(
                c.getId(),
                c.getNombre(),
                c.getCodigo(),
                c.getDireccion(),
                c.getTelefono()
        );
    }
}
