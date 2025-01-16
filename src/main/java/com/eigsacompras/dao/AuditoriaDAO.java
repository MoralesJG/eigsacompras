package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.modelo.Auditoria;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO implements IAuditoriaDAO{
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public AuditoriaDAO(){
    }

    @Override
    public boolean agregarAuditoria(Auditoria auditoria) {
        try {
            conexion= Conexion.getConexion();
            String sql = "INSERT INTO auditoria (tabla_afectada,id_registro_afectado,accion,fecha_cambio,descripcion_cambio,id_usuario)" +
                    "VALUES(?,?,?,?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,auditoria.getTablaAfectada());
            ps.setInt(2,auditoria.getIdRegistroAfectado());
            ps.setString(3,auditoria.getAccion().name());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(auditoria.getFechaCambio()));
            ps.setString(5,auditoria.getDescripcion());
            ps.setInt(6,auditoria.getIdUsuario());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear una auditoria: "+e, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//agregar

    @Override
    public List<Auditoria> listarAuditoria() {
        List<Auditoria> listaAuditoria = new ArrayList<>();
        try {
            conexion= Conexion.getConexion();
            String sql = "SELECT * FROM auditoria";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Auditoria auditoria = new Auditoria();
                auditoria.setIdAuditoria(rs.getInt("id_auditoria"));
                auditoria.setTablaAfectada(rs.getString("tabla_afectada"));
                auditoria.setIdRegistroAfectado(rs.getInt("id_registro_afectado"));
                auditoria.setAccion(TipoAccion.valueOf(rs.getString("accion").toUpperCase()));
                auditoria.setFechaCambio(rs.getTimestamp("fecha_cambio").toLocalDateTime());
                auditoria.setDescripcion(rs.getString("descripcion_cambio"));
                auditoria.setIdUsuario(rs.getInt("id_usuario"));

                listaAuditoria.add(auditoria);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar "+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally

        return listaAuditoria;
    }//listar

    @Override
    public List<Auditoria> filtrarAuditorias(LocalDate fechaDesde, LocalDate fechaHasta, String usuario,String tabla, String accion, boolean todo) {
        List<Auditoria> listaAuditoria = new ArrayList<>();
        try {
            conexion = Conexion.getConexion();
            StringBuilder sql = new StringBuilder(//se usa StringBuilder para concatenar más al sql
                    "SELECT a.*, u.nombre AS usuario " +
                            "FROM Auditoria a " +
                            "LEFT JOIN Usuario u ON a.id_usuario = u.id_usuario " +
                            "WHERE 1=1 ");

            //agregar condiciones si no están vacios
            if (!todo) {
                sql.append("AND a.fecha_cambio BETWEEN ? AND ? ");
            }
            if (usuario != null && !usuario.trim().isEmpty()) {
                sql.append("AND u.nombre LIKE ? ");
            }
            if (tabla != null && !tabla.trim().isEmpty()) {
                sql.append("AND a.tabla_afectada = ? ");
            }
            if (accion != null && !accion.trim().isEmpty()) {
                sql.append("AND a.accion = ? ");
            }
            sql.append("ORDER BY a.fecha_cambio DESC");

            ps = conexion.prepareStatement(sql.toString());
            int indice = 1;
            //ahora se asignan los parametros a las consultas del sql
            if (!todo) {
                ps.setDate(indice++, java.sql.Date.valueOf(fechaDesde));
                ps.setDate(indice++, java.sql.Date.valueOf(fechaHasta));
            }
            if (usuario != null && !usuario.isEmpty()) {
                ps.setString(indice++, "%" + usuario + "%");
            }
            if (tabla != null && !tabla.isEmpty()) {
                ps.setString(indice++, tabla);
            }
            if (accion != null && !accion.isEmpty()) {
                ps.setString(indice++, accion);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                Auditoria auditoria = new Auditoria();
                auditoria.setFechaCambio(rs.getTimestamp("fecha_cambio").toLocalDateTime());
                auditoria.setIdUsuario(rs.getInt("id_usuario"));
                auditoria.setAccion(TipoAccion.valueOf(rs.getString("accion").toUpperCase()));
                auditoria.setTablaAfectada(rs.getString("tabla_afectada"));
                auditoria.setIdRegistroAfectado(rs.getInt("id_registro_afectado"));
                auditoria.setDescripcion(rs.getString("descripcion_cambio"));

                listaAuditoria.add(auditoria);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al filtrar auditoría: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaAuditoria;
    }//filtrar auditorias
}
