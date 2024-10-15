package com.eigsacompras.basededatos;

import com.eigsacompras.utilidades.ConfigLoader;

import javax.swing.*;
import java.sql.*;

public class Conexion {
    private static final String URL = ConfigLoader.getPropiedad("url");
    private static final String USER = ConfigLoader.getPropiedad("user");
    private static final String PASSWORD = ConfigLoader.getPropiedad("password");

    public static Connection getConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
        return conexion;
    }//cierre Connection

    public static void cerrar(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Falló al cerrar la Conexión");
            }
        }//cierre if

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Falló al cerrar la Conexión");
            }
        }//cierre if

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Falló al cerrar la Conexión");
            }
        }//cierre if
    }//cerrar
}
