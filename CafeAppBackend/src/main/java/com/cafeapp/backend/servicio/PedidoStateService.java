package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.EstadoPedido;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.repositorio.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio encargado de gestionar los cambios de estado de un pedido.
 *
 * Reglas de transición:
 *
 * PENDIENTE → PREPARANDO | CANCELADO
 * PREPARANDO → LISTO | CANCELADO
 * LISTO → ENTREGADO | CANCELADO
 * ENTREGADO → (no puede cambiar)
 * CANCELADO → (no puede cambiar)
 */
@Service
public class PedidoStateService {

    private final PedidoRepository pedidoRepository;

    /**
     * Constructor con inyección del repositorio.
     */
    public PedidoStateService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Cambia el estado de un pedido validando la transición.
     *
     * @param pedidoId ID del pedido
     * @param nuevoEstadoStr nuevo estado en formato String
     * @return pedido actualizado
     */
    public Pedido cambiarEstado(Long pedidoId, String nuevoEstadoStr) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        EstadoPedido nuevoEstado = parseEstado(nuevoEstadoStr);
        validarTransicion(pedido.getEstado(), nuevoEstado);

        pedido.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoPedido.ENTREGADO) {
            pedido.setFechaEntrega(LocalDateTime.now());
        }

        return pedidoRepository.save(pedido);
    }

    /**
     * Valida si una transición de estado es permitida.
     */
    private void validarTransicion(EstadoPedido actual, EstadoPedido nuevo) {
        switch (actual) {
            case PENDIENTE -> {
                if (nuevo != EstadoPedido.PREPARANDO && nuevo != EstadoPedido.CANCELADO) {
                    throw new RuntimeException("Transición inválida: PENDIENTE → " + nuevo);
                }
            }
            case PREPARANDO -> {
                if (nuevo != EstadoPedido.LISTO && nuevo != EstadoPedido.CANCELADO) {
                    throw new RuntimeException("Transición inválida: PREPARANDO → " + nuevo);
                }
            }
            case LISTO -> {
                if (nuevo != EstadoPedido.ENTREGADO && nuevo != EstadoPedido.CANCELADO) {
                    throw new RuntimeException("Transición inválida: LISTO → " + nuevo);
                }
            }
            case ENTREGADO, CANCELADO -> {
                throw new RuntimeException("El pedido ya está cerrado y no puede cambiar de estado");
            }
        }
    }

    /**
     * Convierte un String a EstadoPedido.
     */
    private EstadoPedido parseEstado(String estado) {
        try {
            return EstadoPedido.valueOf(estado.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Estado inválido: " + estado);
        }
    }
}
