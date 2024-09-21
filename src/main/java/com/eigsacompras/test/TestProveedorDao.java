package com.eigsacompras.test;

//PROVEEDOR FUNCIONA CORRECTAMENTE

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.ProveedorDAO;
import com.eigsacompras.modelo.Proveedor;
import java.sql.Connection;
import java.sql.SQLException;

public class TestProveedorDao {
    public static void main(String[] args) {
        try(Connection conexion = Conexion.getConexion()){
            ProveedorDAO proveedorDAO = new ProveedorDAO(conexion);

            Proveedor proveedor = new Proveedor();
            proveedor.setNombre("TISA SA DE CV");
            proveedor.setCorreo("celsosaid4@gmail.com");
            proveedor.setTelefono("9373779071");
            proveedor.setUbicacion("Carretera al amcayo 2da Seccion Reforma Chis.");

            if(proveedorDAO.agregarProveedor(proveedor))
                System.out.println("Proveedor agregado");
            else
                System.out.println("Proveedor no agregado");

            System.out.println("----------Listar----------");
            for(Proveedor c : proveedorDAO.listarProveedor()){
                System.out.println(c);
            }//for
        }catch (SQLException e){
            System.out.println("Error al conectar "+e.getMessage());
        }//catch
    }
}
