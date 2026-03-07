package com.cafeapp.backend.servicio;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Servicio encargado de gestionar la subida de imágenes a Cloudinary.
 *
 * Funcionalidades:
 * - Subir imágenes nuevas
 * - Subir imágenes sobrescribiendo una existente (overwrite)
 */
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Constructor que recibe el bean configurado de Cloudinary.
     */
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Sube una imagen a Cloudinary sin sobrescribir archivos existentes.
     *
     * @param archivo archivo de imagen
     * @return URL segura de la imagen subida
     */
    public String subirImagen(MultipartFile archivo) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap("folder", "productos")
            );

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }

    /**
     * Sube una imagen sobrescribiendo una existente en Cloudinary.
     *
     * @param archivo archivo de imagen
     * @param publicId identificador público de la imagen a sobrescribir
     * @return URL segura de la imagen actualizada
     */
    public String subirImagenConOverwrite(MultipartFile archivo, String publicId) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "productos",
                            "public_id", publicId,
                            "overwrite", true,
                            "invalidate", true
                    )
            );

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Error al sobrescribir imagen en Cloudinary", e);
        }
    }
}
