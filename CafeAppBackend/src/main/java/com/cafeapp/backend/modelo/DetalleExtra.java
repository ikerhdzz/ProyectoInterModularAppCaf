package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_extra")
public class DetalleExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_extra")
    private Long id;

    @Column(name = "id_detalle")
    private Long idDetalle;

    @Column(name = "id_extra")
    private Long idExtra;

    public DetalleExtra() {}

    public DetalleExtra(Long idDetalle, Long idExtra) {
        this.idDetalle = idDetalle;
        this.idExtra = idExtra;
    }

    public Long getId() {
        return id;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public Long getIdExtra() {
        return idExtra;
    }
}
