package com.eigsacompras.dao;

import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;

import java.time.LocalDate;
import java.util.List;

public interface ICompraDAO {
    int agregarCompra(Compra compra);
    List<Compra> listarCompras();
    int listarComprasPendientes();
    int listarComprasDelMes();
    int listarComprasTotales();
    Compra listarProximoEntregar();
    boolean actualizarCompra(Compra compra);
    boolean eliminarCompra(int idCompra);
    Compra buscarCompraPorId(int idCompra);
    List<Compra> buscarCompras(String termino);
    List<Compra> filtrarCompras(String producto, String ordenTrabajo, String estatus, String proveedor, LocalDate desde, LocalDate hasta, boolean todo);
}
