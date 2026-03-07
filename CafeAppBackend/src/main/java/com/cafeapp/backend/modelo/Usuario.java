package com.cafeapp.backend.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un usuario del sistema.
 *
 * Tabla relacionada: {@code usuario}
 *
 * Puede ser alumno, empleado, administrador o padre.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    /** DNI del usuario. */
    @NotBlank
    private String dni;

    /** Clase del alumno (solo para usuarios tipo estudiante). */
    private String clase;

    /** Nombre completo del usuario. */
    @NotBlank
    private String nombre;

    /** Email único del usuario. */
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    /** Contraseña encriptada. */
    @NotBlank
    private String password;

    /** URL de la imagen de perfil. */
    @Column(name = "imagen_url")
    private String imagenUrl;

    /** Curso al que pertenece el usuario (si es alumno). */
    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    /** Rol del usuario (ADMIN, EMPLEADO, ALUMNO...). */
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    /** Centro educativo al que pertenece el usuario. */
    @ManyToOne
    @JoinColumn(name = "centro_id")
    private Centro centro;

    /**
     * Lista de alérgenos asociados al usuario.
     * Relación Many-to-Many mediante usuario_alergeno.
     */
    @ManyToMany
    @JoinTable(
            name = "usuario_alergeno",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_alergeno")
    )
    private List<Alergeno> alergenos = new ArrayList<>();

    /** Indica si el usuario completó su perfil. */
    @Column(name = "perfil_completado")
    private boolean perfilCompletado;

    /**
     * ID del padre/tutor (solo para usuarios menores).
     * Relación autorreferenciada.
     */
    @Column(name = "id_padre")
    private Long idPadre;

    // ============================
    // GETTERS Y SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getClase() { return clase; }
    public void setClase(String clase) { this.clase = clase; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Centro getCentro() { return centro; }
    public void setCentro(Centro centro) { this.centro = centro; }

    public List<Alergeno> getAlergenos() { return alergenos; }
    public void setAlergenos(List<Alergeno> alergenos) { this.alergenos = alergenos; }

    public boolean isPerfilCompletado() { return perfilCompletado; }
    public void setPerfilCompletado(boolean perfilCompletado) { this.perfilCompletado = perfilCompletado; }

    public Long getIdPadre() { return idPadre; }
    public void setIdPadre(Long idPadre) { this.idPadre = idPadre; }
}
