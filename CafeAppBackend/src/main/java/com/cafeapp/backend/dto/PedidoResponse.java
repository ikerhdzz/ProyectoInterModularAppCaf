package com.cafeapp.backend.dto;

import com.cafeapp.backend.modelo.Pedido;

import java.util.List;

public class PedidoResponse {

    private Long id;
    private String estado;
    private Integer turnoId;
    private String fecha;
    private List<DetallePedidoResponse> detalles;
    private double subtotal;
    private double impuesto;
    private double total;

    public PedidoResponse(Pedido pedido,
                          List<DetallePedidoResponse> detalles,
                          PedidoTotales totales) {

        this.id = pedido.getId();

        //  Convertimos enum → String
        this.estado = pedido.getEstado().name();

        //  Ahora turno es un objeto, no un Integer
        this.turnoId = pedido.getTurno().getId();

        this.fecha = pedido.getFecha().toString();
        this.detalles = detalles;

        this.subtotal = totales.getSubtotal();
        this.impuesto = totales.getImpuesto();
        this.total = totales.getTotal();
    }

    public Long getId() { return id; }
    public String getEstado() { return estado; }
    public Integer getTurnoId() { return turnoId; }
    public String getFecha() { return fecha; }
    public List<DetallePedidoResponse> getDetalles() { return detalles; }
    public double getSubtotal() { return subtotal; }
    public double getImpuesto() { return impuesto; }
    public double getTotal() { return total; }
}
