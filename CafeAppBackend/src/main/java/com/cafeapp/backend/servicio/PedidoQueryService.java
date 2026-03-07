package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.EstadoPedido;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.repositorio.PedidoRepository;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de consultar pedidos según distintos criterios.
 *
 * Funcionalidades:
 * - Obtener pedido por ID
 * - Listar pedidos de un usuario
 * - Listar pedidos por centro
 * - Listar pedidos por estado
 * - Listar pedidos por turno
 */
@Service
public class PedidoQueryService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     */
    public PedidoQueryService(
            PedidoRepository pedidoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene un pedido por su ID.
     *
     * @param id ID del pedido
     * @return pedido encontrado
     * @throws RuntimeException si no existe
     */
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    /**
     * Lista todos los pedidos realizados por un usuario.
     *
     * @param usuarioId ID del usuario
     * @return lista de pedidos ordenados por fecha descendente
     */
    public List<Pedido> listarPedidosUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pedidoRepository.findByUsuarioOrderByFechaDesc(usuario);
    }

    /**
     * Lista pedidos asociados a un centro.
     */
    public List<Pedido> listarPedidosPorCentro(Long centroId) {
        return pedidoRepository.findByCentroIdOrderByFechaDesc(centroId);
    }

    /**
     * Lista pedidos filtrados por estado.
     *
     * @param estado nombre del estado (string)
     * @return lista de pedidos
     */
    public List<Pedido> listarPorEstado(String estado) {
        EstadoPedido estadoEnum = EstadoPedido.valueOf(estado.toUpperCase());
        return pedidoRepository.findByEstadoOrderByFechaDesc(estadoEnum);
    }

    /**
     * Lista pedidos filtrados por turno.
     */
    public List<Pedido> listarPorTurno(Long turnoId) {
        return pedidoRepository.findByTurnoIdOrderByFechaDesc(turnoId);
    }
}
