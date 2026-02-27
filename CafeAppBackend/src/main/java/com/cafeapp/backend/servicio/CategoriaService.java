package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Categoria;
import com.cafeapp.backend.repositorio.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listar() {
        return repo.findAll();
    }
}
