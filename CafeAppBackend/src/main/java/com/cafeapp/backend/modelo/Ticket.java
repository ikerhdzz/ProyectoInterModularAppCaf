package com.cafeapp.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;



import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long id;

    @Column(name = "codigo_qr")
    private String codigoQr;

    private Boolean canjeada;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public Ticket() {}

    public Ticket(String codigoQr, Boolean canjeada, Pedido pedido) {
        this.codigoQr = codigoQr;
        this.canjeada = canjeada;
        this.pedido = pedido;
    }

    public Long getId() { return id; }
    public String getCodigoQr() { return codigoQr; }
    public Boolean getCanjeada() { return canjeada; }
    public void setCanjeada(Boolean canjeada) { this.canjeada = canjeada; }
    public Pedido getPedido() { return pedido; }
}

