package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Integer id;

    private String nombre;

    @Column(name = "hora_limite")
    private LocalTime horaLimite;

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalTime getHoraLimite() { return horaLimite; }
}
