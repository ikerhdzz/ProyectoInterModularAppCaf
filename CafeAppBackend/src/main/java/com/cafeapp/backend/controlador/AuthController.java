package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.LoginRequest;
import com.cafeapp.backend.dto.RegistroRequest;
import com.cafeapp.backend.dto.TokenResponse;
import com.cafeapp.backend.dto.UsuarioResponse;
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
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest request) {

        Usuario usuario = new Usuario();
        usuario.setDni(request.getDni());
        usuario.setClase(request.getClase());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());

        if (request.getCursoId() != null) {
            Curso curso = new Curso();
            curso.setId(request.getCursoId());
            usuario.setCurso(curso);
        }

        Rol rol = new Rol();
        rol.setId(request.getRolId());
        usuario.setRol(rol);

        Usuario guardado = usuarioService.registrar(usuario);

        // Recarga desde la BD para obtener rol y curso completos
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





    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.login(request.getEmail(), request.getPassword());

        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtil.generarToken(usuario.getEmail());

        UsuarioResponse response = new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().getNombre(),
                usuario.getCurso() != null ? usuario.getCurso().getNombre() : null
        );

        response.setToken(token); // añade token al DTO

        return ResponseEntity.ok(response);
    }

}
