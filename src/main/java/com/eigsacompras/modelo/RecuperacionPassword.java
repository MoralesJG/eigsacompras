package com.eigsacompras.modelo;

import java.sql.Timestamp;

public class RecuperacionPassword {
    private int idRecuperacionPassword;
    private int idUsuario;
    private String codigoRecuperacion;
    private Timestamp fechaExpiracion;

    public RecuperacionPassword(){}

    public RecuperacionPassword(int idUsuario, String codigoRecuperacion, Timestamp fechaExpiracion) {
        this.idUsuario = idUsuario;
        this.codigoRecuperacion = codigoRecuperacion;
        this.fechaExpiracion = fechaExpiracion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodigoRecuperacion() {
        return codigoRecuperacion;
    }

    public void setCodigoRecuperacion(String codigoRecuperacion) {
        this.codigoRecuperacion = codigoRecuperacion;
    }

    public Timestamp getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Timestamp fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public int getIdRecuperacionPassword() {
        return idRecuperacionPassword;
    }

    public void setIdRecuperacionPassword(int idRecuperacionPassword) {
        this.idRecuperacionPassword = idRecuperacionPassword;
    }

    @Override
    public String toString() {
        return "RecuperacionPassword{" +
                "idRecuperacionPassword=" + idRecuperacionPassword +
                ", idUsuario=" + idUsuario +
                ", codigoRecuperacion='" + codigoRecuperacion + '\'' +
                ", fechaExpiracion=" + fechaExpiracion +
                '}';
    }
}
