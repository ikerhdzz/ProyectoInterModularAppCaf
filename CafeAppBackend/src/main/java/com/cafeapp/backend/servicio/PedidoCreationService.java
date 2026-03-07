package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.pedido.PedidoFrontendRequest;
import com.cafeapp.backend.modelo.*;
import com.cafeapp.backend.repositorio.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Servicio encargado de la creación de pedidos desde distintas fuentes:
 *
 * - Desde el carrito del usuario
 * - Desde el frontend (pedido directo sin carrito)
 *
 * También valida turnos, descuenta stock y genera detalles y extras.
 */
@Service
public class PedidoCreationService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final TurnoRepository turnoRepository;
    private final ExtraProductoRepository extraProductoRepository;
    private final DetalleExtraRepository detalleExtraRepository;
    private final ProductoRepository productoRepository;
    private final StockCentroService stockCentroService;

    /**
     * Constructor con inyección de dependencias.
     */
    public PedidoCreationService(
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            TurnoRepository turnoRepository,
            ExtraProductoRepository extraProductoRepository,
            DetalleExtraRepository detalleExtraRepository,
            ProductoRepository productoRepository,
            StockCentroService stockCentroService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.turnoRepository = turnoRepository;
        this.extraProductoRepository = extraProductoRepository;
        this.detalleExtraRepository = detalleExtraRepository;
        this.productoRepository = productoRepository;
        this.stockCentroService = stockCentroService;
    }

    // ============================================================
    // VALIDACIÓN DE TURNO
    // ============================================================

    /**
     * Valida si un turno está disponible según su hora límite.
     *
     * Reglas:
     * - Si el turno no tiene hora límite → siempre válido.
     * - Entre 00:00 y 05:00 se permite pedir aunque el turno esté vencido.
     * - Si la hora actual supera la hora límite → turno no disponible.
     *
     * @param turnoId ID del turno a validar
     */
    private void validarTurno(Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        LocalTime limite = turno.getHoraLimite();
        LocalTime ahora = LocalTime.now(ZoneId.of("Atlantic/Canary"));

        if (limite == null) return;

        // Excepción especial entre 00:00 y 05:00
        if (ahora.isAfter(LocalTime.of(0, 0)) && ahora.isBefore(LocalTime.of(5, 0))) return;

        if (ahora.isAfter(limite)) {
            throw new RuntimeException("El turno '" + turno.getNombre() + "' ya no está disponible");
        }
    }

    // ============================================================
    // CREAR PEDIDO DESDE CARRITO
    // ============================================================

    /**
     * Crea un pedido utilizando los productos del carrito del usuario.
     *
     * Pasos:
     * 1. Validar turno
     * 2. Obtener carrito
     * 3. Crear pedido
     * 4. Convertir cada item del carrito en un DetallePedido
     * 5. Descontar stock
     * 6. Vaciar carrito
     *
     * @param usuario usuario que realiza el pedido
     * @param turnoId turno seleccionado
     * @return pedido creado
     */
    public Pedido crearDesdeCarrito(Usuario usuario, Long turnoId) {
        validarTurno(turnoId);

        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        List<ItemCarrito> items = carrito.getItems();
        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDate.now());
        pedido.setHoraPedido(LocalDateTime.now());
        pedido.setUsuario(usuario);
        pedido.setTurno(turnoRepository.findById(turnoId).orElseThrow());
        pedido.setCentro(usuario.getCentro());

        pedido = pedidoRepository.save(pedido);

        for (ItemCarrito item : items) {
            procesarItem(pedido, usuario, item);
        }

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return pedido;
    }

    // ============================================================
    // CREAR PEDIDO DESDE FRONTEND (SIN CARRITO)
    // ============================================================

    /**
     * Crea un pedido directamente desde el frontend sin usar el carrito.
     *
     * @param usuario usuario que realiza el pedido
     * @param request DTO con productos, extras y notas
     * @return pedido creado
     */
    public Pedido crearDesdeFrontend(Usuario usuario, PedidoFrontendRequest request) {
        validarTurno(request.turnoId());

        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDate.now());
        pedido.setHoraPedido(LocalDateTime.now());
        pedido.setUsuario(usuario);
        pedido.setTurno(turnoRepository.findById(request.turnoId()).orElseThrow());
        pedido.setCentro(usuario.getCentro());

        pedido = pedidoRepository.save(pedido);

        for (PedidoFrontendRequest.ItemPedidoFrontend item : request.items()) {

            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetallePedido detalle = new DetallePedido(pedido, producto, item.cantidad());
            detalle.setNotasPersonalizacion(item.notasPersonalizacion());
            detalle = detallePedidoRepository.save(detalle);

            stockCentroService.restarStock(
                    usuario.getCentro().getId(),
                    producto.getId(),
                    item.cantidad()
            );

            if (item.extras() != null && !item.extras().isEmpty()) {
                agregarExtras(detalle, item.extras());
            }
        }

        return pedido;
    }

    // ============================================================
    // PROCESAR ITEM DEL CARRITO
    // ============================================================

    /**
     * Convierte un item del carrito en un DetallePedido.
     *
     * @param pedido pedido al que pertenece
     * @param usuario usuario que compra
     * @param item item del carrito
     */
    private void procesarItem(Pedido pedido, Usuario usuario, ItemCarrito item) {

        DetallePedido detalle = new DetallePedido(pedido, item.getProducto(), item.getCantidad());
        detalle = detallePedidoRepository.save(detalle);

        stockCentroService.restarStock(
                usuario.getCentro().getId(),
                item.getProducto().getId(),
                item.getCantidad()
        );

        if (item.getExtras() != null && !item.getExtras().isEmpty()) {
            List<Long> extrasIds = item.getExtras().stream()
                    .map(e -> e.getExtra().getId())
                    .toList();
            agregarExtras(detalle, extrasIds);
        }
    }

    // ============================================================
    // AGREGAR EXTRAS A UN DETALLE
    // ============================================================

    /**
     * Agrega extras a un detalle de pedido.
     *
     * @param detalle detalle al que se agregan extras
     * @param extrasIds lista de IDs de extras
     */
    private void agregarExtras(DetallePedido detalle, List<Long> extrasIds) {
        for (Long extraId : extrasIds) {
            ExtraProducto extra = extraProductoRepository.findById(extraId)
                    .orElseThrow(() -> new RuntimeException("Extra no encontrado: " + extraId));

            DetalleExtra detalleExtra = new DetalleExtra(detalle, extra);
            detalleExtraRepository.save(detalleExtra);
        }
    }
}
