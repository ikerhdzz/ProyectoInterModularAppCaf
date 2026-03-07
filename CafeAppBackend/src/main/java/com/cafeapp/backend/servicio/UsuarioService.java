package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.usuario.UsuarioRequest;
import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.modelo.Curso;
import com.cafeapp.backend.modelo.Rol;
import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.repositorio.CentroRepository;
import com.cafeapp.backend.repositorio.CursoRepository;
import com.cafeapp.backend.repositorio.RolRepository;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar usuarios dentro del sistema.
 *
 * Funcionalidades principales:
 * - Consultar usuarios (por ID, email, rol, centro, curso)
 * - Obtener usuario autenticado
 * - Crear, actualizar y eliminar usuarios
 * - Mapear datos desde DTOs
 *
 * Este servicio es utilizado por múltiples módulos:
 * - Autenticación
 * - Gestión de empleados
 * - Pedidos
 * - Administración
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CursoRepository cursoRepository;
    private final CentroRepository centroRepository;

    /**
     * Constructor con inyección de dependencias.
     */
    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            CursoRepository cursoRepository,
            CentroRepository centroRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.cursoRepository = cursoRepository;
        this.centroRepository = centroRepository;
    }

    // ============================================================
    // MÉTODOS DE CONSULTA
    // ============================================================

    /**
     * Lista todos los usuarios del sistema.
     *
     * @return lista completa de usuarios
     */
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return usuario encontrado
     * @throws RuntimeException si no existe
     */
    public Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Obtiene un usuario por su email.
     *
     * @param email email del usuario
     * @return usuario encontrado
     */
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    /**
     * Obtiene el usuario actualmente autenticado mediante Spring Security.
     *
     * @return usuario autenticado
     */
    public Usuario obtenerUsuarioActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return obtenerPorEmail(email);
    }

    // ============================================================
    // CRUD
    // ============================================================

    /**
     * Guarda un usuario directamente en la base de datos.
     *
     * @param usuario entidad usuario
     * @return usuario guardado
     */
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Crea un usuario a partir de un DTO.
     *
     * @param request datos del usuario
     * @return usuario creado
     */
    public Usuario crear(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        mapearDatos(usuario, request);
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id ID del usuario
     * @param request DTO con datos actualizados
     * @return usuario actualizado
     */
    public Usuario actualizar(Long id, UsuarioRequest request) {
        Usuario usuario = obtener(id);
        mapearDatos(usuario, request);
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id identificador del usuario
     */
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Lista usuarios filtrados por nombre de rol.
     */
    public List<Usuario> listarPorRol(String rol) {
        return usuarioRepository.findByRolNombreIgnoreCase(rol);
    }

    /**
     * Lista usuarios pertenecientes a un centro.
     */
    public List<Usuario> listarPorCentro(Long centroId) {
        return usuarioRepository.findByCentroId(centroId);
    }

    /**
     * Lista usuarios pertenecientes a un curso.
     */
    public List<Usuario> listarPorCurso(Long cursoId) {
        return usuarioRepository.findByCursoId(cursoId);
    }

    // ============================================================
    // MAPEO
    // ============================================================

    /**
     * Mapea los datos del DTO {@link UsuarioRequest} a la entidad {@link Usuario}.
     *
     * Este método:
     * - Asigna datos básicos (nombre, email, dni, clase)
     * - Asigna rol
     * - Asigna curso (si aplica)
     * - Asigna centro (si aplica)
     * - Asigna idPadre (si aplica)
     *
     * @param usuario entidad a modificar
     * @param request DTO con datos nuevos
     */
    private void mapearDatos(Usuario usuario, UsuarioRequest request) {

        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setDni(request.dni());
        usuario.setClase(request.clase());
        usuario.setPerfilCompletado(request.perfilCompletado());

        // Curso (opcional)
        if (request.cursoId() != null) {
            Curso curso = cursoRepository.findById(request.cursoId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            usuario.setCurso(curso);
        }

        // Rol (obligatorio)
        Rol rol = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);

        // Centro (opcional)
        if (request.centroId() != null) {
            Centro centro = centroRepository.findById(request.centroId())
                    .orElseThrow(() -> new RuntimeException("Centro no encontrado"));
            usuario.setCentro(centro);
        }

        // Padre (opcional)
        usuario.setIdPadre(request.idPadre());
    }
}
