package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un extra aplicado a un detalle de pedido.
 *
 * Tabla relacionada: {@code detalle_extra}
 *
 * Esta tabla almacena los extras añadidos a un producto dentro de un pedido,
 * junto con el precio histórico pagado por ese extra.
 */
@Entity
@Table(name = "detalle_extra")
public class DetalleExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_extra")
    private Long id;

    /**
     * Relación con el detalle del pedido al que pertenece este extra.
     * Muchos extras pueden pertenecer a un mismo detalle.
     */
    @ManyToOne
    @JoinColumn(name = "id_detalle")
    private DetallePedido detallePedido;

    /**
     * Extra aplicado al producto.
     */
    @ManyToOne
    @JoinColumn(name = "id_extra")
    private ExtraProducto extra;

    /** Precio unitario del extra en el momento del pedido. */
    @Column(name = "precio_unitario_extra")
    private Double precioUnitarioExtra;

    /** Precio final pagado por el extra (puede incluir descuentos). */
    @Column(name = "precio_pagado_extra")
    private Double precioPagadoExtra;

    // ============================
    // CONSTRUCTORES
    // ============================

    public DetalleExtra() {}

    public DetalleExtra(DetallePedido detallePedido, ExtraProducto extra) {
        this.detallePedido = detallePedido;
        this.extra = extra;
        this.precioUnitarioExtra = extra.getPrecio();
        this.precioPagadoExtra = extra.getPrecio();
    }

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }

    public DetallePedido getDetallePedido() { return detallePedido; }
    public void setDetallePedido(DetallePedido detallePedido) { this.detallePedido = detallePedido; }

    public ExtraProducto getExtra() { return extra; }
    public void setExtra(ExtraProducto extra) { this.extra = extra; }

    public Double getPrecioUnitarioExtra() { return precioUnitarioExtra; }
    public void setPrecioUnitarioExtra(Double precioUnitarioExtra) { this.precioUnitarioExtra = precioUnitarioExtra; }

    public Double getPrecioPagadoExtra() { return precioPagadoExtra; }
    public void setPrecioPagadoExtra(Double precioPagadoExtra) { this.precioPagadoExtra = precioPagadoExtra; }
}
