package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa un ticket generado a partir de un pedido.
 *
 * Tabla relacionada: {@code ticket}
 *
 * Incluye información histórica del pedido, del cliente, del centro y del empleado emisor.
 * El número de ticket se genera automáticamente mediante un trigger en la base de datos.
 */
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long id;

    /**
     * Pedido asociado al ticket.
     * Relación 1:1 (cada pedido solo puede tener un ticket).
     */
    @OneToOne
    @JoinColumn(name = "pedido_id", unique = true)
    private Pedido pedido;

    /** Número único del ticket (generado por trigger en BD). */
    @Column(name = "numero_ticket", unique = true)
    private String numeroTicket;

    /** Nombre del centro en el momento del pedido (copia histórica). */
    @Column(name = "nombre_centro_copia")
    private String nombreCentroCopia;

    /** Nombre del cliente en el momento del pedido (copia histórica). */
    @Column(name = "nombre_cliente_copia")
    private String nombreClienteCopia;

    /**
     * Empleado que emitió el ticket.
     * Puede ser null si el ticket se generó automáticamente.
     */
    @ManyToOne
    @JoinColumn(name = "id_empleado_emisor")
    private Usuario empleadoEmisor;

    /** Total pagado por el pedido. */
    @Column(name = "total_pagado")
    private BigDecimal totalPagado;

    /** Curso del cliente en el momento del pedido (copia histórica). */
    @Column(name = "curso_cliente_copia")
    private String cursoClienteCopia;

    /** Tipo de ticket (CONSUMO, RESERVA, etc.). */
    @Column(name = "tipo_ticket")
    private String tipoTicket;

    /** Fecha y hora de emisión del ticket. */
    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Ticket() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public String getNumeroTicket() { return numeroTicket; }
    public void setNumeroTicket(String numeroTicket) { this.numeroTicket = numeroTicket; }

    public String getNombreCentroCopia() { return nombreCentroCopia; }
    public void setNombreCentroCopia(String nombreCentroCopia) { this.nombreCentroCopia = nombreCentroCopia; }

    public String getNombreClienteCopia() { return nombreClienteCopia; }
    public void setNombreClienteCopia(String nombreClienteCopia) { this.nombreClienteCopia = nombreClienteCopia; }

    public Usuario getEmpleadoEmisor() { return empleadoEmisor; }
    public void setEmpleadoEmisor(Usuario empleadoEmisor) { this.empleadoEmisor = empleadoEmisor; }

    public BigDecimal getTotalPagado() { return totalPagado; }
    public void setTotalPagado(BigDecimal totalPagado) { this.totalPagado = totalPagado; }

    public String getCursoClienteCopia() { return cursoClienteCopia; }
    public void setCursoClienteCopia(String cursoClienteCopia) { this.cursoClienteCopia = cursoClienteCopia; }

    public String getTipoTicket() { return tipoTicket; }
    public void setTipoTicket(String tipoTicket) { this.tipoTicket = tipoTicket; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
}
