package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    @ManyToOne
    @JoinColumn(name = "centro_id")
    private Centro centro;

    public Pedido() {
        this.estado = EstadoPedido.PENDIENTE;
        this.fecha = LocalDateTime.now();
    }

    // ============================
    // GETTERS
    // ============================

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public EstadoPedido getEstado() { return estado; }
    public LocalDateTime getFecha() { return fecha; }
    public Turno getTurno() { return turno; }
    public Centro getCentro() { return centro; }

    // ============================
    // SETTERS (NUEVOS)
    // ============================

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }
}
