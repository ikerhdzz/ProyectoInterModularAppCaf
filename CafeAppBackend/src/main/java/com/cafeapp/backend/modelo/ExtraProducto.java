package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un extra que puede añadirse a un producto.
 *
 * Tabla relacionada: {@code extra_producto}
 */
@Entity
@Table(name = "extra_producto")
public class ExtraProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extra")
    private Long id;

    /** Nombre del extra (queso, bacon, salsa...). */
    private String nombre;

    /** Precio del extra. */
    private Double precio;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
