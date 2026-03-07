package com.cafeapp.backend.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro JWT que se ejecuta una vez por cada petición.
 *
 * Funciones:
 * - Leer el token JWT del encabezado Authorization.
 * - Validarlo.
 * - Extraer email y rol.
 * - Registrar la autenticación en el contexto de Spring Security.
 *
 * Además:
 * - Excluye rutas públicas (Swagger, login, registro, webhooks).
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Lista de rutas que NO deben pasar por el filtro JWT.
     */
    private static final String[] EXCLUDED_PATHS = {
            "/auth/login",
            "/auth/register",
            "/api/stripe/webhook",

            // Swagger UI
            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    /**
     * Indica si el filtro debe ignorar la petición actual.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        for (String excluded : EXCLUDED_PATHS) {
            if (path.startsWith(excluded)) {
                return true; // No filtrar esta ruta
            }
        }

        return false; // Filtrar todo lo demás
    }

    /**
     * Procesa cada petición HTTP para validar el token JWT.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Verifica si viene un token en formato Bearer
        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (jwtUtil.esValido(token)) {

                String email = jwtUtil.obtenerEmail(token);
                String rol = jwtUtil.obtenerRol(token);

                // Crea la autenticación para Spring Security
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()))
                        );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Registra la autenticación en el contexto
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continúa con el siguiente filtro
        filterChain.doFilter(request, response);
    }
}
