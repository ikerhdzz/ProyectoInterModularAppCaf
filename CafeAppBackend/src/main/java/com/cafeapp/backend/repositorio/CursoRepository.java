package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {}


