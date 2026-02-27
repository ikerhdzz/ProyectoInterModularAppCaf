package com.cafeapp.backend.dto;

import com.cafeapp.backend.modelo.Centro;
import com.cafeapp.backend.modelo.Rol;
import com.cafeapp.backend.modelo.Usuario;

public class UsuarioResponse {
    private Integer id;
    private String nombre;
    private String email;
    private Rol rol;
    private Centro centro;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.rol = usuario.getRol();
        this.centro = usuario.getCentro();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }




}
