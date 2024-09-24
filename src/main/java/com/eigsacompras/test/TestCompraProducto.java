package com.eigsacompras.test;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.CompraProductoDAO;
import com.eigsacompras.modelo.CompraProducto;

import java.sql.Connection;

public class TestCompraProducto {
    public static void main(String[] args) {
        try {
            Connection conexion = Conexion.getConexion();
            CompraProductoDAO compraProductoDAO = new CompraProductoDAO(conexion);

            CompraProducto compraProducto = new CompraProducto();
            compraProducto.setIdCompra(1);
            compraProducto.setIdProducto(1);
            compraProducto.setPartida(2);
            compraProducto.setCantidad("10 PZAS");
            compraProducto.setPrecioUnitario(100);
            compraProducto.setTotal(100*10);

            compraProducto.setIdCompraProducto(1);

            if(compraProductoDAO.agregarCompraProducto(compraProducto))
                System.out.println("agregado correctamente");
            else
                System.out.println("No agregado");

            for(CompraProducto cp: compraProductoDAO.listarCompraProducto()){
                System.out.println(cp);
            }


        } catch (Exception e) {
            System.out.println("error al iniciar");
        }

    }
}
