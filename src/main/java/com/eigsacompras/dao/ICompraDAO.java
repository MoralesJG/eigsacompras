package com.eigsacompras.dao;

import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;

import java.util.List;

public interface ICompraDAO {
    int agregarCompra(Compra compra, List<CompraProducto> compraProducto);
    List<Compra> listarCompras();
    boolean actualizarCompra(Compra compra, List<CompraProducto> compraProducto);
    boolean eliminarCompra(int idCompra);
    Compra buscarCompraPorId(int idCompra);
}
