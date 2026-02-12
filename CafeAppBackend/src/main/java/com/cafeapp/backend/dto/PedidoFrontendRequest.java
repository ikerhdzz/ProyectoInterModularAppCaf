package com.cafeapp.backend.dto;

import java.util.List;

public class PedidoFrontendRequest {

    private Integer turnoId;
    private List<ItemPedidoFrontend> items;

    public PedidoFrontendRequest() {}

    public Integer getTurnoId() { return turnoId; }
    public void setTurnoId(Integer turnoId) { this.turnoId = turnoId; }

    public List<ItemPedidoFrontend> getItems() { return items; }
    public void setItems(List<ItemPedidoFrontend> items) { this.items = items; }

    public static class ItemPedidoFrontend {

        private Long productoId;
        private int cantidad;

        public ItemPedidoFrontend() {}

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}
