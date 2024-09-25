package com.eigsacompras.modelo;

public class Producto {
    private int idProducto;
    private String descripcion;
    private double precio;

    public Producto(){}

    public Producto(int idProducto, double precio, String descripcion) {
        this.idProducto = idProducto;
        this.precio = precio;
        this.descripcion = descripcion;
    }
    public Producto(double precio, String descripcion) {
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                '}';
    }
}
