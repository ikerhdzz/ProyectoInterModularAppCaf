package com.cafeapp.backend.controlador;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.seguridad.JwtUtil;
import com.cafeapp.backend.servicio.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrar(usuario);
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Usuario encontrado = usuarioService.login(usuario.getEmail(), usuario.getPassword());
        if (encontrado != null) {
            String token = JwtUtil.generarToken(encontrado.getEmail());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
}
