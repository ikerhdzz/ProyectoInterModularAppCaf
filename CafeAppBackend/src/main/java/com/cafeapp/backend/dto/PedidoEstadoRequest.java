package com.cafeapp.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class PedidoEstadoRequest {

    @NotBlank
    private String estado;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
