package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.alergeno.CrearAlergenoRequest;
import com.cafeapp.backend.modelo.Alergeno;
import com.cafeapp.backend.servicio.AlergenoService;
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
 * Controlador REST para la gestión de alérgenos.
 * Proporciona endpoints para listar, crear y eliminar alérgenos.
 */
@RestController
@RequestMapping("/alergenos")
public class AlergenoController {

    private final AlergenoService alergenoService;

    public AlergenoController(AlergenoService alergenoService) {
        this.alergenoService = alergenoService;
    }

    /**
     * Obtiene la lista completa de alérgenos registrados.
     *
     * @return Lista de alérgenos.
     */
    @Operation(summary = "Listar alérgenos", description = "Devuelve todos los alérgenos registrados en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Alergeno>> listar() {
        return ResponseEntity.ok(alergenoService.listar());
    }

    /**
     * Crea un nuevo alérgeno.
     *
     * @param request Datos del alérgeno a crear.
     * @return El alérgeno creado.
     */
    @Operation(summary = "Crear alérgeno", description = "Crea un nuevo alérgeno en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alérgeno creado correctamente",
                    content = @Content(schema = @Schema(implementation = Alergeno.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @PostMapping
    public ResponseEntity<Alergeno> crear(@Valid @RequestBody CrearAlergenoRequest request) {

        Alergeno alergeno = new Alergeno();
        alergeno.setNombre(request.nombre());

        return ResponseEntity.ok(alergenoService.crear(alergeno));
    }

    /**
     * Elimina un alérgeno por su ID.
     *
     * @param id ID del alérgeno a eliminar.
     * @return Respuesta sin contenido.
     */
    @Operation(summary = "Eliminar alérgeno", description = "Elimina un alérgeno existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Alérgeno eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Alérgeno no encontrado",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alergenoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
