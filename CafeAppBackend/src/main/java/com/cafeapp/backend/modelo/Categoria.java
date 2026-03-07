package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa una categoría de productos (bebidas, bocadillos, dulces...).
 *
 * Tabla relacionada: {@code categoria}
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;

    /** Nombre de la categoría. */
    private String nombre;

    /** URL del icono representativo de la categoría. */
    @Column(name = "icono_url")
    private String iconoUrl;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIconoUrl() { return iconoUrl; }
    public void setIconoUrl(String iconoUrl) { this.iconoUrl = iconoUrl; }
}
