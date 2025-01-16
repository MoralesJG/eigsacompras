package com.eigsacompras.modelo;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int idProducto;
    private String descripcion;
    private List<ProductoProveedor> proveedores; //a nivel de aplicacion

    public Producto(){
        proveedores = new ArrayList<>();
    }

    public Producto(int idProducto, String descripcion) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
    }
    public Producto(String descripcion) {
        this.descripcion = descripcion;
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

    public List<ProductoProveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<ProductoProveedor> proveedores) {
        this.proveedores = proveedores;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", descripcion='" + descripcion + '\'' +
                ", proveedores=" + proveedores +
                '}';
    }
}
