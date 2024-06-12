package com.example.diaescrito.entidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Entrada {
    private String titulo, contenido;
    private Date fecha;
    private Usuario usuario;
    private byte[] imagen;
    private int idEntrada;
    private Categoria categoria;

    public Entrada() {
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }
}
