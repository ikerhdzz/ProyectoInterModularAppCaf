package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_extra")
public class DetalleExtra {

    @EmbeddedId
    private DetalleExtraId id;

    @ManyToOne
    @MapsId("detalleId")
    @JoinColumn(name = "id_detalle")
    private DetallePedido detalle;

    @ManyToOne
    @MapsId("extraId")
    @JoinColumn(name = "id_extra")
    private ExtraProducto extra;

    public DetalleExtra() {}

    public DetalleExtra(DetallePedido detalle, ExtraProducto extra) {
        this.detalle = detalle;
        this.extra = extra;
        this.id = new DetalleExtraId(detalle.getId(), extra.getId());
    }

    public DetalleExtraId getId() { return id; }
    public DetallePedido getDetalle() { return detalle; }
    public ExtraProducto getExtra() { return extra; }
}
