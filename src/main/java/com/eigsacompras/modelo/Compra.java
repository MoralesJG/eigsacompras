package com.eigsacompras.modelo;

import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;

import java.time.LocalDate;

public class Compra {
   private int idCompra;
   private String ordenCompra;
   private String condiciones;
   private LocalDate fechaEmision;
   private String ordenTrabajo;
   private LocalDate fechaEntrega;
   private String agenteProveedor;
   private String nombreComprador;
   private String revisadoPor;
   private String aprobadoPor;
   private TipoEstatus estatus;
   private String notasGenerales;
   private TipoCompra tipo;
   private LocalDate fechaInicioRenta;
   private LocalDate fechaFinRenta;
   private int idProveedor;
   private int idUsuario;

    public Compra() {
    }

    public Compra(int idCompra, String condiciones, String ordenCompra, String ordenTrabajo, LocalDate fechaEmision, LocalDate fechaEntrega, String nombreComprador, String agenteProveedor, String revisadoPor, String notasGenerales, String aprobadoPor, TipoEstatus estatus, LocalDate fechaInicioRenta, int idProveedor, LocalDate fechaFinRenta, TipoCompra tipo, int idUsuario) {
        this.idCompra = idCompra;
        this.condiciones = condiciones;
        this.ordenCompra = ordenCompra;
        this.ordenTrabajo = ordenTrabajo;
        this.fechaEmision = fechaEmision;
        this.fechaEntrega = fechaEntrega;
        this.nombreComprador = nombreComprador;
        this.agenteProveedor = agenteProveedor;
        this.revisadoPor = revisadoPor;
        this.notasGenerales = notasGenerales;
        this.aprobadoPor = aprobadoPor;
        this.estatus = estatus;
        this.fechaInicioRenta = fechaInicioRenta;
        this.idProveedor = idProveedor;
        this.fechaFinRenta = fechaFinRenta;
        this.tipo = tipo;
        this.idUsuario = idUsuario;
    }
    public Compra(String condiciones, String ordenCompra, String ordenTrabajo, LocalDate fechaEmision, LocalDate fechaEntrega, String nombreComprador, String agenteProveedor, String revisadoPor, String notasGenerales, String aprobadoPor, TipoEstatus estatus, LocalDate fechaInicioRenta, int idProveedor, LocalDate fechaFinRenta, TipoCompra tipo, int idUsuario) {
        this.condiciones = condiciones;
        this.ordenCompra = ordenCompra;
        this.ordenTrabajo = ordenTrabajo;
        this.fechaEmision = fechaEmision;
        this.fechaEntrega = fechaEntrega;
        this.nombreComprador = nombreComprador;
        this.agenteProveedor = agenteProveedor;
        this.revisadoPor = revisadoPor;
        this.notasGenerales = notasGenerales;
        this.aprobadoPor = aprobadoPor;
        this.estatus = estatus;
        this.fechaInicioRenta = fechaInicioRenta;
        this.idProveedor = idProveedor;
        this.fechaFinRenta = fechaFinRenta;
        this.tipo = tipo;
        this.idUsuario = idUsuario;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public LocalDate getFechaInicioRenta() {
        return fechaInicioRenta;
    }

    public void setFechaInicioRenta(LocalDate fechaInicioRenta) {
        this.fechaInicioRenta = fechaInicioRenta;
    }

    public LocalDate getFechaFinRenta() {
        return fechaFinRenta;
    }

    public void setFechaFinRenta(LocalDate fechaFinRenta) {
        this.fechaFinRenta = fechaFinRenta;
    }

    public TipoCompra getTipo() {
        return tipo;
    }

    public void setTipo(TipoCompra tipo) {
        this.tipo = tipo;
    }

    public String getNotasGenerales() {
        return notasGenerales;
    }

    public void setNotasGenerales(String notasGenerales) {
        this.notasGenerales = notasGenerales;
    }

    public String getRevisadoPor() {
        return revisadoPor;
    }

    public void setRevisadoPor(String revisadoPor) {
        this.revisadoPor = revisadoPor;
    }

    public String getAgenteProveedor() {
        return agenteProveedor;
    }

    public void setAgenteProveedor(String agenteProveedor) {
        this.agenteProveedor = agenteProveedor;
    }

    public TipoEstatus getEstatus() {
        return estatus;
    }

    public void setEstatus(TipoEstatus estatus) {
        this.estatus = estatus;
    }

    public String getAprobadoPor() {
        return aprobadoPor;
    }

    public void setAprobadoPor(String aprobadoPor) {
        this.aprobadoPor = aprobadoPor;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(String ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", ordenCompra='" + ordenCompra + '\'' +
                ", condiciones='" + condiciones + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", ordenTrabajo='" + ordenTrabajo + '\'' +
                ", fechaEntrega=" + fechaEntrega +
                ", agenteProveedor='" + agenteProveedor + '\'' +
                ", nombreComprador='" + nombreComprador + '\'' +
                ", revisadoPor='" + revisadoPor + '\'' +
                ", aprobadoPor='" + aprobadoPor + '\'' +
                ", estatus=" + estatus +
                ", notasGenerales='" + notasGenerales + '\'' +
                ", tipo=" + tipo +
                ", fechaInicioRenta=" + fechaInicioRenta +
                ", fechaFinRenta=" + fechaFinRenta +
                ", idProveedor=" + idProveedor +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
