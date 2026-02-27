package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarritoExtra> extras = new ArrayList<>();

    public ItemCarrito() {}

    public ItemCarrito(Carrito carrito, Producto producto, Integer cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public Carrito getCarrito() { return carrito; }
    public Producto getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public List<ItemCarritoExtra> getExtras() { return extras; }

    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
