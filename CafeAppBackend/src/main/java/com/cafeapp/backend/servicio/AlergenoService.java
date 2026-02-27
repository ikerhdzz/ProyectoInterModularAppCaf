package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Alergeno;
import com.cafeapp.backend.repositorio.AlergenoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlergenoService {

    private final AlergenoRepository alergenoRepository;

    public AlergenoService(AlergenoRepository alergenoRepository) {
        this.alergenoRepository = alergenoRepository;
    }

    public List<Alergeno> listar() {
        return alergenoRepository.findAll();
    }

    public Alergeno crear(Alergeno alergeno) {
        return alergenoRepository.save(alergeno);
    }

    public void eliminar(Long id) {
        alergenoRepository.deleteById(id);
    }
}
