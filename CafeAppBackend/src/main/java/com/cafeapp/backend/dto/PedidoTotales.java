package com.cafeapp.backend.dto;

public class PedidoTotales {

    private double subtotal;
    private double impuesto;
    private double total;

    public PedidoTotales(double subtotal, double impuesto, double total) {
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
    }

    public double getSubtotal() { return subtotal; }
    public double getImpuesto() { return impuesto; }
    public double getTotal() { return total; }
}
