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

    private String estado;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "turno_id")
    private Integer turnoId;

    public Pedido() {}

    public Pedido(Usuario usuario, String estado, Integer turnoId) {
        this.usuario = usuario;
        this.estado = estado;
        this.turnoId = turnoId;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFecha() { return fecha; }
    public Integer getTurnoId() { return turnoId; }
}
