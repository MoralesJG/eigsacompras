package com.eigsacompras.test;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.ProductoDAO;
import com.eigsacompras.modelo.Producto;

import java.sql.Connection;
import java.sql.SQLException;

public class TestProductoDAO {
    public static void main(String[] args) {
        try (Connection conexion = Conexion.getConexion()){
            ProductoDAO productoDAO = new ProductoDAO(conexion);

            Producto producto = new Producto();
            producto.setDescripcion("Llave alen tipo chimichurri");
            producto.setPrecio(12300);

            if(productoDAO.agregarProducto(producto))
                System.out.println("Agregado correctamente");
            else
                System.out.println("Error al agregar");

            System.out.println("----------Listar----------");
            for(Producto c: productoDAO.listarProducto()){
                System.out.println(c);
            }
        }catch (SQLException e){
            System.out.println("Error al conectar al db");
        }
    }

}
