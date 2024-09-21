package com.eigsacompras.modelo;

import java.time.LocalDate;

public class Notificacion {
    private int idNotificacion;
    private LocalDate fecha;
    private String mensaje;

    public Notificacion(){}

    public Notificacion(int idNotificacion, String mensaje, LocalDate fecha) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", fecha=" + fecha +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
