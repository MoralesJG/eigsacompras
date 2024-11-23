package com.eigsacompras.controlador;

import com.eigsacompras.dao.ProductoProveedorDAO;
import com.eigsacompras.dao.ProveedorDAO;
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
        this.productoProveedorDAO = new ProductoProveedorDAO();
    }

    public boolean agregarProveedor(String nombre, String correo, String telefono, String ubicacion, List<ProductoProveedor> productosProveedor){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            if(validarCorreo(correo)) {
                Proveedor proveedor = new Proveedor(ubicacion, telefono, nombre, correo);
                if (!productosProveedor.isEmpty()) {
                    try {
                        int idProveedor = proveedorDAO.agregarProveedor(proveedor);
                        for (ProductoProveedor producto : productosProveedor) {
                            producto.setIdProveedor(idProveedor);
                            productoProveedorDAO.agregarProductoProveedor(producto);
                        }
                        JOptionPane.showMessageDialog(null, "Proveedor agregado correctamente.", "Agregado", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error al agregar al proveedor. Por favor, inténtelo nuevamente. Error: " + e, "No agregado", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }//try
                } else {
                    JOptionPane.showMessageDialog(null, "Agregue al menos 1 producto.", "Sin productos", JOptionPane.WARNING_MESSAGE);
                    return false;
                }//if productos proveedor
            }else{
                JOptionPane.showMessageDialog(null, "Correo inválido. Verificar", "Verificar correo", JOptionPane.WARNING_MESSAGE);
                return false;
            }//if validar correo
        }else{
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
            return false;
        }//validar
    }//agregar

    public List<Proveedor> listarProveedor(){
        return proveedorDAO.listarProveedor();
    }//listar

    public Proveedor listarProveedorPorId(int idProveedor){
        return proveedorDAO.buscarProveedorPorId(idProveedor);
    }//listar por id

    public boolean actualizarProveedor(String nombre, String correo, String telefono, String ubicacion, int idProveedor, List<ProductoProveedor> productosProveedor){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,correo,telefono,nombre,idProveedor);
            if (!productosProveedor.isEmpty()) {
                if (proveedorDAO.actualizarProveedor(proveedor)) {
                    productoProveedorDAO.eliminarProductoProveedor(idProveedor);//se eliminan los productos con el proveedor
                    for (ProductoProveedor producto : productosProveedor) {//se agregan los productos nuevamente incluidos los nuevos a ese proveedor
                        productoProveedorDAO.agregarProductoProveedor(producto);
                    }
                    //se eliminan todos los productos asociados al proveedor ya que si se modifica el proveedor y se quieren agregar otros productos si solo se actualiza no se podrá agregar lo nuevos productos al proveedor...
                    //...por lo que para asegurar que se actualice correctamente incluyendo los nuevos productos que se quieren agregar es necesario eliminar todo y agregar todo lo nuevo
                    JOptionPane.showMessageDialog(null, "Todo actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el proveedor. Por favor, inténtelo nuevamente.", "No actualizado", JOptionPane.ERROR_MESSAGE);
                    return false;
                }//if dao
            }else{
                JOptionPane.showMessageDialog(null, "Agregue al menos 1 producto.", "Sin productos", JOptionPane.WARNING_MESSAGE);
                return false;
            }//if valida productoProveedor vacio
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
            return false;

        }//if validar
    }//actualizar

    public void eliminarProveedor(int idProveedor){
        int opc = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este proveedor?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            if (productoProveedorDAO.eliminarProductoProveedor(idProveedor)) {
                proveedorDAO.eliminarProveedor(idProveedor);
                JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el proveedor. Por favor, inténtelo nuevamente.", "No eliminado", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//eliminar

    public List<Proveedor> buscarProveedores(String termino){
        return proveedorDAO.buscarProveedores(termino);
    }

    public boolean validarProveedor(String nombre, String correo, String telefono, String ubicacion){
        if(nombre.isEmpty()) return false;
        if(correo.isEmpty()) return false;
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