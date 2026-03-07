package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.time.LocalTime;

/**
 * Representa un turno de recogida de pedidos.
 *
 * Tabla relacionada: {@code turno}
 *
 * Ejemplos: "Desayuno", "Recreo", "Almuerzo".
 */
@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long id;

    /** Nombre del turno. */
    private String nombre;

    /** Hora límite para realizar pedidos en este turno. */
    @Column(name = "hora_limite")
    private LocalTime horaLimite;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalTime getHoraLimite() { return horaLimite; }
    public void setHoraLimite(LocalTime horaLimite) { this.horaLimite = horaLimite; }
}
