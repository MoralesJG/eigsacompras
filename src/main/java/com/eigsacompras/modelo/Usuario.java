package com.eigsacompras.modelo;

import com.eigsacompras.enums.TipoAcceso;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private TipoAcceso tipo;
    private String contrasena;

    public Usuario(){}

    public Usuario(int idUsuario){
        this.idUsuario=idUsuario;
    }

    public Usuario(int idUsuario, String contrasena, String correo, String nombre, TipoAcceso tipo) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public Usuario( String contrasena, String correo, String nombre, TipoAcceso tipo) {
        this.contrasena = contrasena;
        this.correo = correo;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public TipoAcceso getTipo() {
        return tipo;
    }

    public void setTipo(TipoAcceso tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", tipo=" + tipo +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
