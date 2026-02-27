package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Alergeno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlergenoRepository extends JpaRepository<Alergeno, Long> {
}
