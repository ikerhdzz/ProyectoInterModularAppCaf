package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un alérgeno presente en productos o asociado a usuarios.
 *
 * Tabla relacionada: {@code alergeno}
 *
 * Campos:
 * - id: Identificador único del alérgeno.
 * - nombre: Nombre del alérgeno (gluten, lactosa, frutos secos...).
 * - esTrazaComun: Indica si es un alérgeno común por trazas.
 */
@Entity
@Table(name = "alergeno")
public class Alergeno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alergeno")
    private Long id;

    /** Nombre del alérgeno. */
    private String nombre;

    /** Indica si este alérgeno suele aparecer como traza común. */
    @Column(name = "es_traza_comun")
    private Boolean esTrazaComun;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getEsTrazaComun() { return esTrazaComun; }
    public void setEsTrazaComun(Boolean esTrazaComun) { this.esTrazaComun = esTrazaComun; }
}
