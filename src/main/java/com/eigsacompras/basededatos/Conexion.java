package com.eigsacompras.basededatos;

import com.eigsacompras.utilidades.ConfigDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = ConfigDB.getPropiedad("url");
    private static final String USER = ConfigDB.getPropiedad("user");
    private static final String PASSWORD = ConfigDB.getPropiedad("password");

    public static Connection getConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
        return conexion;
    }
}
