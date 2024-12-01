package com.blackcode.dg.model;

/**
 * @author marko
 */
public class Galleta {

    int idGalleta;
    String tipo;
    String sabor;
    int cantidad;
    String fechaCad;
    int estatus;

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getIdGalleta() {
        return idGalleta;
    }

    public void setIdGalleta(int idGalleta) {
        this.idGalleta = idGalleta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public String getFechaCad() {
        return fechaCad;
    }

    public void setFechaCad(String fechaCad) {
        this.fechaCad = fechaCad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
