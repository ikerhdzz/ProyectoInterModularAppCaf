package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "item_carrito_extra")
public class ItemCarritoExtra {

    @EmbeddedId
    private ItemCarritoExtraId id;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "id_item")
    private ItemCarrito item;

    @ManyToOne
    @MapsId("extraId")
    @JoinColumn(name = "id_extra")
    private ExtraProducto extra;

    public ItemCarritoExtra() {}

    public ItemCarritoExtra(ItemCarrito item, ExtraProducto extra) {
        this.item = item;
        this.extra = extra;
        this.id = new ItemCarritoExtraId(item.getId(), extra.getId());
    }

    public ItemCarritoExtraId getId() { return id; }
    public ItemCarrito getItem() { return item; }
    public ExtraProducto getExtra() { return extra; }
}
