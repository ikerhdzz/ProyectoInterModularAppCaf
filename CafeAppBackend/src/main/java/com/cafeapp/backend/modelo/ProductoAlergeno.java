package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa la relación entre un producto y un alérgeno.
 *
 * Tabla relacionada: {@code producto_alergeno}
 *
 * Incluye el tipo de presencia del alérgeno (CONTIENE o TRAZAS).
 */
@Entity
@Table(name = "producto_alergeno")
public class ProductoAlergeno {

    @EmbeddedId
    private ProductoAlergenoId id;

    @ManyToOne
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @MapsId("idAlergeno")
    @JoinColumn(name = "id_alergeno")
    private Alergeno alergeno;

    /** Tipo de presencia del alérgeno (CONTIENE / TRAZAS). */
    @Column(name = "tipo_presencia")
    private String tipoPresencia;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public ProductoAlergenoId getId() { return id; }
    public void setId(ProductoAlergenoId id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Alergeno getAlergeno() { return alergeno; }
    public void setAlergeno(Alergeno alergeno) { this.alergeno = alergeno; }

    public String getTipoPresencia() { return tipoPresencia; }
    public void setTipoPresencia(String tipoPresencia) { this.tipoPresencia = tipoPresencia; }
}
