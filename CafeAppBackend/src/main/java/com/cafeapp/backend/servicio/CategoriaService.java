package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Categoria;
import com.cafeapp.backend.repositorio.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar categorías de productos.
 *
 * Funcionalidades:
 * - Listar categorías
 * - Obtener por ID
 * - Crear/actualizar
 * - Eliminar
 * - Buscar por nombre
 */
@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    /**
     * Constructor que inyecta el repositorio de categorías.
     */
    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    /**
     * Lista todas las categorías.
     */
    public List<Categoria> listar() {
        return repo.findAll();
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @throws RuntimeException si no existe
     */
    public Categoria obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    /**
     * Guarda o actualiza una categoría.
     */
    public Categoria guardar(Categoria categoria) {
        return repo.save(categoria);
    }

    /**
     * Elimina una categoría por ID.
     */
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        repo.deleteById(id);
    }

    /**
     * Busca una categoría por nombre ignorando mayúsculas/minúsculas.
     */
    public Categoria obtenerPorNombre(String nombre) {
        return repo.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + nombre));
    }
}
