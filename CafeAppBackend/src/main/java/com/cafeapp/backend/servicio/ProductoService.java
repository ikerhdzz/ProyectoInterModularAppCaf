package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.producto.ProductoRequest;
import com.cafeapp.backend.modelo.Alergeno;
import com.cafeapp.backend.modelo.Categoria;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.repositorio.AlergenoRepository;
import com.cafeapp.backend.repositorio.CategoriaRepository;
import com.cafeapp.backend.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

/**
 * Servicio encargado de gestionar productos.
 *
 * Funcionalidades:
 * - Listar productos
 * - Obtener por ID
 * - Crear productos
 * - Actualizar productos
 * - Actualizar imagen del producto
 * - Eliminar productos
 * - Validar categoría, alérgenos y URL de imagen
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AlergenoRepository alergenoRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            AlergenoRepository alergenoRepository
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.alergenoRepository = alergenoRepository;
    }

    // ============================================================
    // LISTAR / OBTENER
    // ============================================================

    /**
     * Lista todos los productos.
     */
    public List<Producto> listar() {
        return productoRepository.findAllWithAlergenos();
    }

    /**
     * Obtiene un producto por su ID.
     */
    public Producto obtener(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    /**
     * Lista productos filtrados por categoría.
     */
    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    // ============================================================
    // CREAR
    // ============================================================

    /**
     * Crea un nuevo producto validando:
     * - URL de imagen
     * - Categoría existente
     * - Alérgenos existentes
     */
    public Producto crear(Producto producto) {
        validarUrl(producto.getImagenUrl());
        validarCategoria(producto);
        validarAlergenos(producto);
        return productoRepository.save(producto);
    }

    // ============================================================
    // ACTUALIZAR
    // ============================================================

    /**
     * Actualiza un producto existente usando un DTO.
     */
    public Producto actualizar(Long id, ProductoRequest request, Categoria categoria) {
        Producto existente = obtener(id);

        validarUrl(request.imagenUrl());

        existente.setNombre(request.nombre());
        existente.setPrecioBase(request.precioBase());
        existente.setDescripcion(request.descripcion());
        existente.setImagenUrl(request.imagenUrl());
        existente.setCategoria(categoria);
        existente.setEsModificable(request.esModificable());

        return productoRepository.save(existente);
    }

    // ============================================================
    // ACTUALIZAR SOLO LA IMAGEN
    // ============================================================

    /**
     * Actualiza únicamente la URL de la imagen del producto.
     *
     * @param id      ID del producto
     * @param nuevaUrl URL generada por Cloudinary
     * @return producto actualizado
     */
    public Producto actualizarImagen(Long id, String nuevaUrl) {
        Producto producto = obtener(id);

        validarUrl(nuevaUrl);

        producto.setImagenUrl(nuevaUrl);

        return productoRepository.save(producto);
    }

    // ============================================================
    // ELIMINAR
    // ============================================================

    /**
     * Elimina un producto por su ID.
     */
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Valida que la categoría del producto exista.
     */
    private void validarCategoria(Producto producto) {
        if (producto.getCategoria() == null) return;

        Long categoriaId = producto.getCategoria().getId();

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setCategoria(categoria);
    }

    /**
     * Valida que todos los alérgenos del producto existan.
     */
    private void validarAlergenos(Producto producto) {
        if (producto.getAlergenos() == null) return;

        List<Alergeno> alergenos = producto.getAlergenos().stream()
                .map(a -> alergenoRepository.findById(a.getId())
                        .orElseThrow(() -> new RuntimeException("Alérgeno no encontrado: " + a.getId())))
                .toList();

        producto.setAlergenos(alergenos);
    }

    /**
     * Valida que la URL de imagen tenga un formato correcto.
     */
    private void validarUrl(String url) {
        if (url == null || url.isBlank()) return;

        try {
            new URL(url).toURI();
        } catch (Exception e) {
            throw new RuntimeException("URL de imagen inválida: " + url);
        }
    }
}
