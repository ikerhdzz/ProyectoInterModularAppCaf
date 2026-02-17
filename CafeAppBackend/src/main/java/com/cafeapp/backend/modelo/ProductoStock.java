package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "producto_stock")
public class ProductoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", unique = true)
    private Long productoId;

    @Column(name = "stock")
    private Integer stock;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
