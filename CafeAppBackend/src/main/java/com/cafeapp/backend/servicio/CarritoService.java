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
    private final ItemCarritoExtraRepository itemCarritoExtraRepository;
    private final ProductoRepository productoRepository;
    private final ExtraProductoRepository extraProductoRepository;
    private final StockCentroService stockCentroService;

    public CarritoService(
            UsuarioRepository usuarioRepository,
            CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            ItemCarritoExtraRepository itemCarritoExtraRepository,
            ExtraProductoRepository extraProductoRepository,
            ProductoRepository productoRepository,
            StockCentroService stockCentroService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.itemCarritoExtraRepository = itemCarritoExtraRepository;
        this.extraProductoRepository = extraProductoRepository;
        this.productoRepository = productoRepository;
        this.stockCentroService = stockCentroService;
    }

    private Usuario usuarioActual() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(email).orElseThrow();
    }

    private Carrito obtenerOCrearCarrito(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> carritoRepository.save(new Carrito(usuario)));
    }

    // ============================================================
    // AGREGAR PRODUCTO (VALIDANDO STOCK)
    // ============================================================
    public void agregarProducto(Long productoId, int cantidad) {

        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // VALIDAR STOCK DEL CENTRO DEL USUARIO
        StockCentro stock = stockCentroService.obtenerStockProducto(
                usuario.getCentro().getId(),
                productoId
        );

        if (stock.getStockActual() < cantidad) {
            throw new RuntimeException(
                    "Stock insuficiente. Disponible: " + stock.getStockActual()
            );
        }

        // Buscar si ya existe el item en el carrito
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

        // Validar stock para la cantidad total
        int nuevaCantidad = item.getCantidad() + cantidad;

        if (stock.getStockActual() < nuevaCantidad) {
            throw new RuntimeException(
                    "No hay suficiente stock para aumentar la cantidad. Disponible: " + stock.getStockActual()
            );
        }

        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);
    }

    // ============================================================
    // AGREGAR EXTRA
    // ============================================================
    public ItemCarrito agregarExtraItem(Integer itemId, Long extraId) {

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        ExtraProducto extra = extraProductoRepository.findById(extraId)
                .orElseThrow(() -> new RuntimeException("Extra no encontrado"));

        ItemCarritoExtra itemExtra = new ItemCarritoExtra(item, extra);
        itemCarritoExtraRepository.save(itemExtra);

        return item;
    }

    // ============================================================
    // QUITAR EXTRA
    // ============================================================
    public void quitarExtraItem(Long itemId, Long extraId) {
        ItemCarritoExtraId id = new ItemCarritoExtraId(itemId, extraId);
        itemCarritoExtraRepository.deleteById(id);
    }

    // ============================================================
    // OBTENER CARRITO
    // ============================================================
    public CarritoResponse obtenerCarritoUsuario() {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        List<ItemCarritoResponse> items = carrito.getItems().stream()
                .map(i -> new ItemCarritoResponse(
                        i.getId(),
                        i.getProducto().getId(),
                        i.getProducto().getNombre(),
                        i.getProducto().getPrecio(),
                        i.getCantidad(),
                        i.getExtras().stream()
                                .map(e -> e.getExtra().getNombre())
                                .toList()
                ))
                .toList();

        return new CarritoResponse(items);
    }

    // ============================================================
    // ACTUALIZAR CANTIDAD (VALIDANDO STOCK)
    // ============================================================
    public void actualizarCantidad(Long productoId, int cantidad) {

        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow();

        // Validar stock
        StockCentro stock = stockCentroService.obtenerStockProducto(
                usuario.getCentro().getId(),
                productoId
        );

        if (stock.getStockActual() < cantidad) {
            throw new RuntimeException(
                    "Stock insuficiente. Disponible: " + stock.getStockActual()
            );
        }

        item.setCantidad(cantidad);
        itemCarritoRepository.save(item);
    }

    // ============================================================
    // ELIMINAR PRODUCTO
    // ============================================================
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

    // ============================================================
    // VACIAR CARRITO
    // ============================================================
    public void vaciarCarrito() {
        Usuario usuario = usuarioActual();
        Carrito carrito = obtenerOCrearCarrito(usuario);

        carrito.getItems().forEach(itemCarritoRepository::delete);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}
