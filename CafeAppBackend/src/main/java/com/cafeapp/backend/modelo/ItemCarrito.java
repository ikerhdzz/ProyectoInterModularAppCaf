package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "items_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    @ElementCollection
    @CollectionTable(name = "carrito_item_extras", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "extra_id")
    private List<Long> extras;


    public ItemCarrito() {}

    public ItemCarrito(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public Integer getCantidad() { return cantidad; }
    public Producto getProducto() { return producto; }

    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public List<Long> getExtras() {
        return extras;
    }

    public void setExtras(List<Long> extras) {
        this.extras = extras;
    }
}
