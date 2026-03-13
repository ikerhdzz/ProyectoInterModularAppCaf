package com.cafeapp.backend.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeapp.backend.dto.auth.LoginRequest;
import com.cafeapp.backend.dto.auth.LoginResponse;
import com.cafeapp.backend.dto.auth.RegistroRequest;
import com.cafeapp.backend.dto.usuario.UsuarioResponse;
import com.cafeapp.backend.modelo.Curso;
import com.cafeapp.backend.modelo.Rol;
import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.repositorio.CursoRepository;
import com.cafeapp.backend.repositorio.RolRepository;
import com.cafeapp.backend.seguridad.JwtUtil;
import com.cafeapp.backend.servicio.UsuarioService;
import com.cafeapp.backend.servicio.UsuarioValidationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * Controlador REST para autenticación y registro de usuarios.
 * Maneja login, registro y obtención del usuario autenticado.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final UsuarioValidationService usuarioValidationService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CursoRepository cursoRepository;
    private final RolRepository rolRepository;

    public AuthController(
            UsuarioService usuarioService,
            UsuarioValidationService usuarioValidationService,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            CursoRepository cursoRepository,
            RolRepository rolRepository
    ) {
        this.usuarioService = usuarioService;
        this.usuarioValidationService = usuarioValidationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.cursoRepository = cursoRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Inicia sesión con email y contraseña.
     *
     * @param request DTO con email y contraseña.
     * @return Token JWT y datos del usuario.
     */
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        Usuario usuario = usuarioService.obtenerPorEmail(request.email());
        String token = jwtUtil.generarToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token, new UsuarioResponse(usuario)));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request DTO con los datos del usuario.
     * @return Usuario registrado.
     */
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegistroRequest request) {
        
        // ✅ Validar que el email no esté registrado
        usuarioValidationService.validarEmailUnico(request.email());

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setDni(request.dni());
        usuario.setClase(request.clase());

        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // ✅ ASIGNAR ROL POR DEFECTO: USUARIO (nunca tomar del cliente)
        Rol rolPorDefecto = rolRepository.findByNombre("USUARIO")
                .orElseThrow(() -> new RuntimeException("Rol USUARIO no configurado en el sistema"));

        usuario.setCurso(curso);
        usuario.setRol(rolPorDefecto);

        Usuario guardado = usuarioService.guardar(usuario);

        return ResponseEntity.ok(new UsuarioResponse(guardado));
    }

    /**
     * Obtiene los datos del usuario autenticado.
     *
     * @return Datos del usuario.
     */
    @Operation(summary = "Obtener usuario autenticado", description = "Devuelve los datos del usuario actualmente autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.obtenerPorEmail(email);
        return ResponseEntity.ok(new UsuarioResponse(usuario));
    }
}
