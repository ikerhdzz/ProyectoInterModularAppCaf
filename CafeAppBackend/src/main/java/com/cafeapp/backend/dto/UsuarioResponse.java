package com.cafeapp.backend.dto;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private String curso;

    public UsuarioResponse(Long id, String nombre, String email, String rol, String curso) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.curso = curso;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getCurso() { return curso; }
}
