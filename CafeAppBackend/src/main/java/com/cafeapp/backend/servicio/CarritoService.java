package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.CarritoResponse;
import com.cafeapp.backend.dto.ItemCarritoResponse;
import com.cafeapp.backend.modelo.*;
import com.cafeapp.backend.repositorio.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;
    private final ExtraProductoRepository extraProductoRepository;


    public CarritoService(UsuarioRepository usuarioRepository,
                          CarritoRepository carritoRepository,
                          ItemCarritoRepository itemCarritoRepository,
                          ExtraProductoRepository extraProductoRepository,
                          ProductoRepository productoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.extraProductoRepository = extraProductoRepository;
        this.productoRepository = productoRepository;
    }

    private Usuario usuarioActual() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(email).orElseThrow();
    }

    private Carrito obtenerOCrearCarrito(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> carritoRepository.save(new Carrito(usuario)));
    }

    public void agregarProducto(Long usuarioId, Long productoId, int cantidad) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = obtenerOCrearCarrito(usuario);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseGet(() -> {
                    ItemCarrito nuevo = new ItemCarrito(producto, 0);
                    itemCarritoRepository.save(nuevo);
                    carrito.getItems().add(nuevo);
                    carritoRepository.save(carrito);
                    return nuevo;
                });

        item.setCantidad(item.getCantidad() + cantidad);
        itemCarritoRepository.save(item);
    }


    public CarritoResponse obtenerCarritoUsuario() {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        List<ItemCarritoResponse> items = carrito.getItems().stream()
                .map(i -> new ItemCarritoResponse(
                        i.getProducto().getId(),
                        i.getProducto().getNombre(),
                        i.getProducto().getPrecio(),
                        i.getCantidad()
                ))
                .toList();

        return new CarritoResponse(items);
    }

    public void actualizarCantidad(Long productoId, int cantidad) {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow();

        item.setCantidad(cantidad);
        itemCarritoRepository.save(item);
    }

    public void eliminarProducto(Long productoId) {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow();

        carrito.getItems().remove(item);
        carritoRepository.save(carrito);
        itemCarritoRepository.delete(item);
    }

    public void vaciarCarrito() {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        carrito.getItems().forEach(itemCarritoRepository::delete);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }

    public void agregarExtraItem(Long itemId, Long extraId) {

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        ExtraProducto extra = extraProductoRepository.findById(extraId)
                .orElseThrow(() -> new RuntimeException("Extra no encontrado"));

        item.getExtras().add(extra.getId());
        itemCarritoRepository.save(item);
    }


    public void quitarExtraItem(Long itemId, Long extraId) {

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        item.getExtras().remove(extraId);
        itemCarritoRepository.save(item);
    }


}
