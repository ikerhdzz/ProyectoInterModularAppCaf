package com.cafeapp.backend.modelo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clave primaria compuesta para la tabla item_carrito_extra.
 *
 * Representa la relación entre un item del carrito y un extra aplicado.
 *
 * Campos:
 * - idItem: ID del item del carrito.
 * - idExtra: ID del extra aplicado.
 */
@Embeddable
public class ItemCarritoExtraId implements Serializable {

    private Long idItem;
    private Long idExtra;

    public ItemCarritoExtraId() {}

    public ItemCarritoExtraId(Long idItem, Long idExtra) {
        this.idItem = idItem;
        this.idExtra = idExtra;
    }

    public Long getIdItem() { return idItem; }
    public void setIdItem(Long idItem) { this.idItem = idItem; }

    public Long getIdExtra() { return idExtra; }
    public void setIdExtra(Long idExtra) { this.idExtra = idExtra; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCarritoExtraId that)) return false;
        return Objects.equals(idItem, that.idItem) &&
                Objects.equals(idExtra, that.idExtra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItem, idExtra);
    }
}
