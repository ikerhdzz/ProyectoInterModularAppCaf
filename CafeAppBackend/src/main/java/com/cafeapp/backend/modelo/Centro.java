package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un centro educativo.
 *
 * Tabla relacionada: {@code centro}
 */
@Entity
@Table(name = "centro")
public class Centro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centro")
    private Long id;

    private String nombre;
    private String codigo;
    private String direccion;
    private String telefono;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
