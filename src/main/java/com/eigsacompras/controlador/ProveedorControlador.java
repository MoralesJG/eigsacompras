package com.eigsacompras.controlador;

import com.eigsacompras.dao.ProductoProveedorDAO;
import com.eigsacompras.dao.ProveedorDAO;
import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProveedorControlador {
    private ProveedorDAO proveedorDAO;
    private ProductoProveedorDAO productoProveedorDAO;
    private AuditoriaControlador auditoriaControlador;

    public ProveedorControlador(){
        this.proveedorDAO= new ProveedorDAO();
        this.productoProveedorDAO = new ProductoProveedorDAO();
        this.auditoriaControlador = new AuditoriaControlador();
    }

    public boolean agregarProveedor(String nombre, String correo, String telefono, String ubicacion, List<ProductoProveedor> productosProveedor,int idUsuario){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            if(validarCorreo(correo)) {
                Proveedor proveedor = new Proveedor(ubicacion, telefono, nombre, correo);
                    try {
                        int idProveedor = proveedorDAO.agregarProveedor(proveedor);
                        if (idProveedor!=-1){
                            if(!productosProveedor.isEmpty()) {//si no hay productos continua normalmente
                                for (ProductoProveedor producto : productosProveedor) {
                                    producto.setIdProveedor(idProveedor);
                                    productoProveedorDAO.agregarProductoProveedor(producto);
                                }
                            }//if
                            mandarAuditoria(idProveedor, TipoAccion.INSERTAR,"Se insertó un proveedor con id = "+idProveedor,idUsuario);
                            JOptionPane.showMessageDialog(null, "Proveedor agregado correctamente.", "Agregado", JOptionPane.INFORMATION_MESSAGE);
                            return true;
                        }else {
                            JOptionPane.showMessageDialog(null, "Producto no agregado.\n Si el problema persiste, contacte al desarrollador ", "No agregado", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error al agregar al proveedor. Por favor, inténtelo nuevamente. Error: " + e, "No agregado", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }//try
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

    public boolean actualizarProveedor(String nombre, String correo, String telefono, String ubicacion, int idProveedor, List<ProductoProveedor> productosProveedor, int idUsuario){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,correo,telefono,nombre,idProveedor);
                if (proveedorDAO.actualizarProveedor(proveedor)) {
                    productoProveedorDAO.eliminarProductoProveedorPorIdProveedor(idProveedor);//se eliminan los productos con el proveedor
                    if(!productosProveedor.isEmpty()) {//si no hay productos continua normalmente
                        for (ProductoProveedor producto : productosProveedor) {//se agregan los productos nuevamente incluidos los nuevos a ese proveedor
                            productoProveedorDAO.agregarProductoProveedor(producto);
                        }
                        //se eliminan todos los productos asociados al proveedor ya que si se modifica el proveedor y se quieren agregar otros productos si solo se actualiza no se podrá agregar lo nuevos productos al proveedor...
                        //...por lo que para asegurar que se actualice correctamente incluyendo los nuevos productos que se quieren agregar es necesario eliminar todo y agregar todo lo nuevo
                    }//if
                    mandarAuditoria(idProveedor, TipoAccion.ACTUALIZAR,"Se actualizó el proveedor con id = "+idProveedor,idUsuario);
                    JOptionPane.showMessageDialog(null, "Todo actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el proveedor. Por favor, inténtelo nuevamente.", "No actualizado", JOptionPane.ERROR_MESSAGE);
                    return false;
                }//if dao
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
            return false;

        }//if validar
    }//actualizar

    public void eliminarProveedor(int idProveedor,int idUsuario){
        int opc = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este proveedor?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            if (productoProveedorDAO.eliminarProductoProveedorPorIdProveedor(idProveedor)) {
                proveedorDAO.eliminarProveedor(idProveedor);
                mandarAuditoria(idProveedor, TipoAccion.ELIMINAR,"Se eliminó el proveedor con id = "+idProveedor,idUsuario);
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

    public void mandarAuditoria(int idRegistro, TipoAccion accion,String descripcion,int idUsuario){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime fecha = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        auditoriaControlador.agregarAuditoria("PROVEEDORES",idRegistro,accion,fecha,descripcion,idUsuario);
    }//auditoria


}