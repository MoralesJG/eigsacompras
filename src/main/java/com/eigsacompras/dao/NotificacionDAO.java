package com.eigsacompras.dao;

import com.eigsacompras.modelo.Notificacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class NotificacionDAO implements INotificacionDAO{
    private PreparedStatement ps;
    private Connection conexion;

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
            ps.setInt(3,notificacion.ge);
        } catch (SQLException e) {
            System.out.println("Error al insertar "+e.getMessage());
        }
    }

    @Override
    public List<Notificacion> listarNotificacion() {
        return List.of();
    }

    @Override
    public boolean actualizarNotificacion(Notificacion notificacion) {
        return false;
    }

    @Override
    public boolean elimincarNotificacion(int idNotificacion) {
        return false;
    }

    @Override
    public boolean buscarPorIdNotificacion(int idNotificacion) {
        return false;
    }
}
