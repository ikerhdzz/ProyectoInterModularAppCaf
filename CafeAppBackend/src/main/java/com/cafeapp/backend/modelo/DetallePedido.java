package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un producto dentro de un pedido.
 *
 * Tabla relacionada: {@code detalle_pedido}
 *
 * Contiene información histórica del precio del producto en el momento del pedido.
 */
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    /**
     * Pedido al que pertenece este detalle.
     */
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    /**
     * Producto asociado a este detalle.
     */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    /** Cantidad del producto. */
    private Integer cantidad;

    /** Notas de personalización del usuario. */
    @Column(name = "notas_personalizacion")
    private String notasPersonalizacion;

    /** Precio unitario del producto en el momento del pedido. */
    @Column(name = "precio_unitario_producto")
    private Double precioUnitarioProducto;

    /** Precio final pagado por unidad (puede incluir descuentos). */
    @Column(name = "precio_pagado_unidad")
    private Double precioPagadoUnidad;

    // ============================
    // CONSTRUCTORES
    // ============================

    public DetallePedido() {}

    public DetallePedido(Pedido pedido, Producto producto, Integer cantidad) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;

        this.precioUnitarioProducto = producto.getPrecioBase();
        this.precioPagadoUnidad = producto.getPrecioBase();
    }

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getNotasPersonalizacion() { return notasPersonalizacion; }
    public void setNotasPersonalizacion(String notasPersonalizacion) { this.notasPersonalizacion = notasPersonalizacion; }

    public Double getPrecioUnitarioProducto() { return precioUnitarioProducto; }
    public void setPrecioUnitarioProducto(Double precioUnitarioProducto) { this.precioUnitarioProducto = precioUnitarioProducto; }

    public Double getPrecioPagadoUnidad() { return precioPagadoUnidad; }
    public void setPrecioPagadoUnidad(Double precioPagadoUnidad) { this.precioPagadoUnidad = precioPagadoUnidad; }
}
