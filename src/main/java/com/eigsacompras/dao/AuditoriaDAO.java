package com.eigsacompras.dao;

import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.modelo.Auditoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO implements IAuditoriaDAO{
    private Connection conexion;
    private PreparedStatement ps;
    public AuditoriaDAO(Connection conexion){
        this.conexion=conexion;
    }

    @Override
    public boolean agregarAuditoria(Auditoria auditoria) {
        String sql = "INSERT INTO auditoria (tabla_afectada,id_registro_afectado,accion,fecha_cambio,descripcion_cambio,id_usuario)" +
                "VALUES(?,?,?,?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1,auditoria.getTablaAfectada());
            ps.setInt(2,auditoria.getIdRegistroAfectado());
            ps.setString(3,auditoria.getAccion().name());
            ps.setDate(4,java.sql.Date.valueOf(auditoria.getFechaCambio()));
            ps.setString(5,auditoria.getDescripcion());
            ps.setInt(6,auditoria.getIdUsuario());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar "+e.getMessage());
            return false;
        }
    }//agregar

    @Override
    public List<Auditoria> listarAuditoria() {
        List<Auditoria> listaAuditoria = new ArrayList<>();
        String sql = "SELECT * FROM auditoria";
        try {
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Auditoria auditoria = new Auditoria();
                auditoria.setIdAuditoria(rs.getInt("id_auditoria"));
                auditoria.setTablaAfectada(rs.getString("tabla_afectada"));
                auditoria.setIdRegistroAfectado(rs.getInt("id_registro_afectado"));
                auditoria.setAccion(TipoAccion.valueOf(rs.getString("accion").toUpperCase()));
                auditoria.setFechaCambio(rs.getDate("fecha_cambio").toLocalDate());
                auditoria.setDescripcion(rs.getString("descripcion_cambio"));
                auditoria.setIdUsuario(rs.getInt("id_usuario"));

                listaAuditoria.add(auditoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar "+e.getMessage());
        }
        return listaAuditoria;
    }//listar

    @Override
    public boolean buscarAuditoriaPorId(int idAuditoria) {

        return false;
    }
}
