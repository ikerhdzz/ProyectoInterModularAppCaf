package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un producto dentro del carrito de un usuario.
 *
 * Tabla relacionada: {@code item_carrito}
 */
@Entity
@Table(name = "item_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;

    /** Carrito al que pertenece este item. */
    @ManyToOne
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    /** Producto añadido al carrito. */
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    /** Cantidad del producto. */
    private Integer cantidad;

    /**
     * Lista de extras añadidos a este item.
     * Relación 1:N con ItemCarritoExtra.
     */
    @OneToMany(mappedBy = "itemCarrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarritoExtra> extras = new ArrayList<>();

    // ============================
    // CONSTRUCTORES
    // ============================

    public ItemCarrito() {}

    public ItemCarrito(Carrito carrito, Producto producto, Integer cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public List<ItemCarritoExtra> getExtras() { return extras; }
    public void setExtras(List<ItemCarritoExtra> extras) { this.extras = extras; }
}
