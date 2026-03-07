package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.util.List;

/**
 * Representa un producto disponible para la venta.
 *
 * Tabla relacionada: {@code producto}
 *
 * Incluye información de categoría, precio, descripción y alérgenos asociados.
 */
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    /** Nombre del producto. */
    private String nombre;

    /** Precio base del producto. */
    @Column(name = "precio_base")
    private Double precioBase;

    /** URL de la imagen del producto. */
    @Column(name = "imagen_url")
    private String imagenUrl;

    /** Categoría del producto. */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /** Descripción del producto. */
    private String descripcion;

    /** Indica si el producto permite personalización. */
    @Column(name = "es_modificable")
    private Boolean esModificable;

    /**
     * Lista de alérgenos asociados al producto.
     * Relación Many-to-Many mediante la tabla producto_alergeno.
     */
    @ManyToMany
    @JoinTable(
            name = "producto_alergeno",
            joinColumns = @JoinColumn(name = "id_producto"),
            inverseJoinColumns = @JoinColumn(name = "id_alergeno")
    )
    private List<Alergeno> alergenos;

    public Producto() {}

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecioBase() { return precioBase; }
    public void setPrecioBase(Double precioBase) { this.precioBase = precioBase; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getEsModificable() { return esModificable; }
    public void setEsModificable(Boolean esModificable) { this.esModificable = esModificable; }

    public List<Alergeno> getAlergenos() { return alergenos; }
    public void setAlergenos(List<Alergeno> alergenos) { this.alergenos = alergenos; }

    // Métodos auxiliares usados en servicios
    public Double getPrecio() { return precioBase; }
    public String getImagen() { return imagenUrl; }
}
