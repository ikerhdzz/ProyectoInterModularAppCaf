package com.cafeapp.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DetalleExtraId implements Serializable {

    @Column(name = "id_detalle")
    private Long detalleId;

    @Column(name = "id_extra")
    private Long extraId;

    public DetalleExtraId() {}

    public DetalleExtraId(Long detalleId, Long extraId) {
        this.detalleId = detalleId;
        this.extraId = extraId;
    }

    public Long getDetalleId() { return detalleId; }
    public Long getExtraId() { return extraId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleExtraId)) return false;
        DetalleExtraId that = (DetalleExtraId) o;
        return Objects.equals(detalleId, that.detalleId) &&
                Objects.equals(extraId, that.extraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(detalleId, extraId);
    }
}
