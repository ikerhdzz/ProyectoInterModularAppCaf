package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.*;
import com.cafeapp.backend.modelo.Curso;
import com.cafeapp.backend.modelo.Rol;
import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.seguridad.JwtUtil;
import com.cafeapp.backend.servicio.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // En desarrollo está bien, luego afinamos
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    // ============================================================
    // REGISTRO
    // ============================================================
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest request) {

        Usuario usuario = new Usuario();
        usuario.setDni(request.getDni());
        usuario.setClase(request.getClase());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());

        // Asignar curso si viene
        if (request.getCursoId() != null) {
            Curso curso = new Curso();
            curso.setId(request.getCursoId());
            usuario.setCurso(curso);
        }

        // Asignar rol
        Rol rol = new Rol();
        rol.setId(request.getRolId());
        usuario.setRol(rol);

        // Guardar usuario
        Usuario guardado = usuarioService.registrar(usuario);

        // Recargar desde BD para obtener curso y rol completos
        Usuario completo = usuarioService.obtenerPorId(guardado.getId());

        return ResponseEntity.ok(
                new UsuarioResponse(
                        completo.getId(),
                        completo.getNombre(),
                        completo.getEmail(),
                        completo.getRol().getNombre(),
                        completo.getCurso() != null ? completo.getCurso().getNombre() : null
                )
        );
    }

    // ============================================================
    // LOGIN
    // ============================================================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.login(request.getEmail(), request.getPassword());

        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        // Generar token
        String token = jwtUtil.generarToken(usuario.getEmail());

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().getNombre().toUpperCase(),
                usuario.getCurso() != null ? usuario.getCurso().getNombre() : null
        );

        return ResponseEntity.ok(new LoginResponse(token, usuarioResponse));
    }
}
