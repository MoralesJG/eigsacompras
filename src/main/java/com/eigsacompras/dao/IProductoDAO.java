package com.eigsacompras.dao;

import com.eigsacompras.modelo.Producto;

import java.util.List;

public interface IProductoDAO {
    int agregarProducto(Producto producto);
    List<Producto> listarProducto();
    boolean actualizarProducto(Producto producto);
    boolean eliminarProducto(int idProducto);
    Producto buscarProductoPorId(int idProducto);
    List<Producto> buscarProductos(String termino);
}
