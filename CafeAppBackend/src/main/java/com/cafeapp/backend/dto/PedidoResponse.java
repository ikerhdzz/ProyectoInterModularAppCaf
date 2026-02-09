package com.cafeapp.backend.dto;

import com.cafeapp.backend.modelo.Pedido;

import java.util.List;

public class PedidoResponse {

    private Long id;
    private String estado;
    private Integer turnoId;
    private String fecha;
    private List<DetallePedidoResponse> detalles;
    private Double total;

    public PedidoResponse(Pedido pedido,
                          List<DetallePedidoResponse> detalles,
                          Double total) {

        this.id = pedido.getId();
        this.estado = pedido.getEstado();
        this.turnoId = pedido.getTurnoId();
        this.fecha = pedido.getFecha().toString();
        this.detalles = detalles;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getTurnoId() {
        return turnoId;
    }

    public String getFecha() {
        return fecha;
    }

    public List<DetallePedidoResponse> getDetalles() {
        return detalles;
    }

    public Double getTotal() {
        return total;
    }
}
