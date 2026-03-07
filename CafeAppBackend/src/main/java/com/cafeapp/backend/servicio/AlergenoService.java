package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Alergeno;
import com.cafeapp.backend.repositorio.AlergenoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar la lógica relacionada con los alérgenos.
 *
 * Funcionalidades:
 * - Listar alérgenos
 * - Crear nuevos alérgenos
 * - Eliminar alérgenos existentes
 */
@Service
public class AlergenoService {

    private final AlergenoRepository alergenoRepository;

    /**
     * Constructor que inyecta el repositorio de alérgenos.
     */
    public AlergenoService(AlergenoRepository alergenoRepository) {
        this.alergenoRepository = alergenoRepository;
    }

    /**
     * Obtiene la lista completa de alérgenos.
     *
     * @return lista de alérgenos
     */
    public List<Alergeno> listar() {
        return alergenoRepository.findAll();
    }

    /**
     * Crea un nuevo alérgeno.
     *
     * @param alergeno objeto alérgeno a guardar
     * @return alérgeno guardado
     */
    public Alergeno crear(Alergeno alergeno) {
        return alergenoRepository.save(alergeno);
    }

    /**
     * Elimina un alérgeno por su ID.
     *
     * @param id identificador del alérgeno
     * @throws RuntimeException si el alérgeno no existe
     */
    public void eliminar(Long id) {
        if (!alergenoRepository.existsById(id)) {
            throw new RuntimeException("Alérgeno no encontrado");
        }
        alergenoRepository.deleteById(id);
    }
}
