package com.eigsacompras.modelo;

public class CompraProducto {
    private int idCompraProducto;
    private int idCompra;
    private int idProducto;
    private int partida;
    private String cantidad;
    private double precioUnitario;
    private double total;

    public CompraProducto() {
    }

    public CompraProducto(double total, double precioUnitario, String cantidad, int partida, int idProducto, int idCompra, int idCompraProducto) {
        this.total = total;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.partida = partida;
        this.idProducto = idProducto;
        this.idCompra = idCompra;
        this.idCompraProducto = idCompraProducto;
    }
    public CompraProducto(double total, double precioUnitario, String cantidad, int partida, int idProducto, int idCompra) {
        this.total = total;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.partida = partida;
        this.idProducto = idProducto;
        this.idCompra = idCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCompraProducto() {
        return idCompraProducto;
    }

    public void setIdCompraProducto(int idCompraProducto) {
        this.idCompraProducto = idCompraProducto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public int getPartida() {
        return partida;
    }

    public void setPartida(int partida) {
        this.partida = partida;
    }

    @Override
    public String toString() {
        return "CompraProducto{" +
                "idCompraProducto=" + idCompraProducto +
                ", idCompra=" + idCompra +
                ", idProducto=" + idProducto +
                ", partida=" + partida +
                ", cantidad='" + cantidad + '\'' +
                ", precioUnitario=" + precioUnitario +
                ", total=" + total +
                '}';
    }
}
