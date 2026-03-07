package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un curso dentro de un centro educativo.
 *
 * Tabla relacionada: {@code curso}
 */
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long id;

    /** Nombre del curso (Ej: 1ºA, 2ºB, ESO3...). */
    private String nombre;

    /**
     * Centro al que pertenece el curso.
     * Relación N:1 con Centro.
     */
    @ManyToOne
    @JoinColumn(name = "centro_id")
    private Centro centro;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Centro getCentro() { return centro; }
    public void setCentro(Centro centro) { this.centro = centro; }
}
