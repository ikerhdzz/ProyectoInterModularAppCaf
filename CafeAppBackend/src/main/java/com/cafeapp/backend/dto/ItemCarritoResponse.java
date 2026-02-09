package com.cafeapp.backend.dto;

public class ItemCarritoResponse {

    private Long productoId;
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private Double subtotal;

    public ItemCarritoResponse(Long productoId, String nombre, Double precio, Integer cantidad) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = precio * cantidad;
    }

    public Long getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
    public Integer getCantidad() { return cantidad; }
    public Double getSubtotal() { return subtotal; }
}
