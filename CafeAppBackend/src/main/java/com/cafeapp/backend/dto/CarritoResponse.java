package com.cafeapp.backend.dto;

import java.util.List;

public class CarritoResponse {

    private List<ItemCarritoResponse> items;
    private Double total;

    public CarritoResponse(List<ItemCarritoResponse> items) {
        this.items = items;
        this.total = items.stream()
                .mapToDouble(ItemCarritoResponse::getSubtotal)
                .sum();
    }

    public List<ItemCarritoResponse> getItems() { return items; }
    public Double getTotal() { return total; }
}
