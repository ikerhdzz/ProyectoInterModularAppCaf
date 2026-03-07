package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un rol dentro del sistema (ADMIN, EMPLEADO, ALUMNO, PADRE...).
 *
 * Tabla relacionada: {@code rol}
 */
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    /** Nombre del rol. */
    private String nombre;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
