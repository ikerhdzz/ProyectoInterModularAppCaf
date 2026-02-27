package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "alergeno")
public class Alergeno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alergeno")
    private Long id;

    private String nombre;

    private String descripcion;

    public Alergeno() {}

    public Alergeno(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
