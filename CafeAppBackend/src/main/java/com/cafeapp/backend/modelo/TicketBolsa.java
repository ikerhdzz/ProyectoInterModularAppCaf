package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa un ticket de bolsa asociado a un pedido.
 *
 * Tabla relacionada: {@code ticket_bolsa}
 *
 * Se utiliza para imprimir bolsas identificadas con un código único.
 */
@Entity
@Table(name = "ticket_bolsa")
public class TicketBolsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long id;

    /** Pedido asociado a la bolsa. */
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    /** Código único de la bolsa. */
    @Column(name = "codigo_bolsa")
    private String codigoBolsa;

    /** Indica si la bolsa ya fue impresa. */
    private Boolean impreso;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public String getCodigoBolsa() { return codigoBolsa; }
    public void setCodigoBolsa(String codigoBolsa) { this.codigoBolsa = codigoBolsa; }

    public Boolean getImpreso() { return impreso; }
    public void setImpreso(Boolean impreso) { this.impreso = impreso; }
}
