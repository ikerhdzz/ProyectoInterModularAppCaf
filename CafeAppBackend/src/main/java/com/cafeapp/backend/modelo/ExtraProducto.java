package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "extra_producto")
public class ExtraProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extra")
    private Long id;

    private String nombre;
    private Double precio;

    public ExtraProducto() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
}
