package com.eigsacompras.test;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.ProductoProveedorDAO;
import com.eigsacompras.enums.TipoDisponibilidad;
import com.eigsacompras.modelo.ProductoProveedor;

import java.sql.Connection;

public class TestProductoProveedor{
    public static void main(String[] args) {
        try {
            Connection conexion = Conexion.getConexion();
            ProductoProveedorDAO productoProveedorDAO = new ProductoProveedorDAO(conexion);

            ProductoProveedor propre = new ProductoProveedor();
            propre.setPrecioOfrecido(120.90);
            propre.setDisponibilidad(TipoDisponibilidad.DISPONIBLE);
            propre.setIdProducto(1);
            propre.setIdProveedor(1);
            if(productoProveedorDAO.eliminarProductoProveedor(2))
                System.out.println("actualizadp");
            else
                System.out.println("No agregado");

            System.out.println("-----LISTAR-----");
            for(ProductoProveedor pp:productoProveedorDAO.listarProductoProveedor()){
                System.out.println(pp);
            }

        } catch (Exception e) {
            System.out.println("error al intentar accionar");
        }
    }
}
