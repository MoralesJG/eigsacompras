package com.eigsacompras.controlador;

import com.eigsacompras.dao.ProveedorDAO;
import com.eigsacompras.modelo.Proveedor;

import javax.swing.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProveedorControlador {
    private ProveedorDAO proveedorDAO;

    public ProveedorControlador(){
        this.proveedorDAO= new ProveedorDAO();
    }

    public void agregarProveedor(String nombre, String correo, String telefono, String ubicacion){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,telefono,nombre,correo);
            proveedorDAO.agregarProveedor(proveedor);
        }else{
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }
    }//agregar

    public List<Proveedor> listarProveedor(){
        return proveedorDAO.listarProveedor();
    }//listar

    public void actualizarProveedor(String nombre, String correo, String telefono, String ubicacion, int idProveedor){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,telefono,nombre,correo,idProveedor);
            proveedorDAO.actualizarProveedor(proveedor);
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }
    }//actualizar

    public void eliminarProveedor(int idProveedor){
        proveedorDAO.eliminarProveedor(idProveedor);
    }

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