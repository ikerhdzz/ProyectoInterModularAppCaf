package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.carrito.CarritoResponse;
import com.cafeapp.backend.dto.carrito.ItemCarritoResponse;
import com.cafeapp.backend.dto.producto.ExtraProductoResponse;
import com.cafeapp.backend.modelo.*;
import com.cafeapp.backend.repositorio.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar el carrito de compra del usuario.
 *
 * Funcionalidades:
 * - Obtener o crear carrito
 * - Agregar productos
 * - Agregar o quitar extras
 * - Actualizar cantidades
 * - Eliminar productos
 * - Vaciar carrito
 * - Obtener carrito completo con totales
 */
@Service
public class CarritoService {

    private final UsuarioService usuarioService;
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ItemCarritoExtraRepository itemCarritoExtraRepository;
    private final ProductoRepository productoRepository;
    private final ExtraProductoRepository extraProductoRepository;
    private final StockCentroService stockCentroService;

    /**
     * Constructor con inyección de dependencias.
     */
    public CarritoService(
            UsuarioService usuarioService,
            CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            ItemCarritoExtraRepository itemCarritoExtraRepository,
            ExtraProductoRepository extraProductoRepository,
            ProductoRepository productoRepository,
            StockCentroService stockCentroService
    ) {
        this.usuarioService = usuarioService;
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.itemCarritoExtraRepository = itemCarritoExtraRepository;
        this.extraProductoRepository = extraProductoRepository;
        this.productoRepository = productoRepository;
        this.stockCentroService = stockCentroService;
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     */
    private Usuario usuarioActual() {
        return usuarioService.obtenerUsuarioActual();
    }

    /**
     * Obtiene el carrito del usuario o lo crea si no existe.
     *
     * @param usuario usuario propietario del carrito
     * @return carrito existente o nuevo
     */
    private Carrito obtenerOCrearCarrito(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> carritoRepository.save(new Carrito(usuario)));
    }

    /**
     * Agrega un producto al carrito del usuario.
     *
     * @param productoId ID del producto
     * @param cantidad cantidad a agregar
     */
    public void agregarProducto(Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero");
        }

        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        StockCentro stock = stockCentroService.obtenerPorCentroYProducto(
                usuario.getCentro().getId(),
                productoId
        );

        if (stock.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + stock.getStockActual());
        }

        // Buscar si el producto ya está en el carrito
        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseGet(() -> {
                    ItemCarrito nuevo = new ItemCarrito(carrito, producto, 0);
                    itemCarritoRepository.save(nuevo);
                    carrito.getItems().add(nuevo);
                    carritoRepository.save(carrito);
                    return nuevo;
                });

        int nuevaCantidad = item.getCantidad() + cantidad;

        if (stock.getStockActual() < nuevaCantidad) {
            throw new RuntimeException(
                    "No hay suficiente stock para aumentar la cantidad. Disponible: " + stock.getStockActual()
            );
        }

        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);
    }

    /**
     * Agrega un extra a un item del carrito.
     *
     * @param itemId ID del item del carrito
     * @param extraId ID del extra
     * @return item actualizado
     */
    public ItemCarrito agregarExtraItem(Long itemId, Long extraId) {
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        ExtraProducto extra = extraProductoRepository.findById(extraId)
                .orElseThrow(() -> new RuntimeException("Extra no encontrado"));

        ItemCarritoExtra itemExtra = new ItemCarritoExtra(item, extra);
        itemCarritoExtraRepository.save(itemExtra);

        return item;
    }

    /**
     * Quita un extra asociado a un item del carrito.
     */
    public void quitarExtraItem(Long itemId, Long extraId) {
        ItemCarritoExtraId id = new ItemCarritoExtraId(itemId, extraId);
        if (!itemCarritoExtraRepository.existsById(id)) {
            throw new RuntimeException("El extra no está asociado a ese item");
        }
        itemCarritoExtraRepository.deleteById(id);
    }

    /**
     * Obtiene el carrito del usuario con todos los items y el total calculado.
     *
     * @return CarritoResponse con items y total
     */
    public CarritoResponse obtenerCarritoUsuario() {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        List<ItemCarritoResponse> items = carrito.getItems().stream()
                .map(i -> new ItemCarritoResponse(
                        i.getId(),
                        i.getProducto().getId(),
                        i.getProducto().getNombre(),
                        i.getProducto().getPrecioBase(),
                        i.getCantidad(),
                        i.getExtras().stream()
                                .map(e -> new ExtraProductoResponse(
                                        e.getExtra().getId(),
                                        e.getExtra().getNombre(),
                                        e.getExtra().getPrecio()
                                ))
                                .toList()
                ))
                .toList();

        double total = items.stream()
                .mapToDouble(ItemCarritoResponse::subtotal)
                .sum();

        return new CarritoResponse(items, total);
    }

    /**
     * Actualiza la cantidad de un producto dentro del carrito.
     */
    public void actualizarCantidad(Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero");
        }

        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        StockCentro stock = stockCentroService.obtenerPorCentroYProducto(
                usuario.getCentro().getId(),
                productoId
        );

        if (stock.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + stock.getStockActual());
        }

        item.setCantidad(cantidad);
        itemCarritoRepository.save(item);
    }

    /**
     * Elimina un producto del carrito.
     */
    public void eliminarProducto(Long productoId) {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        carrito.getItems().remove(item);
        carritoRepository.save(carrito);
        itemCarritoRepository.delete(item);
    }

    /**
     * Vacía completamente el carrito del usuario.
     */
    public void vaciarCarrito() {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        carrito.getItems().forEach(itemCarritoRepository::delete);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}
