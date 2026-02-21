package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}

