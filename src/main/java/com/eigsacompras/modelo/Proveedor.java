package com.eigsacompras.modelo;

public class Proveedor {
    private int idProveedor;
    private String nombre;
    private String correo;
    private String telefono;
    private String ubicacion;

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
