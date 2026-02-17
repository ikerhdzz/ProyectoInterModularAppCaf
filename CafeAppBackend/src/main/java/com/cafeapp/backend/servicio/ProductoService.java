package com.cafeapp.backend.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cafeapp.backend.dto.ProductoRequest;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.repositorio.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto upsertFromSupabase(ProductoRequest req) {
        if (req.getId() != null) {
            Optional<Producto> existente = productoRepository.findById(req.getId());
            if (existente.isPresent()) {
                Producto p = existente.get();
                p.setNombre(req.getNombre());
                p.setPrecio(req.getPrecio());
                p.setImagen(req.getImagen());
                p.setCategoriaId(req.getCategoriaId());
                return productoRepository.save(p);
            } else {
                Producto nuevo = new Producto();
                nuevo.setId(req.getId());
                nuevo.setNombre(req.getNombre());
                nuevo.setPrecio(req.getPrecio());
                nuevo.setImagen(req.getImagen());
                nuevo.setCategoriaId(req.getCategoriaId());
                return productoRepository.save(nuevo);
            }
        } else {
            Producto nuevo = new Producto();
            nuevo.setNombre(req.getNombre());
            nuevo.setPrecio(req.getPrecio());
            nuevo.setImagen(req.getImagen());
            nuevo.setCategoriaId(req.getCategoriaId());
            return productoRepository.save(nuevo);
        }
    }

    public Producto actualizar(Long id, ProductoRequest datos) {
        return productoRepository.findById(id).map(p -> {
            // Aplicar solo los campos que no sean null para permitir actualizaciones parciales
            if (datos.getNombre() != null) p.setNombre(datos.getNombre());
            if (datos.getPrecio() != null) p.setPrecio(datos.getPrecio());
            if (datos.getImagen() != null) p.setImagen(datos.getImagen());
            if (datos.getCategoriaId() != null) p.setCategoriaId(datos.getCategoriaId());
            return productoRepository.save(p);
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
