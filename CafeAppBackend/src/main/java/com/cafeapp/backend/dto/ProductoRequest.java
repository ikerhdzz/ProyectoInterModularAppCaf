package com.cafeapp.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductoRequest {

    @JsonAlias({"id_producto", "id"})
    private Long id;

    @NotBlank
    @JsonAlias({"nombre"})
    private String nombre;

    @NotNull
    @JsonAlias({"precio", "precio_base"})
    private Double precio;

    @JsonAlias({"imagen", "imagen_url"})
    private String imagen;

    @JsonAlias({"categoriaId", "categoria_id"})
    private Long categoriaId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
}
