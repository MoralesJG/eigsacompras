package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.modelo.Notificacion;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO implements INotificacionDAO{
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public NotificacionDAO(){
    }
    @Override
    public boolean agregarNotificacion(Notificacion notificacion) {
        try {
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO notificacion(fecha,mensaje,id_compra) " +
                    "VALUES(?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setDate(1,java.sql.Date.valueOf(notificacion.getFecha()));
            ps.setString(2,notificacion.getMensaje());
            ps.setInt(3,notificacion.getIdCompra());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar una notificación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//agregar

    @Override
    public List<Notificacion> listarNotificacion() {
        List<Notificacion> listarnotificacion = new ArrayList<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT * FROM notificacion ORDER BY fecha DESC";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Notificacion notificacion = new Notificacion();
                notificacion.setIdNotificacion(rs.getInt("id_notificacion"));
                notificacion.setMensaje(rs.getString("mensaje"));
                notificacion.setFecha(rs.getDate("fecha").toLocalDate());
                notificacion.setIdCompra(rs.getInt("id_compra"));
                listarnotificacion.add(notificacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar"+ e.getMessage());
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
        return listarnotificacion;
    }//listar

    @Override
    public boolean actualizarNotificacion(Notificacion notificacion) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE notificacion SET fecha=?,mensaje=? WHERE id_compra=?";
            ps = conexion.prepareStatement(sql);
            ps.setDate(1,java.sql.Date.valueOf(notificacion.getFecha()));
            ps.setString(2,notificacion.getMensaje());
            ps.setInt(3,notificacion.getIdCompra());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar una notificación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//actualizar

    @Override
    public boolean eliminarNotificacion(int idCompra) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM notificacion WHERE id_compra=?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idCompra);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar una notificación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar

    @Override
    public List<Notificacion> buscarNotificaciones(String filtro) {
        List<Notificacion> listaNotificacion = new ArrayList<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT * FROM notificacion " +
                    "WHERE fecha LIKE ? OR " +
                    "mensaje LIKE ? " +
                    "ORDER BY fecha DESC";
            ps = conexion.prepareStatement(sql);
            String valorFiltro = "%" + filtro + "%";//para buscar en todo
            for (int i = 1; i <= 2; i++) {
                ps.setString(i, valorFiltro);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                Notificacion notificacion = new Notificacion(rs.getInt("id_notificacion"),
                        rs.getString("mensaje"),rs.getDate("fecha").toLocalDate(),rs.getInt("id_compra"));
                listaNotificacion.add(notificacion);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar notificaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaNotificacion;
    }//buscar proveedores
}
