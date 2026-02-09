package com.cafeapp.backend.dto;

import java.util.List;

public class DetallePedidoResponse {

    private Long productoId;
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private Double subtotal;
    private List<ExtraDetalleResponse> extras;

    public DetallePedidoResponse(Long productoId, String nombre, Double precio, Integer cantidad) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = precio * cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public List<ExtraDetalleResponse> getExtras() {
        return extras;
    }

    public void setExtras(List<ExtraDetalleResponse> extras) {
        this.extras = extras;
    }
}
