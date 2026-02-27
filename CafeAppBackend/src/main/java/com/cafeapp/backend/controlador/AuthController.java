package com.cafeapp.backend.controlador;

import com.cafeapp.backend.dto.*;
import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.seguridad.JwtUtil;
import com.cafeapp.backend.servicio.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // ============================================================
    // LOGIN
    // ============================================================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        // 1. Autenticar con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Guardar autenticación en el contexto
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Obtener usuario desde el servicio
        Usuario usuario = usuarioService.obtenerPorEmail(request.getEmail());

        // 4. Generar token
        String token = jwtUtil.generarToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token, new UsuarioResponse(usuario)));
    }

    @GetMapping("/hash/{pwd}")
    public String hash(@PathVariable String pwd, PasswordEncoder encoder) {
        return encoder.encode(pwd);
    }

}
