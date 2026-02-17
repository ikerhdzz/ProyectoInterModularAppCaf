package com.cafeapp.backend.dto;

public class ProductoResponse {

    private Long id;
    private String nombre;
    private Double precio;
    private String imagen;
    private Long categoriaId;
    private String categoriaNombre;

    public ProductoResponse(Long id, String nombre, Double precio, String imagen, Long categoriaId, String categoriaNombre) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
    public String getImagen() { return imagen; }
    public Long getCategoriaId() {
        return categoriaId;
    }
    public String getCategoriaNombre() {
        return categoriaNombre;
    }

}
