package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.DetallePedidoResponse;
import com.cafeapp.backend.dto.ExtraDetalleResponse;
import com.cafeapp.backend.modelo.*;
import com.cafeapp.backend.repositorio.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final TurnoRepository turnoRepository;
    private final TicketRepository ticketRepository;
    private final ExtraProductoRepository extraProductoRepository;
    private final DetalleExtraRepository detalleExtraRepository;

    public PedidoService(UsuarioRepository usuarioRepository,
                         PedidoRepository pedidoRepository,
                         DetallePedidoRepository detallePedidoRepository,
                         CarritoRepository carritoRepository,
                         ItemCarritoRepository itemCarritoRepository,
                         TurnoRepository turnoRepository,
                         TicketRepository ticketRepository,
                         ExtraProductoRepository extraProductoRepository,
                         DetalleExtraRepository detalleExtraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.turnoRepository = turnoRepository;
        this.ticketRepository = ticketRepository;
        this.extraProductoRepository = extraProductoRepository;
        this.detalleExtraRepository = detalleExtraRepository;
    }

    private Usuario usuarioActual() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(email).orElseThrow();
    }

    private void validarTurno(Integer turnoId) {

        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        LocalTime limite = turno.getHoraLimite();

        LocalTime ahora = LocalTime.now(ZoneId.of("Atlantic/Canary"));

        if (ahora.isAfter(limite)) {
            throw new RuntimeException("El turno '" + turno.getNombre() + "' ya no está disponible");
        }
    }


    public Pedido crearPedidoDesdeCarrito(Integer turnoId) {

        validarTurno(turnoId);

        Usuario usuario = usuarioActual();
        Carrito carrito = carritoRepository.findByUsuario(usuario).orElseThrow();
        List<ItemCarrito> items = carrito.getItems();

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Pedido pedido = pedidoRepository.save(new Pedido(usuario, "PENDIENTE", turnoId));

        for (ItemCarrito item : items) {

            DetallePedido dp = detallePedidoRepository.save(
                    new DetallePedido(pedido, item.getProducto(), item.getCantidad())
            );

            if (item.getExtras() != null) {
                agregarExtras(dp, item.getExtras());
            }
        }

        items.forEach(itemCarritoRepository::delete);
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return pedido;
    }


    public List<Pedido> listarPedidosUsuario() {
        Usuario usuario = usuarioActual();
        return pedidoRepository.findByUsuario(usuario);
    }

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

            List<DetalleExtra> extras = detalleExtraRepository.findByIdDetalle(detalle.getId());

            List<ExtraDetalleResponse> extrasDto = extras.stream()
                    .map(e -> {
                        ExtraProducto extra = extraProductoRepository.findById(e.getIdExtra())
                                .orElseThrow(() -> new RuntimeException("Extra no encontrado"));
                        return new ExtraDetalleResponse(
                                extra.getId(),
                                extra.getNombre(),
                                extra.getPrecio()
                        );
                    })
                    .toList();

            dto.setExtras(extrasDto);

            double subtotal = calcularSubtotalConExtras(detalle, extras);
            dto.setSubtotal(subtotal);

            return dto;

        }).toList();
    }




    public Pedido cambiarEstado(Long pedidoId, String nuevoEstado) {

        List<String> estadosValidos = List.of("PENDIENTE", "PREPARANDO", "LISTO", "ENTREGADO");

        if (!estadosValidos.contains(nuevoEstado.toUpperCase())) {
            throw new RuntimeException("Estado no válido: " + nuevoEstado);
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado.toUpperCase());

        return pedidoRepository.save(pedido);
    }

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


    public void agregarExtras(DetallePedido detalle, List<Long> extrasIds) {

        for (Long extraId : extrasIds) {

            ExtraProducto extra = extraProductoRepository.findById(extraId)
                    .orElseThrow(() -> new RuntimeException("Extra no encontrado: " + extraId));

            DetalleExtra detalleExtra = new DetalleExtra(detalle.getId(), extra.getId());

            detalleExtraRepository.save(detalleExtra);
        }
    }


    private double calcularSubtotalConExtras(DetallePedido detalle, List<DetalleExtra> extras) {

        double subtotalProducto = detalle.getProducto().getPrecio() * detalle.getCantidad();

        double subtotalExtras = extras.stream()
                .mapToDouble(e -> extraProductoRepository.findById(e.getIdExtra())
                        .orElseThrow()
                        .getPrecio())
                .sum();

        return subtotalProducto + subtotalExtras;
    }

    public double calcularTotalPedido(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);

        return detalles.stream()
                .mapToDouble(detalle -> {
                    List<DetalleExtra> extras = detalleExtraRepository.findByIdDetalle(detalle.getId());
                    return calcularSubtotalConExtras(detalle, extras);
                })
                .sum();
    }


}
