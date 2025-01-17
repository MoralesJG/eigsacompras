package com.eigsacompras.modelo;

import java.util.List;

public class Proveedor {
    private int idProveedor;
    private String nombre;
    private String correo;
    private String telefono;
    private String ubicacion;
    private List<ProductoProveedor> productos;//solo a nivel de aplicacion

    public Proveedor(){}

    public Proveedor(int idProveedor){
        this.idProveedor=idProveedor;
    }

    public Proveedor(String ubicacion, String correo, String telefono, String nombre, int idProveedor) {
        this.ubicacion = ubicacion;
        this.correo = correo;
        this.telefono = telefono;
        this.nombre = nombre;
        this.idProveedor = idProveedor;
    }

    public Proveedor(String ubicacion, String telefono, String nombre, String correo) {
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.nombre = nombre;
        this.correo = correo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ProductoProveedor> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoProveedor> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}
