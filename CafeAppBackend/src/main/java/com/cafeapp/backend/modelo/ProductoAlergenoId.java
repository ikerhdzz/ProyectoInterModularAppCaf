package com.cafeapp.backend.modelo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * Clave primaria compuesta para la tabla producto_alergeno.
 */
@Embeddable
public class ProductoAlergenoId implements Serializable {

    private Long idProducto;
    private Long idAlergeno;

    public ProductoAlergenoId() {}

    public ProductoAlergenoId(Long idProducto, Long idAlergeno) {
        this.idProducto = idProducto;
        this.idAlergeno = idAlergeno;
    }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public Long getIdAlergeno() { return idAlergeno; }
    public void setIdAlergeno(Long idAlergeno) { this.idAlergeno = idAlergeno; }
}
