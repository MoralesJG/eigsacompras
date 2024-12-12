package com.eigsacompras.controlador;

import com.eigsacompras.dao.ProductoDAO;
import com.eigsacompras.dao.ProductoProveedorDAO;
import com.eigsacompras.modelo.Producto;
import com.eigsacompras.modelo.ProductoProveedor;
import javax.swing.*;
import java.util.List;

public class ProductoControlador {
    private ProductoDAO productoDAO;
    private ProductoProveedorDAO productoProveedorDAO;

    public ProductoControlador() {
        this.productoDAO = new ProductoDAO();
        this.productoProveedorDAO = new ProductoProveedorDAO();
    }

    public boolean agregarProducto(String descripcion, List<ProductoProveedor> productosProveedores) {
        if (!descripcion.isEmpty()) {
            Producto producto = new Producto(descripcion);
            try {
                int idProducto = productoDAO.agregarProducto(producto);
                if (idProducto != -1) {
                    if (!productosProveedores.isEmpty()) {
                        for (ProductoProveedor productoProveedor : productosProveedores) {
                            productoProveedor.setIdProducto(idProducto);
                            productoProveedorDAO.agregarProductoProveedor(productoProveedor);
                            JOptionPane.showMessageDialog(null, "Producto con proveedor agregado correctamente.", "Agregado", JOptionPane.INFORMATION_MESSAGE);
                            return true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto agregado correctamente.", "Agregado", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }//if productoProveedor vacio
                } else {
                    JOptionPane.showMessageDialog(null, "Producto no agregado.\n Si el problema persiste, contacte al desarrollador ", "No agregado", JOptionPane.ERROR_MESSAGE);
                    return false;
                }//if -1
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al agregar el producto. Por favor, inténtelo nuevamente. Error: " + e, "No agregado", JOptionPane.ERROR_MESSAGE);
                return false;
            }//try

        } else {
            JOptionPane.showMessageDialog(null, "Se necesita la descripción del producto", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return false;
    }//agregar

    public List<Producto> listarProducto() {
        return productoDAO.listarProducto();
    }//listar

    public boolean actualizarProducto(String descripcion, int idProducto, List<ProductoProveedor> productoProveedores) {
        if (!descripcion.isEmpty()) {
            Producto producto = new Producto(idProducto, descripcion);
            if (productoDAO.actualizarProducto(producto)) {
                productoProveedorDAO.eliminarProductoProveedorPorIdProducto(idProducto);//se eliminan los productos con el proveedor
                if (!productoProveedores.isEmpty()) {//si hay proveedores en el producto se agregarán.
                    for (ProductoProveedor productoProveedor : productoProveedores) {//se agregan los productos nuevamente incluidos los nuevos a ese proveedor
                        productoProveedorDAO.agregarProductoProveedor(productoProveedor);
                    }
                    //se eliminan todos los productos asociados al proveedor ya que si se modifica el proveedor y se quieren agregar otros productos si solo se actualiza no se podrá agregar lo nuevos productos al proveedor...
                    //...por lo que para asegurar que se actualice correctamente incluyendo los nuevos productos que se quieren agregar es necesario eliminar todo y agregar todo lo nuevo
                    JOptionPane.showMessageDialog(null, "Todo actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el producto. Por favor, inténtelo nuevamente.", "No actualizado", JOptionPane.ERROR_MESSAGE);
                return false;
            }//if dao
        } else {
            JOptionPane.showMessageDialog(null, "Se necesita la descripción del producto", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }//actualizar

    public void eliminarProducto(int idProducto) {
        int opc = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este producto?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            if (productoProveedorDAO.eliminarProductoProveedorPorIdProducto(idProducto)) {
                productoDAO.eliminarProducto(idProducto);
                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el producto. Por favor, inténtelo nuevamente.", "No eliminado", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//eliminar

    public Producto listarProductoPorId(int idProducto) {
        return productoDAO.buscarProductoPorId(idProducto);
    }//listar por id

    public List<Producto> buscarProductos(String termino) {
        return productoDAO.buscarProductos(termino);
    }//buscar productos

}
