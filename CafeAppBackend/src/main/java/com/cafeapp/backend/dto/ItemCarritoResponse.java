package com.cafeapp.backend.dto;

import java.util.List;

public class ItemCarritoResponse {

    private Long itemId;
    private Long productoId;
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private List<String> extras;

    public ItemCarritoResponse(Long itemId,
                               Long productoId,
                               String nombre,
                               Double precio,
                               Integer cantidad,
                               List<String> extras) {
        this.itemId = itemId;
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.extras = extras;
    }

    public Long getItemId() { return itemId; }
    public Long getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
    public Integer getCantidad() { return cantidad; }
    public List<String> getExtras() { return extras; }

    // ============================================================
    // SUBTOTAL (OBLIGATORIO PARA CarritoResponse)
    // ============================================================
    public Double getSubtotal() {
        return precio * cantidad;
    }
}
