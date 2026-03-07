package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.modelo.Rol;
import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import com.cafeapp.backend.repositorio.CentroRepository;
import com.cafeapp.backend.repositorio.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar empleados dentro del sistema.
 *
 * Funcionalidades:
 * - Listar empleados por centro
 * - Crear empleados
 * - Cambiar rol
 * - Cambiar centro
 * - Eliminar empleados
 */
@Service
public class EmpleadoService {

    private final UsuarioRepository usuarioRepository;
    private final CentroRepository centroRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor con inyección de dependencias.
     */
    public EmpleadoService(
            UsuarioRepository usuarioRepository,
            CentroRepository centroRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.centroRepository = centroRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // LISTAR EMPLEADOS POR CENTRO
    // ============================================================

    /**
     * Obtiene todos los empleados de un centro específico.
     *
     * @param centroId ID del centro
     * @return lista de empleados
     */
    public List<Usuario> obtenerEmpleadosPorCentro(Long centroId) {
        return usuarioRepository.findByRolIdAndCentroId(2L, centroId);
    }

    // ============================================================
    // CREAR EMPLEADO
    // ============================================================

    /**
     * Crea un nuevo empleado asignándolo a un centro y al rol EMPLEADO.
     *
     * @param empleado datos del empleado
     * @param centroId ID del centro al que pertenece
     * @return empleado creado
     */
    public Usuario crearEmpleado(Usuario empleado, Long centroId) {

        Centro centro = centroRepository.findById(centroId)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado"));

        Rol rolEmpleado = rolRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Rol empleado no encontrado"));

        empleado.setCentro(centro);
        empleado.setRol(rolEmpleado);
        empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));

        return usuarioRepository.save(empleado);
    }

    // ============================================================
    // CAMBIAR ROL
    // ============================================================

    /**
     * Cambia el rol de un usuario.
     *
     * @param idUsuario ID del usuario
     * @param nuevoRolId ID del nuevo rol
     * @return usuario actualizado
     */
    public Usuario cambiarRol(Long idUsuario, Long nuevoRolId) {

        Usuario u = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol nuevoRol = rolRepository.findById(nuevoRolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        u.setRol(nuevoRol);
        return usuarioRepository.save(u);
    }

    // ============================================================
    // CAMBIAR CENTRO
    // ============================================================

    /**
     * Cambia el centro asignado a un usuario.
     *
     * @param idUsuario ID del usuario
     * @param nuevoCentroId ID del nuevo centro
     * @return usuario actualizado
     */
    public Usuario cambiarCentro(Long idUsuario, Long nuevoCentroId) {

        Usuario u = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Centro centro = centroRepository.findById(nuevoCentroId)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado"));

        u.setCentro(centro);
        return usuarioRepository.save(u);
    }

    // ============================================================
    // ELIMINAR EMPLEADO
    // ============================================================

    /**
     * Elimina un empleado del sistema.
     *
     * @param idUsuario ID del usuario a eliminar
     */
    public void eliminarEmpleado(Long idUsuario) {

        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(idUsuario);
    }
}
