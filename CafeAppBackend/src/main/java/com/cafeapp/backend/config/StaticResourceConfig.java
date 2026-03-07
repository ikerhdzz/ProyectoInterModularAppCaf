package com.cafeapp.backend.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración para exponer recursos estáticos desde la carpeta "uploads".
 *
 * IMPORTANTE:
 * Esta clase ya NO implementa WebMvcConfigurer directamente para evitar
 * que Spring la trate como una configuración MVC global y mezcle CORS.
 *
 * En su lugar, se expone un bean WebMvcConfigurer dedicado SOLO a recursos estáticos.
 */
@Configuration
public class StaticResourceConfig {

    /**
     * Bean que registra un manejador de recursos estáticos para la carpeta "uploads".
     *
     * Este bean NO define CORS, NO afecta a CorsConfig y NO interfiere con SecurityConfig.
     */
    @Bean
    public WebMvcConfigurer staticResourceConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {

                // Obtiene la ruta absoluta de la carpeta uploads
                String absolute = Paths.get("uploads").toAbsolutePath().toString();

                // Asegura el formato correcto del prefijo file:
                String uploadPath = "file:" + absolute +
                        (absolute.endsWith(System.getProperty("file.separator")) ? "" : System.getProperty("file.separator"));

                // Asegura barra final
                if (!uploadPath.endsWith("/")) uploadPath = uploadPath + "/";

                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations(uploadPath);
            }
        };
    }
}
