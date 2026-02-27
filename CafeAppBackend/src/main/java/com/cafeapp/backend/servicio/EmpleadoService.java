package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.modelo.Rol;
import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import com.cafeapp.backend.repositorio.CentroRepository;
import com.cafeapp.backend.repositorio.RolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CentroRepository centroRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ============================================================
    // OBTENER EMPLEADOS POR CENTRO
    // ============================================================
    public List<Usuario> obtenerEmpleadosPorCentro(Integer centroId) {
        return usuarioRepository.findByRolIdAndCentroId(2, centroId); // Integer, no Long
    }

    // ============================================================
    // CREAR EMPLEADO
    // ============================================================
    public Usuario crearEmpleado(Usuario empleado, Integer centroId) {

        Centro centro = centroRepository.findById(centroId)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado"));

        Rol rolEmpleado = rolRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol empleado no encontrado"));

        empleado.setCentro(centro);
        empleado.setRol(rolEmpleado);
        empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));

        return usuarioRepository.save(empleado);
    }

    // ============================================================
    // CAMBIAR ROL
    // ============================================================
    public Usuario cambiarRol(Integer idUsuario, Integer nuevoRolId) {

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
    public Usuario cambiarCentro(Integer idUsuario, Integer nuevoCentroId) {

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
    public void eliminarEmpleado(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}
