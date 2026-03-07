package com.cafeapp.backend.config;

import com.cafeapp.backend.seguridad.CustomUserDetailsService;
import com.cafeapp.backend.seguridad.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de seguridad del backend.
 *
 * Incluye:
 * - Autenticación con JWT
 * - Filtro personalizado JwtFilter
 * - Rutas públicas y protegidas
 * - Sesiones sin estado (stateless)
 * - Acceso permitido a Swagger UI
 *
 * NOTA IMPORTANTE:
 * Se desactiva CORS dentro de Spring Security para evitar conflictos,
 * permitiendo que la configuración global definida en CorsConfig sea la única activa.
 */
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Bean para encriptar contraseñas usando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación basado en usuarios cargados desde la BD.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Expone el AuthenticationManager para manejar autenticaciones manuales.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configuración principal de seguridad HTTP.
     *
     * - Desactiva CSRF (porque usamos JWT)
     * - DESACTIVA CORS dentro de Spring Security para evitar conflictos
     * - Define rutas públicas y protegidas
     * - Añade el filtro JWT antes del filtro de autenticación estándar
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Desactivar CSRF porque usamos JWT
                .csrf(csrf -> csrf.disable())

                // DESACTIVAR CORS en Spring Security
                // Esto permite que SOLO CorsConfig maneje CORS
                .cors(cors -> cors.disable())

                // Sesiones sin estado (JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Reglas de autorización
                .authorizeHttpRequests(auth -> auth

                        // ============================
                        // RUTAS PÚBLICAS
                        // ============================

                        // Autenticación
                        .requestMatchers("/auth/login", "/auth/register").permitAll()

                        // Webhook de Stripe
                        .requestMatchers("/api/stripe/webhook").permitAll()

                        // Swagger UI y documentación
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ============================
                        // RUTAS PROTEGIDAS
                        // ============================

                        // Pagos requieren autenticación
                        .requestMatchers("/pagos/**").authenticated()

                        // Todo lo demás requiere JWT
                        .anyRequest().authenticated()
                )

                // Proveedor de autenticación
                .authenticationProvider(authenticationProvider())

                // Filtro JWT antes del filtro estándar
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
