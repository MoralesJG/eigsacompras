package com.eigsacompras.modelo;

import com.eigsacompras.enums.TipoDisponibilidad;

public class ProductoProveedor {
    private Integer idProductoProveedor;
    private double precioOfrecido;
    private TipoDisponibilidad disponibilidad;
    private int idProducto;
    private int idProveedor;
    private Producto producto;//a nivel de aplicacion

    public ProductoProveedor(){}

    public ProductoProveedor(int idProductoProveedor, int idProveedor, int idProducto, TipoDisponibilidad disponibilidad, double precioOfrecido) {
        this.idProductoProveedor = idProductoProveedor;
        this.idProveedor = idProveedor;
        this.idProducto = idProducto;
        this.disponibilidad = disponibilidad;
        this.precioOfrecido = precioOfrecido;
    }
    public ProductoProveedor(int idProveedor, int idProducto, TipoDisponibilidad disponibilidad, double precioOfrecido) {
        this.idProveedor = idProveedor;
        this.idProducto = idProducto;
        this.disponibilidad = disponibilidad;
        this.precioOfrecido = precioOfrecido;
    }

    public int getIdProductoProveedor() {
        return idProductoProveedor;
    }

    public void setIdProductoProveedor(int idProductoProveedor) {
        this.idProductoProveedor = idProductoProveedor;
    }

    public TipoDisponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(TipoDisponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public double getPrecioOfrecido() {
        return precioOfrecido;
    }

    public void setPrecioOfrecido(double precioOfrecido) {
        this.precioOfrecido = precioOfrecido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "ProductoProveedor{" +
                "idProductoProveedor=" + idProductoProveedor +
                ", precioOfrecido=" + precioOfrecido +
                ", disponibilidad=" + disponibilidad +
                ", idProducto=" + idProducto +
                ", idProveedor=" + idProveedor +
                '}';
    }
}
