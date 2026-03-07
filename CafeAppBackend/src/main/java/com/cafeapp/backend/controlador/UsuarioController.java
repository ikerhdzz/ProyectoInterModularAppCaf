package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.usuario.UsuarioRequest;
import com.cafeapp.backend.dto.usuario.UsuarioResponse;
import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.servicio.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> usuarios = usuarioService.listar()
                .stream()
                .map(this::convertir)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtener(id);
        return ResponseEntity.ok(convertir(usuario));
    }

    @Operation(summary = "Crear usuario")
    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        Usuario creado = usuarioService.crear(request);
        return ResponseEntity.ok(convertir(creado));
    }

    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request
    ) {
        Usuario actualizado = usuarioService.actualizar(id, request);
        return ResponseEntity.ok(convertir(actualizado));
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar usuarios por rol")
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioResponse>> listarPorRol(@PathVariable String rol) {
        List<UsuarioResponse> usuarios = usuarioService.listarPorRol(rol)
                .stream()
                .map(this::convertir)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Listar usuarios por centro")
    @GetMapping("/centro/{centroId}")
    public ResponseEntity<List<UsuarioResponse>> listarPorCentro(@PathVariable Long centroId) {
        List<UsuarioResponse> usuarios = usuarioService.listarPorCentro(centroId)
                .stream()
                .map(this::convertir)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Listar usuarios por curso")
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<UsuarioResponse>> listarPorCurso(@PathVariable Long cursoId) {
        List<UsuarioResponse> usuarios = usuarioService.listarPorCurso(cursoId)
                .stream()
                .map(this::convertir)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    private UsuarioResponse convertir(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getDni(),
                u.getClase(),
                u.getImagenUrl(),
                u.getRol() != null ? u.getRol().getNombre() : null,
                u.getCurso() != null ? u.getCurso().getNombre() : null,
                u.getCentro() != null ? u.getCentro().getNombre() : null,
                u.isPerfilCompletado() // CORREGIDO
        );
    }
}
