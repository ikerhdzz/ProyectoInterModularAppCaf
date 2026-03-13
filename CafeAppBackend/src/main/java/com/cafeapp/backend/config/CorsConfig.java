package com.cafeapp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS para el backend.
 *
 * Esta clase define qué orígenes (frontends) pueden comunicarse con la API.
 * Es necesaria para permitir peticiones desde aplicaciones externas como:
 * - Frontend en React/Vite
 * - Aplicaciones móviles
 * - Herramientas como Insomnia o Postman
 *
 * IMPORTANTE:
 * - allowCredentials(true) requiere que los orígenes NO usen "*".
 * - Por eso aquí se listan explícitamente los puertos del frontend.
 */
@Configuration
public class CorsConfig {

    /**
     * Configura las reglas CORS para toda la aplicación.
     *
     * @return un WebMvcConfigurer con las reglas personalizadas
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Registra las reglas CORS para todas las rutas del backend.
             *
             * - allowedOrigins: lista explícita de frontends permitidos.
             * - allowedMethods: métodos HTTP permitidos.
             * - allowedHeaders: permite cualquier encabezado.
             * - allowCredentials: permite enviar cookies o tokens en headers.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")
                        // Frontends permitidos (React/Vite)
                        .allowedOrigins(
                                "http://localhost:5173",
                                "http://localhost:5174"
                        )

                        // Métodos HTTP permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")

                        // Permitir cualquier encabezado
                        .allowedHeaders("*")

                        /**
                         * Permitir credenciales:
                         * - Necesario si el frontend envía Authorization: Bearer <token>
                         * - NO compatible con allowedOrigins("*")
                         * - Por eso aquí se listan explícitamente los orígenes
                         */
                        .allowCredentials(true);
            }
        };
    }
}
