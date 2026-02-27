package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Alergeno;
import com.cafeapp.backend.modelo.Categoria;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.repositorio.AlergenoRepository;
import com.cafeapp.backend.repositorio.CategoriaRepository;
import com.cafeapp.backend.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto obtener(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crear(Producto producto) {

        // Validación opcional de categoría
        if (producto.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        // Validación opcional de alérgenos
        if (producto.getAlergenos() != null) {
            List<Alergeno> alergenos = producto.getAlergenos().stream()
                    .map(a -> alergenoRepository.findById(a.getId())
                            .orElseThrow(() -> new RuntimeException("Alérgeno no encontrado: " + a.getId())))
                    .toList();
            producto.setAlergenos(alergenos);
        }

        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        Producto existente = obtener(id);

        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setDescripcion(producto.getDescripcion());
        existente.setImagen(producto.getImagen());

        // Validación opcional de categoría
        if (producto.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            existente.setCategoria(categoria);
        }

        // Validación opcional de alérgenos
        if (producto.getAlergenos() != null) {
            List<Alergeno> alergenos = producto.getAlergenos().stream()
                    .map(a -> alergenoRepository.findById(a.getId())
                            .orElseThrow(() -> new RuntimeException("Alérgeno no encontrado: " + a.getId())))
                    .toList();
            existente.setAlergenos(alergenos);
        }

        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}
