package com.eigsacompras.dao;

import com.eigsacompras.modelo.CompraProducto;
import java.util.List;

public interface ICompraProductoDAO {
    boolean agregarCompraProducto(CompraProducto compraProducto);
    List<CompraProducto> listarCompraProducto();
    boolean actualizarCompraProducto(CompraProducto compraProducto);
    boolean eliminarCompraProducto(int idCompraProducto);
    boolean buscarCompraProductoPorId(int idCompraProducto);
}
