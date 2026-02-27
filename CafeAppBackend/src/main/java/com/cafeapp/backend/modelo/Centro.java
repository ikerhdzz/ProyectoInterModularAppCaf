package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "centro")
public class Centro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centro")
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String codigo;

    private String direccion;

    private String telefono;

    public Centro() {}

    public Centro(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // GETTERS
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }

    // SETTERS
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
