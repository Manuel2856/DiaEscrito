package com.example.diaescrito.entidades;

public class Usuario {
    private String nombre,email,contrasena;
    private int idUsuario;

    public Usuario(String nombre, String email, String contrasena, int idUsuario) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.idUsuario = idUsuario;
    }
    public Usuario(String nombre, String email, String contrasena) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
