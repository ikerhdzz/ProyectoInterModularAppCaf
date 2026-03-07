package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el carrito de compra de un usuario.
 *
 * Tabla relacionada: {@code carrito}
 *
 * Relación importante:
 * - Un usuario solo puede tener un carrito (1:1).
 * - Un carrito contiene múltiples items (1:N).
 */
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;

    /**
     * Usuario propietario del carrito.
     * Relación 1:1 con la tabla usuario.
     */
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    /**
     * Lista de items dentro del carrito.
     * Relación 1:N con ItemCarrito.
     */
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();

    // ============================
    // CONSTRUCTORES
    // ============================

    public Carrito() {}

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<ItemCarrito> getItems() { return items; }
    public void setItems(List<ItemCarrito> items) { this.items = items; }
}
