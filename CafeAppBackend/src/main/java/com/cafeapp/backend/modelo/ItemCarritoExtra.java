package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un extra aplicado a un item del carrito.
 *
 * Tabla relacionada: {@code item_carrito_extra}
 *
 * Usa clave compuesta mediante {@link ItemCarritoExtraId}.
 */
@Entity
@Table(name = "item_carrito_extra")
public class ItemCarritoExtra {

    @EmbeddedId
    private ItemCarritoExtraId id;

    /** Item del carrito al que pertenece este extra. */
    @ManyToOne
    @MapsId("idItem")
    @JoinColumn(name = "id_item")
    private ItemCarrito itemCarrito;

    /** Extra aplicado al item. */
    @ManyToOne
    @MapsId("idExtra")
    @JoinColumn(name = "id_extra")
    private ExtraProducto extra;

    // ============================
    // CONSTRUCTORES
    // ============================

    public ItemCarritoExtra() {}

    public ItemCarritoExtra(ItemCarrito itemCarrito, ExtraProducto extra) {
        this.itemCarrito = itemCarrito;
        this.extra = extra;
        this.id = new ItemCarritoExtraId(itemCarrito.getId(), extra.getId());
    }

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public ItemCarritoExtraId getId() { return id; }
    public void setId(ItemCarritoExtraId id) { this.id = id; }

    public ItemCarrito getItemCarrito() { return itemCarrito; }
    public void setItemCarrito(ItemCarrito itemCarrito) { this.itemCarrito = itemCarrito; }

    public ExtraProducto getExtra() { return extra; }
    public void setExtra(ExtraProducto extra) { this.extra = extra; }
}
