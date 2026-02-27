package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    private String nombre;

    @Column(name = "precio_base")
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = true)
    private String descripcion;

    @Column(name = "imagen_url", nullable = true)
    private String imagen;

    // ============================================================
    // RELACIÓN CON ALÉRGENOS
    // ============================================================
    @ManyToMany
    @JoinTable(
            name = "producto_alergeno",
            joinColumns = @JoinColumn(name = "id_producto"),
            inverseJoinColumns = @JoinColumn(name = "id_alergeno")
    )
    private List<Alergeno> alergenos = new ArrayList<>();

    public Producto() {}

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public List<Alergeno> getAlergenos() { return alergenos; }
    public void setAlergenos(List<Alergeno> alergenos) { this.alergenos = alergenos; }
}
