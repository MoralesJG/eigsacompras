package com.eigsacompras.dao;

import com.eigsacompras.modelo.ProductoProveedor;

import java.util.List;

public interface IProductoProveedorDAO {
    boolean agregarProductoProveedor(ProductoProveedor productoProveedor);
    List<ProductoProveedor> listarProductoProveedor();
    boolean actualizarProductoProveedor(ProductoProveedor productoProveedor);
    boolean eliminarProductoProveedorPorIdProveedor(int idProveedor);
    boolean eliminarProductoProveedorPorIdProducto(int idProducto);
    boolean buscarPorIdProductoProveedor(int idProductoProveedor);

}
