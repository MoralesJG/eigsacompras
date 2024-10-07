package com.eigsacompras.controlador;

import com.eigsacompras.dao.ProductoDAO;
import com.eigsacompras.modelo.Producto;
import javax.swing.*;
import java.util.List;

public class ProductoControlador {
    private ProductoDAO productoDAO;

    public ProductoControlador(){
        this.productoDAO = new ProductoDAO();
    }

    public void agregarProducto(String descripcion,double precio){
        if(validarProducto(descripcion,precio)){
            Producto producto = new Producto(precio,descripcion);
            productoDAO.agregarProducto(producto);
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }
    }//agregar

    public List<Producto> listarProducto(){
        return productoDAO.listarProducto();
    }//listar

    public void actualizarProducto(String descripcion, double precio, int idProducto){
        if(validarProducto(descripcion,precio)){
            Producto producto = new Producto(idProducto,precio,descripcion);
            productoDAO.agregarProducto(producto);
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }
    }//actualizar

    public void eliminarProducto(int idProducto){
        productoDAO.eliminarProducto(idProducto);
    }//eliminar

    public boolean validarProducto(String descripcion, double precio){
        if(descripcion.isEmpty()) return false;
        if(precio == 0) return false;

        return true;
    }//validar
}
