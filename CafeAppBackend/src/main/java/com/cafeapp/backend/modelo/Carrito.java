package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito() {}

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public List<ItemCarrito> getItems() { return items; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
