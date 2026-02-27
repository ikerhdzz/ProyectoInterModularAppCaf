package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.DetallePedidoResponse;
import com.cafeapp.backend.dto.ExtraDetalleResponse;
import com.cafeapp.backend.dto.PedidoFrontendRequest;
import com.cafeapp.backend.dto.PedidoTotales;
import com.cafeapp.backend.modelo.*;
import com.cafeapp.backend.repositorio.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final TurnoRepository turnoRepository;
    private final TicketRepository ticketRepository;
    private final ExtraProductoRepository extraProductoRepository;
    private final DetalleExtraRepository detalleExtraRepository;
    private final ProductoRepository productoRepository;
    private final StockCentroRepository stockCentroRepository;

    public PedidoServiceImpl(
            UsuarioRepository usuarioRepository,
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            TurnoRepository turnoRepository,
            TicketRepository ticketRepository,
            ExtraProductoRepository extraProductoRepository,
            DetalleExtraRepository detalleExtraRepository,
            ProductoRepository productoRepository,
            StockCentroRepository stockCentroRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.turnoRepository = turnoRepository;
        this.ticketRepository = ticketRepository;
        this.extraProductoRepository = extraProductoRepository;
        this.detalleExtraRepository = detalleExtraRepository;
        this.productoRepository = productoRepository;
        this.stockCentroRepository = stockCentroRepository;
    }

    // ============================================================
    // USUARIO ACTUAL
    // ============================================================
    private Usuario usuarioActual() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(email).orElseThrow();
    }

    // ============================================================
    // VALIDAR TURNO
    // ============================================================
    private void validarTurno(Integer turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        LocalTime limite = turno.getHoraLimite();
        LocalTime ahora = LocalTime.now(ZoneId.of("Atlantic/Canary"));

        if (limite == null) return;

        if (ahora.isAfter(LocalTime.of(0, 0)) && ahora.isBefore(LocalTime.of(5, 0))) return;

        if (ahora.isAfter(limite)) {
            throw new RuntimeException("El turno '" + turno.getNombre() + "' ya no está disponible");
        }
    }

    // ============================================================
    // DESCONTAR STOCK
    // ============================================================
    private void descontarStock(Usuario usuario, Producto producto, int cantidad) {

        StockCentro stock = stockCentroRepository
                .findByCentroIdAndProductoId(usuario.getCentro().getId(), producto.getId())
                .orElseThrow(() -> new RuntimeException(
                        "No hay stock configurado para el producto '" + producto.getNombre() + "' en este centro"
                ));

        if (stock.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        stock.setStockActual(stock.getStockActual() - cantidad);
        stockCentroRepository.save(stock);
    }

    // ============================================================
    // CREAR DETALLE + EXTRAS + STOCK
    // ============================================================
    private void procesarItemPedido(Pedido pedido, Usuario usuario, ItemCarrito item) {

        DetallePedido detalle = detallePedidoRepository.save(
                new DetallePedido(pedido, item.getProducto(), item.getCantidad())
        );

        descontarStock(usuario, item.getProducto(), item.getCantidad());

        if (item.getExtras() != null && !item.getExtras().isEmpty()) {
            agregarExtras(detalle, item.getExtras().stream()
                    .map(e -> e.getExtra().getId())
                    .toList());
        }
    }

    // ============================================================
    // AGREGAR EXTRAS
    // ============================================================
    @Override
    public void agregarExtras(DetallePedido detalle, List<Long> extrasIds) {

        for (Long extraId : extrasIds) {

            ExtraProducto extra = extraProductoRepository.findById(extraId)
                    .orElseThrow(() -> new RuntimeException("Extra no encontrado: " + extraId));

            DetalleExtra detalleExtra = new DetalleExtra(detalle, extra);

            detalleExtraRepository.save(detalleExtra);
        }
    }

    // ============================================================
    // CREAR PEDIDO DESDE CARRITO
    // ============================================================
    @Override
    public Pedido crearPedidoDesdeCarrito(Integer turnoId) {

        validarTurno(turnoId);

        Usuario usuario = usuarioActual();
        Carrito carrito = carritoRepository.findByUsuario(usuario).orElseThrow();
        List<ItemCarrito> items = carrito.getItems();

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDateTime.now());
        pedido.setUsuario(usuario);
        pedido.setTurno(turnoRepository.findById(turnoId).orElseThrow());
        pedido.setCentro(usuario.getCentro());

        pedido = pedidoRepository.save(pedido);

        for (ItemCarrito item : items) {
            procesarItemPedido(pedido, usuario, item);
        }

        items.forEach(itemCarritoRepository::delete);
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return pedido;
    }

    // ============================================================
    // LISTAR PEDIDOS DEL USUARIO
    // ============================================================
    @Override
    public List<Pedido> listarPedidosUsuario() {
        Usuario usuario = usuarioActual();
        return pedidoRepository.findByUsuarioId(usuario.getId());
    }

    // ============================================================
    // DETALLES DEL PEDIDO
    // ============================================================
    @Override
    public List<DetallePedidoResponse> obtenerDetallesPedido(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);

        return detalles.stream().map(detalle -> {

            DetallePedidoResponse dto = new DetallePedidoResponse(
                    detalle.getProducto().getId(),
                    detalle.getProducto().getNombre(),
                    detalle.getProducto().getPrecio(),
                    detalle.getCantidad()
            );

            List<DetalleExtra> extras = detalleExtraRepository.findByDetalle(detalle);

            List<ExtraDetalleResponse> extrasDto = extras.stream()
                    .map(e -> new ExtraDetalleResponse(
                            e.getExtra().getId(),
                            e.getExtra().getNombre(),
                            e.getExtra().getPrecio()
                    ))
                    .toList();

            dto.setExtras(extrasDto);

            double subtotal = calcularSubtotalConExtras(detalle, extras);
            dto.setSubtotal(subtotal);

            return dto;

        }).toList();
    }

    private double calcularSubtotalConExtras(DetallePedido detalle, List<DetalleExtra> extras) {

        double subtotalProducto = detalle.getProducto().getPrecio() * detalle.getCantidad();

        double subtotalExtras = extras.stream()
                .mapToDouble(e -> e.getExtra().getPrecio())
                .sum();

        return subtotalProducto + subtotalExtras;
    }

    // ============================================================
    // CAMBIAR ESTADO
    // ============================================================
    @Override
    public Pedido cambiarEstado(Long pedidoId, String nuevoEstado) {

        EstadoPedido estado = EstadoPedido.valueOf(nuevoEstado.toUpperCase());

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(estado);

        return pedidoRepository.save(pedido);
    }

    // ============================================================
    // GENERAR TICKET
    // ============================================================
    @Override
    public Ticket generarTicket(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        ticketRepository.findByPedido(pedido).ifPresent(t -> {
            throw new RuntimeException("Este pedido ya tiene un ticket generado");
        });

        String codigo = "TICKET-" + pedido.getId() + "-" + UUID.randomUUID();

        Ticket ticket = new Ticket(codigo, false, pedido);

        return ticketRepository.save(ticket);
    }

    // ============================================================
    // TOTALES
    // ============================================================
    @Override
    public double calcularTotalPedido(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);

        return detalles.stream()
                .mapToDouble(detalle -> {
                    List<DetalleExtra> extras = detalleExtraRepository.findByDetalle(detalle);
                    return calcularSubtotalConExtras(detalle, extras);
                })
                .sum();
    }

    @Override
    public PedidoTotales calcularTotalesPedido(Long pedidoId) {

        double subtotal = calcularTotalPedido(pedidoId);
        double impuesto = subtotal * 0.07;
        double total = subtotal + impuesto;

        return new PedidoTotales(subtotal, impuesto, total);
    }

    // ============================================================
    // EXPIRAR PEDIDOS
    // ============================================================
    @Override
    public void expirarPedidos() {

        var estadosActivos = Arrays.asList(
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_PREPARACION,
                EstadoPedido.LISTO
        );

        var pedidos = pedidoRepository.findAll();

        for (Pedido p : pedidos) {
            if (estadosActivos.contains(p.getEstado())) {
                if (p.getFecha().isBefore(LocalDateTime.now().minusMinutes(30))) {
                    p.setEstado(EstadoPedido.EXPIRADO);
                    pedidoRepository.save(p);
                }
            }
        }
    }

    // ============================================================
    // CREAR PEDIDO DESDE FRONTEND
    // ============================================================
    @Override
    public Pedido crearPedidoDesdeFrontend(PedidoFrontendRequest request) {

        validarTurno(request.getTurnoId());
        Usuario usuario = usuarioActual();

        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDateTime.now());
        pedido.setUsuario(usuario);
        pedido.setTurno(turnoRepository.findById(request.getTurnoId()).orElseThrow());
        pedido.setCentro(usuario.getCentro());

        pedido = pedidoRepository.save(pedido);

        for (PedidoFrontendRequest.ItemPedidoFrontend item : request.getItems()) {

            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetallePedido detalle = new DetallePedido(pedido, producto, item.getCantidad());
            detallePedidoRepository.save(detalle);

            descontarStock(usuario, producto, item.getCantidad());
        }

        return pedido;
    }

    @Override
    public List<Pedido> listarPedidosPorCentro(Integer centroId) {
        return pedidoRepository.findByCentroIdAndEstadoIn(
                centroId,
                List.of(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PREPARACION,
                        EstadoPedido.LISTO
                )
        );
    }
}
