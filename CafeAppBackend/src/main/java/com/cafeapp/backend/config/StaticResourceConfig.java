package com.cafeapp.backend.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Exponer la carpeta uploads como /uploads/**
        String absolute = Paths.get("uploads").toAbsolutePath().toString();
        // Asegurar prefijo file: y barra final
        String uploadPath = "file:" + absolute + (absolute.endsWith(System.getProperty("file.separator")) ? "" : System.getProperty("file.separator"));
        if (!uploadPath.endsWith("/")) uploadPath = uploadPath + "/";
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations(uploadPath);
    }
}
