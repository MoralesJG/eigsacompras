package com.eigsacompras.dao;

import com.eigsacompras.modelo.Notificacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO implements INotificacionDAO{
    private PreparedStatement ps;
    private final Connection conexion;

    public NotificacionDAO(Connection conexion){
        this.conexion=conexion;
    }
    @Override
    public boolean agregarNotificacion(Notificacion notificacion) {
        String sql = "INSERT INTO notificacion(fecha,mensaje,id_compra) " +
                "VALUES(?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setDate(1,java.sql.Date.valueOf(notificacion.getFecha()));
            ps.setString(2,notificacion.getMensaje());
            ps.setInt(3,notificacion.getIdCompra());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar "+e.getMessage());
            return false;
        }
    }//agregar

    @Override
    public List<Notificacion> listarNotificacion() {
        List<Notificacion> listarnotificacion = new ArrayList<>();
        String sql = "SELECT * FROM notificacion";
        try {
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
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
        }
        return listarnotificacion;
    }

    @Override
    public boolean actualizarNotificacion(Notificacion notificacion) {
        String sql = "UPDATE notificacion SET fecha=?,mensaje=?,id_compra=? WHERE id_notificacion=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setDate(1,java.sql.Date.valueOf(notificacion.getFecha()));
            ps.setString(2,notificacion.getMensaje());
            ps.setInt(3,notificacion.getIdCompra());
            ps.setInt(4,notificacion.getIdNotificacion());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Erro al actualizar"+ e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarNotificacion(int idNotificacion) {
        String sql = "DELETE FROM notificacion WHERE id_notificacion=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idNotificacion);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar"+e.getMessage());
            return false;
        }
    }

    @Override
    public boolean buscarPorIdNotificacion(int idNotificacion) {
        return false;
    }
}
