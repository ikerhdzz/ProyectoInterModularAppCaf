package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.centro.CentroRequest;
import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.repositorio.CentroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar los centros educativos.
 *
 * Funcionalidades:
 * - Listar centros
 * - Obtener centro por ID
 * - Crear centro
 * - Actualizar centro
 * - Eliminar centro
 */
@Service
public class CentroService {

    private final CentroRepository centroRepository;

    /**
     * Constructor con inyección del repositorio.
     */
    public CentroService(CentroRepository centroRepository) {
        this.centroRepository = centroRepository;
    }

    /**
     * Obtiene la lista completa de centros.
     *
     * @return lista de centros
     */
    public List<Centro> listar() {
        return centroRepository.findAll();
    }

    /**
     * Obtiene un centro por su ID.
     *
     * @param id identificador del centro
     * @return centro encontrado
     * @throws RuntimeException si no existe
     */
    public Centro obtener(Long id) {
        return centroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro no encontrado"));
    }

    /**
     * Crea un nuevo centro a partir de un DTO.
     *
     * @param request datos del centro
     * @return centro creado
     */
    public Centro crear(CentroRequest request) {
        Centro centro = new Centro();
        mapearDatos(centro, request);
        return centroRepository.save(centro);
    }

    /**
     * Actualiza un centro existente.
     *
     * @param id ID del centro
     * @param request datos actualizados
     * @return centro actualizado
     */
    public Centro actualizar(Long id, CentroRequest request) {
        Centro centro = obtener(id);
        mapearDatos(centro, request);
        return centroRepository.save(centro);
    }

    /**
     * Elimina un centro por ID.
     *
     * @param id identificador del centro
     */
    public void eliminar(Long id) {
        if (!centroRepository.existsById(id)) {
            throw new RuntimeException("Centro no encontrado");
        }
        centroRepository.deleteById(id);
    }

    /**
     * Mapea los datos del DTO al modelo Centro.
     */
    private void mapearDatos(Centro centro, CentroRequest request) {
        centro.setNombre(request.nombre());
        centro.setCodigo(request.codigo());
        centro.setDireccion(request.direccion());
        centro.setTelefono(request.telefono());
    }
}
