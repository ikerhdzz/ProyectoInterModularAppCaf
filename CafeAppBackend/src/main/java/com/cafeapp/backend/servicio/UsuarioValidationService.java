package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio de validación para usuario.
 * Contiene métodos de validación reutilizables.
 */
@Service
public class UsuarioValidationService {
    
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioValidationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * Valida que el email sea único en el sistema.
     * 
     * @param email el email a validar
     * @throws RuntimeException si el email ya está registrado
     */
    public void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado en el sistema");
        }
    }
    
    /**
     * Valida que el DNI sea único en el sistema (opcional, según requerimientos de negocio).
     * 
     * @param dni el DNI a validar
     * @throws RuntimeException si el DNI ya está registrado
     */
    public void validarDniUnico(String dni) {
        if (usuarioRepository.existsByDni(dni)) {
            throw new RuntimeException("El DNI ya está registrado en el sistema");
        }
    }
}
