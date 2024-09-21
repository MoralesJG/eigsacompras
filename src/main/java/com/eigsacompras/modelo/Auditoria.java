package com.eigsacompras.modelo;

import com.eigsacompras.enums.TipoAccion;

import java.time.LocalDate;

public class Auditoria {
    private int idAuditoria;
    private String tablaAfectada;
    private int idRegistroAfectado;
    private TipoAccion accion;
    private LocalDate fechaCambio;
    private String descripcion;
    private int idUsuario;

    public Auditoria(){}

    public Auditoria(int idAuditoria, int idUsuario, String descripcion, LocalDate fechaCambio, TipoAccion accion, int idRegistroAfectado, String tablaAfectada) {
        this.idAuditoria = idAuditoria;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.fechaCambio = fechaCambio;
        this.accion = accion;
        this.idRegistroAfectado = idRegistroAfectado;
        this.tablaAfectada = tablaAfectada;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public int getIdRegistroAfectado() {
        return idRegistroAfectado;
    }

    public void setIdRegistroAfectado(int idRegistroAfectado) {
        this.idRegistroAfectado = idRegistroAfectado;
    }

    public TipoAccion getAccion() {
        return accion;
    }

    public void setAccion(TipoAccion accion) {
        this.accion = accion;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }
    @Override
    public String toString() {
        return "Auditoria{" +
                "idAuditoria=" + idAuditoria +
                ", tablaAfectada='" + tablaAfectada + '\'' +
                ", idRegistroAfectado=" + idRegistroAfectado +
                ", accion=" + accion +
                ", fechaCambio=" + fechaCambio +
                ", descripcion='" + descripcion + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
