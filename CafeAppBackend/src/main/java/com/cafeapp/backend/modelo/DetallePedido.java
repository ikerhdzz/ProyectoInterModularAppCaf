package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public DetallePedido() {}

    public DetallePedido(Pedido pedido, Producto producto, Integer cantidad) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public Integer getCantidad() { return cantidad; }
    public Producto getProducto() { return producto; }
    public Pedido getPedido() { return pedido; }
}
