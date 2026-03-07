package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa el stock de un producto en un centro educativo.
 *
 * Tabla relacionada: {@code stock_centro}
 */
@Entity
@Table(name = "stock_centro")
public class StockCentro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long id;

    /** Centro al que pertenece este stock. */
    @ManyToOne
    @JoinColumn(name = "centro_id")
    private Centro centro;

    /** Producto cuyo stock se controla. */
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    /** Cantidad actual en stock. */
    @Column(name = "stock_actual")
    private Integer stockActual;

    /** Nivel de alerta para avisar de stock bajo. */
    @Column(name = "alerta_stock")
    private Integer alertaStock;

    public StockCentro() {}

    public StockCentro(Centro centro, Producto producto, Integer stockInicial, Integer alertaStock) {
        this.centro = centro;
        this.producto = producto;
        this.stockActual = stockInicial;
        this.alertaStock = alertaStock;
    }

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Centro getCentro() { return centro; }
    public void setCentro(Centro centro) { this.centro = centro; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getAlertaStock() { return alertaStock; }
    public void setAlertaStock(Integer alertaStock) { this.alertaStock = alertaStock; }

    /** Indica si el stock está por debajo del nivel de alerta. */
    public boolean isEnAlerta() {
        return stockActual != null && alertaStock != null && stockActual <= alertaStock;
    }
}
