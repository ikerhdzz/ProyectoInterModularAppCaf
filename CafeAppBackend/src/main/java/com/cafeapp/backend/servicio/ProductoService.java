package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.ProductoRequest;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Producto actualizar(Long id, ProductoRequest datos) {
        return productoRepository.findById(id).map(p -> {
            p.setNombre(datos.getNombre());
            p.setPrecio(datos.getPrecio());
            p.setImagen(datos.getImagen());
            p.setCategoriaId(datos.getCategoriaId());
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
