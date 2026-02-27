package com.cafeapp.backend.modelo;


import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemCarritoExtraId implements Serializable {

    @Column(name = "id_item")
    private Long itemId;

    @Column(name = "id_extra")
    private Long extraId;

    public ItemCarritoExtraId() {}

    public ItemCarritoExtraId(Long itemId, Long extraId) {
        this.itemId = itemId;
        this.extraId = extraId;
    }

    public Long getItemId() { return itemId; }
    public Long getExtraId() { return extraId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCarritoExtraId)) return false;
        ItemCarritoExtraId that = (ItemCarritoExtraId) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(extraId, that.extraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, extraId);
    }
}

