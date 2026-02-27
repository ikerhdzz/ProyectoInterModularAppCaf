package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(
        name = "stock_centro",
        uniqueConstraints = @UniqueConstraint(columnNames = {"centro_id", "producto_id"})
)
public class StockCentro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "centro_id", nullable = false)
    private Centro centro;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "alerta_stock", nullable = false)
    private Integer alertaStock;

    public StockCentro() {}

    public StockCentro(Centro centro, Producto producto, Integer stockActual, Integer alertaStock) {
        this.centro = centro;
        this.producto = producto;
        this.stockActual = stockActual;
        this.alertaStock = alertaStock;
    }

    public Long getId() { return id; }
    public Centro getCentro() { return centro; }
    public Producto getProducto() { return producto; }
    public Integer getStockActual() { return stockActual; }
    public Integer getAlertaStock() { return alertaStock; }

    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
}
