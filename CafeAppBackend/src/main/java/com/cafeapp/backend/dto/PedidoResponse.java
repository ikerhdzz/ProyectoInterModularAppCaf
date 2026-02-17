package com.cafeapp.backend.dto;

import com.cafeapp.backend.modelo.Pedido;
import java.util.List;

public class PedidoResponse {

    private Long id;
    private String estado;
    private Integer turnoId;
    private String fecha;
    private List<DetallePedidoResponse> detalles;

    private Double subtotal;
    private Double impuesto;
    private Double total;

    public PedidoResponse(Pedido pedido,
                          List<DetallePedidoResponse> detalles,
                          PedidoTotales totales) {

        this.id = pedido.getId();
        this.estado = pedido.getEstado();
        this.turnoId = pedido.getTurnoId();
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

    public Double getSubtotal() { return subtotal; }
    public Double getImpuesto() { return impuesto; }
    public Double getTotal() { return total; }
}
