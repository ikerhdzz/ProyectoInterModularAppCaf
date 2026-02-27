package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Centro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CentroRepository extends JpaRepository<Centro, Integer> {}

