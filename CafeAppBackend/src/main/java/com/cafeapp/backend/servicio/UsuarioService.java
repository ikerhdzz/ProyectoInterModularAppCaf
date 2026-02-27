package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ============================================================
    // OBTENER POR EMAIL (NECESARIO PARA LOGIN)
    // ============================================================
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    // ============================================================
    // GUARDAR
    // ============================================================
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElse(null);
    }

    // ============================================================
    // ACTUALIZAR
    // ============================================================
    public Usuario actualizar(Integer id, Usuario datos) {
        return usuarioRepository.findById(id)
                .map(u -> {
                    u.setNombre(datos.getNombre());
                    u.setEmail(datos.getEmail());
                    u.setPassword(datos.getPassword());
                    u.setRol(datos.getRol());
                    u.setCentro(datos.getCentro());
                    return usuarioRepository.save(u);
                })
                .orElse(null);
    }

    // ============================================================
    // ELIMINAR
    // ============================================================
    public boolean eliminar(Integer id) {
        return usuarioRepository.findById(id)
                .map(u -> {
                    usuarioRepository.delete(u);
                    return true;
                })
                .orElse(false);
    }

    // ============================================================
    // LISTAR
    // ============================================================
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
}
