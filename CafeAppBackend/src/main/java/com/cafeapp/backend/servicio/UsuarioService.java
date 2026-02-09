package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(null);
    }


    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(datos.getNombre());
            u.setEmail(datos.getEmail());
            if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
                u.setPassword(passwordEncoder.encode(datos.getPassword()));
            }
            return usuarioRepository.save(u);
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        return usuarioRepository.findById(id).map(u -> {
            usuarioRepository.delete(u);
            return true;
        }).orElse(false);
    }
}


