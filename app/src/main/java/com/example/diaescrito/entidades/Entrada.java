package com.example.diaescrito.entidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Entrada {
    private String titulo, contenido;
    private Date fecha;
    private Usuario usuario;
    private byte[] imagen;

    public Entrada(String titulo, String contenido, Date fecha, Usuario usuario, byte[] imagen) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.usuario = usuario;
        this.imagen = imagen;
    }

    public Entrada(String titulo, String contenido, Date fecha, Usuario usuario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean hasImage() {
        return imagen != null && imagen.length > 0;
    }
    public String getFechaFormateada() {
        return convertirFecha(this.fecha);
    }
    private String convertirFecha(Date fechaOriginal) {
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
        return formatoSalida.format(fechaOriginal);
    }

}
