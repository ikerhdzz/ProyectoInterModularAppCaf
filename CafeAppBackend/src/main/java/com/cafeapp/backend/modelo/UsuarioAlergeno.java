package com.cafeapp.backend.modelo;

import jakarta.persistence.*;

/**
 * Representa la relación entre un usuario y un alérgeno.
 *
 * Tabla relacionada: {@code usuario_alergeno}
 *
 * Permite indicar si existe riesgo de contaminación cruzada.
 */
@Entity
@Table(name = "usuario_alergeno")
public class UsuarioAlergeno {

    @EmbeddedId
    private UsuarioAlergenoId id;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idAlergeno")
    @JoinColumn(name = "id_alergeno")
    private Alergeno alergeno;

    /** Indica si existe riesgo de contaminación cruzada. */
    @Column(name = "riesgo_contaminacion_cruzada")
    private Boolean riesgoContaminacionCruzada;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public UsuarioAlergenoId getId() { return id; }
    public void setId(UsuarioAlergenoId id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Alergeno getAlergeno() { return alergeno; }
    public void setAlergeno(Alergeno alergeno) { this.alergeno = alergeno; }

    public Boolean getRiesgoContaminacionCruzada() { return riesgoContaminacionCruzada; }
    public void setRiesgoContaminacionCruzada(Boolean riesgoContaminacionCruzada) {
        this.riesgoContaminacionCruzada = riesgoContaminacionCruzada;
    }
}
