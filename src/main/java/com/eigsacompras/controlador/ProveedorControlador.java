package com.eigsacompras.controlador;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.ProveedorDAO;
import com.eigsacompras.modelo.Proveedor;

import java.sql.Connection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProveedorControlador {
    private final ProveedorDAO proveedorDAO;
    private Connection conexion;

    public ProveedorControlador(){
        conexion = Conexion.getConexion();
        this.proveedorDAO= new ProveedorDAO(conexion);
    }

    public void agregarProveedor(String nombre, String correo, String telefono, String ubicacion){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,telefono,nombre,correo);
            proveedorDAO.agregarProveedor(proveedor);
        }
    }//agregar

    public List<Proveedor> listarProveedor(){
        return proveedorDAO.listarProveedor();
    }//listar

    public void actualizarProveedor(String nombre, String correo, String telefono, String ubicacion, int idProveedor){
        if(validarProveedor(nombre, correo, telefono,ubicacion)){
            Proveedor proveedor = new Proveedor (ubicacion,telefono,nombre,correo,idProveedor);
            proveedorDAO.actualizarProveedor(proveedor);
        }
    }//actualizar

    public void eliminarProveedor(int idProveedor){
        proveedorDAO.eliminarProveedor(idProveedor);
    }


    public boolean validarProveedor(String nombre, String correo, String telefono, String ubicacion){
        if(nombre.isEmpty()){
            System.out.println("Nombre no puede estar vacio");
            return false;
        }
        if(!validarCorreo(correo) || correo.isEmpty()){
            System.out.println("Verifica el correo electronico");
            return false;
        }
        if(telefono.isEmpty()){
            System.out.println("El telefono no puede estar vacio");
            return false;
        }
        if(ubicacion.isEmpty()){
            System.out.println("La ubicación no puede estar vacia");
            return false;
        }
        return true;
    }//validar proveedor


    public boolean validarCorreo(String correo){
        String emailExpresion = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailExpresion);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();

    }//validar correo


}