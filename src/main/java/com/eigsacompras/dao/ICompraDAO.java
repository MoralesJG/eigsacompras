package com.eigsacompras.dao;

import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;

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
}
