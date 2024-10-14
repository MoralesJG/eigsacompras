package com.eigsacompras.controlador;

import com.eigsacompras.dao.ProductoProveedorDAO;
import com.eigsacompras.dao.ProveedorDAO;
import com.eigsacompras.modelo.CompraProducto;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;

import javax.swing.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProveedorControlador {
    private ProveedorDAO proveedorDAO;
    private ProductoProveedorDAO productoProveedorDAO;

    public ProveedorControlador(){
        this.proveedorDAO= new ProveedorDAO();
    }

    public void agregarProveedor(String nombre, String correo, String telefono, String ubicacion, List<ProductoProveedor> productosProveedor){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,telefono,nombre,correo);
            try {
                int idProveedor = proveedorDAO.agregarProveedor(proveedor);
                for(ProductoProveedor producto: productosProveedor){
                    producto.setIdProveedor(idProveedor);
                    productoProveedorDAO.agregarProductoProveedor(producto);
                }
                JOptionPane.showMessageDialog(null, "Proveedor agregado correctamente.", "Agregado", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error al agregar al proveedor. Por favor, inténtelo nuevamente. Error: "+e, "No agregado", JOptionPane.ERROR_MESSAGE);
            }//try

        }else{
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }//validar
    }//agregar

    public List<Proveedor> listarProveedor(){
        return proveedorDAO.listarProveedor();
    }//listar

    public void actualizarProveedor(String nombre, String correo, String telefono, String ubicacion, int idProveedor, List<ProductoProveedor> productosProveedor){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,telefono,nombre,correo,idProveedor);
            if(proveedorDAO.actualizarProveedor(proveedor)){
                for(ProductoProveedor producto: productosProveedor){
                    productoProveedorDAO.actualizarProductoProveedor(producto);
                }
                JOptionPane.showMessageDialog(null, "Todo actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Error al actualizar el proveedor. Por favor, inténtelo nuevamente.", "No actualizado", JOptionPane.ERROR_MESSAGE);
            }//if dao
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }//if validar
    }//actualizar

    public void eliminarProveedor(int idProveedor){
        if(proveedorDAO.eliminarProveedor(idProveedor)){
            productoProveedorDAO.eliminarProductoProveedor(idProveedor);
            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null, "Error al eliminar el proveedor. Por favor, inténtelo nuevamente.", "No eliminado", JOptionPane.ERROR_MESSAGE);
        }
    }//eliminar

    public boolean validarProveedor(String nombre, String correo, String telefono, String ubicacion){
        if(nombre.isEmpty()) return false;
        if(!validarCorreo(correo) || correo.isEmpty()) return false;
        if(telefono.isEmpty()) return false;
        if(ubicacion.isEmpty()) return false;

        return true;
    }//validar proveedor

    public boolean validarCorreo(String correo){
        String emailExpresion = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailExpresion);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();//retorna true si cumple con lo requerido
    }//validar correo


}