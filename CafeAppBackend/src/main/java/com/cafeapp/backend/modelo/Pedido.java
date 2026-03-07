package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa un pedido realizado por un usuario.
 *
 * Tabla relacionada: {@code pedido}
 *
 * Contiene información del estado, usuario, turno, centro y fechas relevantes.
 */
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    /** Estado actual del pedido. */
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    /** Fecha del pedido (DATE en BD). */
    private LocalDate fecha;

    /** Fecha de entrega del pedido. */
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    /** Hora en la que se realizó el pedido. */
    @Column(name = "hora_pedido")
    private LocalDateTime horaPedido;

    /** Número de reserva opcional. */
    @Column(name = "numero_reserva")
    private Integer numeroReserva;

    /** Usuario que realiza el pedido. */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /** Beneficiario del pedido (opcional). */
    @ManyToOne
    @JoinColumn(name = "beneficiario_id")
    private Usuario beneficiario;

    /** Turno asociado al pedido. */
    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    /** Centro donde se realiza el pedido. */
    @ManyToOne
    @JoinColumn(name = "centro_id")
    private Centro centro;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public LocalDateTime getHoraPedido() { return horaPedido; }
    public void setHoraPedido(LocalDateTime horaPedido) { this.horaPedido = horaPedido; }

    public Integer getNumeroReserva() { return numeroReserva; }
    public void setNumeroReserva(Integer numeroReserva) { this.numeroReserva = numeroReserva; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Usuario getBeneficiario() { return beneficiario; }
    public void setBeneficiario(Usuario beneficiario) { this.beneficiario = beneficiario; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }

    public Centro getCentro() { return centro; }
    public void setCentro(Centro centro) { this.centro = centro; }
}
