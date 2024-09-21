package com.eigsacompras.dao;

import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.modelo.Compra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO implements ICompraDAO{
    private Connection conexion;

    public CompraDAO(Connection conexion){
        this.conexion=conexion;
    }
    @Override
    public boolean agregarCompra(Compra compra) {
        String sql = "INSERT INTO compra (orden_compra,condiciones,fecha_emision,orden_trabajo, fecha_entrega,agente_proveedor,nombre_comprador,revisado_por,aprobado_por,estatus,notas_generales,tipo,fecha_inicio_renta,fecha_fin_renta,id_proveedor,id_usuario)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, compra.getOrdenCompra());
            ps.setString(2, compra.getCondiciones());
            ps.setDate(3,java.sql.Date.valueOf(compra.getFechaEmision()));
            ps.setString(4, compra.getOrdenTrabajo());
            ps.setDate(5, java.sql.Date.valueOf(compra.getFechaEntrega()));
            ps.setString(6, compra.getAgenteProveedor());
            ps.setString(7, compra.getNombreComprador());
            ps.setString(8, compra.getRevisadoPor());
            ps.setString(9, compra.getAprobadoPor());
            ps.setString(10, compra.getEstatus().name());
            ps.setString(11, compra.getNotasGenerales());
            ps.setString(12, compra.getTipo().name());
            if(compra.getTipo()==TipoCompra.RENTA){//si es de tipo renta la orden se agrega esto
                ps.setDate(13,java.sql.Date.valueOf(compra.getFechaInicioRenta()));
                ps.setDate(14,java.sql.Date.valueOf(compra.getFechaFinRenta()));
            }else{
                ps.setNull(13,java.sql.Types.DATE);
                ps.setNull(14, java.sql.Types.DATE);
            }
            ps.setInt(15, compra.getIdProveedor());
            ps.setInt(16, compra.getIdUsuario());

            ps.executeUpdate();
            return true;
        }catch (Exception e){
            System.out.println("Error al registrar la orden"+e.getMessage());
            return false;
        }
    }//cierre de agregarCompra

    @Override
    public List<Compra> listarCompras() {
        List<Compra> listaCompras = new ArrayList<>();
        String sql = "SELECT * FROM compra";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("id_compra"));
                compra.setOrdenCompra(rs.getString("orden_compra"));
                compra.setCondiciones(rs.getString("Condiciones"));
                compra.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
                compra.setOrdenTrabajo(rs.getString("orden_trabajo"));
                compra.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
                compra.setAgenteProveedor(rs.getString("agente_proveedor"));
                compra.setNombreComprador(rs.getString("nombre_comprador"));
                compra.setRevisadoPor(rs.getString("revisado_por"));
                compra.setAprobadoPor(rs.getString("aprobado_por"));
                compra.setEstatus(TipoEstatus.valueOf(rs.getString("estatus")));
                compra.setNotasGenerales(rs.getString("notas_generales"));
                compra.setTipo(TipoCompra.valueOf(rs.getString("tipo")));
                compra.setFechaInicioRenta(rs.getDate("fecha_inicio_renta").toLocalDate());
                compra.setFechaFinRenta(rs.getDate("fecha_fin_renta").toLocalDate());
                compra.setIdProveedor(rs.getInt("id_proveedor"));
                compra.setIdUsuario(rs.getInt("id_usuario"));

                listaCompras.add(compra);
            }
        }catch (SQLException e){
            System.out.println("Error al mostrar las compras"+e.getMessage());
        }
        return listaCompras;
    }//listar

    @Override
    public boolean actualizarCompra(Compra compra) {
        String sql = "UPDATE compra SET orden_compra = ?, condiciones = ?, fecha_emision = ?, orden_trabajo = ?, fecha_entrega = ?, agente_proveedor = ?, nombre_comprador = ?, revisado_por = ?, aprobado_por = ?, estatus = ?, notas_generales = ?, tipo = ?, fecha_inicio_renta=?,fecha_fin_renta=?,id_proveedor=?,id_usuario=? WHERE id_compra = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, compra.getOrdenCompra());
            ps.setString(2, compra.getCondiciones());
            ps.setDate(3, java.sql.Date.valueOf(compra.getFechaEmision()));
            ps.setString(4, compra.getOrdenTrabajo());
            ps.setDate(5, java.sql.Date.valueOf(compra.getFechaEntrega()));
            ps.setString(6, compra.getAgenteProveedor());
            ps.setString(7, compra.getNombreComprador());
            ps.setString(8, compra.getRevisadoPor());
            ps.setString(9, compra.getAprobadoPor());
            ps.setString(10, compra.getEstatus().name());
            ps.setString(11, compra.getNotasGenerales());
            ps.setString(12, compra.getTipo().name());
            if (compra.getTipo() == TipoCompra.RENTA) {// Si tipo renta se actuliza
                ps.setDate(13, java.sql.Date.valueOf(compra.getFechaInicioRenta()));
                ps.setDate(14, java.sql.Date.valueOf(compra.getFechaFinRenta()));
            } else {//sino se manda null
                ps.setNull(13, java.sql.Types.DATE);
                ps.setNull(14, java.sql.Types.DATE);
            }
            ps.setInt(15, compra.getIdProveedor());
            ps.setInt(16, compra.getIdUsuario());
            ps.setInt(17, compra.getIdCompra());//para el where
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar la compra: " + e.getMessage());
            return false;
        }
    }//actualizar

    @Override
    public boolean eliminarCompra(int idCompra) {
        String sql = "DELETE FROM compra WHERE id_compra = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCompra);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar la compra: " + e.getMessage());
            return false;
        }
    }//eliminar

    @Override
    public Compra buscarCompraPorId(int idCompra) {
        return null;
    }
}
