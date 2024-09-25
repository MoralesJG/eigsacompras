package com.eigsacompras.dao;

import com.eigsacompras.modelo.Compra;

import java.util.List;

public interface ICompraDAO {
    int agregarCompra(Compra compra);
    List<Compra> listarCompras();
    boolean actualizarCompra(Compra compra);
    boolean eliminarCompra(int idCompra);
    Compra buscarCompraPorId(int idCompra);
}
