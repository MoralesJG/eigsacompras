package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.modelo.RecuperacionPassword;

import javax.swing.*;
import java.sql.*;

public class RecuperacionPasswordDAO implements IRecuperacionPasswordDAO{
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public RecuperacionPasswordDAO(){}

    @Override
    public boolean agregarCodigoRecuperacion(RecuperacionPassword rp) {
        try {
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO recuperacionPassword (id_usuario,codigo_recuperacion,fecha_expiracion) " +
                    "VALUES (?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,rp.getIdUsuario());
            ps.setString(2,rp.getCodigoRecuperacion());
            ps.setTimestamp(3,rp.getFechaExpiracion());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error en la generación del Token" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion,ps,null);
        }
    }//agregar

    @Override
    public boolean validarCodigoRecuperacion(String codigo, int idUsuario) {
        try {
            conexion=Conexion.getConexion();
            String sql = "SELECT * FROM RecuperacionPassword WHERE codigo_recuperacion=? AND id_usuario=?";
            ps=conexion.prepareStatement(sql);
            ps.setString(1,codigo);
            ps.setInt(2,idUsuario);
            rs=ps.executeQuery();
            if (rs.next()){
                Timestamp fecha = rs.getTimestamp("fecha_expiracion");
                if(fecha.after(new Timestamp(System.currentTimeMillis()))){
                    return true;
                }//cierre if.after
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar validar el codigo." + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return false;
    }//validar

    @Override
    public boolean eliminarRecuperacionPassword(int idUsuario) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM recuperacionPassword WHERE id_usuario = ?";
            ps=conexion.prepareStatement(sql);
            ps.setInt(1,idUsuario);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error interno al eliminar el token" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }
        return false;
    }//eliminar

    @Override
    public int idUsuarioPorEmail(String email) {
        int idUsuario = -1;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT id_usuario FROM usuario WHERE email=? AND estatus = 'activo' ";
            ps=conexion.prepareStatement(sql);
            ps.setString(1,email);
            rs=ps.executeQuery();
            if (rs.next()){
                idUsuario = rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error interno al intentar recuperar el id del usuario "+e,"Error",JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion,ps,rs);
            }
        return idUsuario;
    }//id
}
