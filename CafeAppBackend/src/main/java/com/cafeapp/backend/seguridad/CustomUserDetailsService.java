package com.cafeapp.backend.seguridad;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación personalizada de {@link UserDetailsService} para Spring Security.
 *
 * Esta clase se encarga de cargar un usuario desde la base de datos
 * utilizando su email como identificador, y convertirlo en un objeto
 * {@link UserDetails} que Spring Security pueda utilizar para autenticación.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor que inyecta el repositorio de usuarios.
     */
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga un usuario por su email y lo adapta al modelo de seguridad de Spring.
     *
     * @param email Email del usuario (username en este sistema)
     * @return UserDetails con email, contraseña y rol
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Spring Security requiere roles con prefijo "ROLE_"
        String rol = "ROLE_" + usuario.getRol().getNombre().toUpperCase();

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority(rol))
        );
    }
}
