package com.cafeapp.backend.repositorio;

import com.cafeapp.backend.modelo.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
}
