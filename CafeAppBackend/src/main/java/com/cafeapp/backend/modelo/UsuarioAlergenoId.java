package com.cafeapp.backend.modelo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * Clave primaria compuesta para la tabla usuario_alergeno.
 */
@Embeddable
public class UsuarioAlergenoId implements Serializable {

    private Long idUsuario;
    private Long idAlergeno;

    public UsuarioAlergenoId() {}

    public UsuarioAlergenoId(Long idUsuario, Long idAlergeno) {
        this.idUsuario = idUsuario;
        this.idAlergeno = idAlergeno;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Long getIdAlergeno() { return idAlergeno; }
    public void setIdAlergeno(Long idAlergeno) { this.idAlergeno = idAlergeno; }
}
