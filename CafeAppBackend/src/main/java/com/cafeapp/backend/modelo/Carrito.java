package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @OneToMany
    @JoinTable(
            name = "carritos_items",
            joinColumns = @JoinColumn(name = "carrito_id"),
            inverseJoinColumns = @JoinColumn(name = "items_id")
    )
    private List<ItemCarrito> items = new ArrayList<>();


    public Carrito() {}

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public List<ItemCarrito> getItems() { return items; }
}

