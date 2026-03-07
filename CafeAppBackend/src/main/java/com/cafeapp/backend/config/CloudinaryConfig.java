package com.cafeapp.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Cloudinary para la subida de imágenes.
 *
 * Esta clase expone un bean {@link Cloudinary} configurado con las credenciales
 * definidas en application.properties.
 *
 * Uso:
 * - Inyectar Cloudinary en servicios que manejen imágenes.
 */
@Configuration
public class CloudinaryConfig {

    /** Nombre del cloud de Cloudinary. */
    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    /** API Key de Cloudinary. */
    @Value("${cloudinary.api_key}")
    private String apiKey;

    /** API Secret de Cloudinary. */
    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    /**
     * Crea y expone un bean de Cloudinary configurado con las credenciales.
     *
     * @return instancia de Cloudinary lista para usar
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}
