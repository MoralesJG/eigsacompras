package com.eigsacompras.dao;

import com.eigsacompras.modelo.CompraProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompraProductoDAO implements ICompraProductoDAO{
    private PreparedStatement ps;
    private Connection conexion;

    public CompraProductoDAO(Connection conexion){
        this.conexion=conexion;
    }

    @Override
    public boolean agregarCompraProducto(CompraProducto compraProducto) {
        String sql = "INSERT INTO compra_producto(id_compra,id_producto,partida,cantidad,precio_unitario,total)" +
                "VALUES (?,?,?,?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,compraProducto.getIdCompra());
            ps.setInt(2,compraProducto.getIdProducto());
            ps.setInt(3,compraProducto.getPartida());
            ps.setString(4,compraProducto.getCantidad());
            ps.setDouble(5,compraProducto.getPrecioUnitario());
            ps.setDouble(6,compraProducto.getTotal());

            ps.executeUpdate();

            return true;


        } catch (SQLException e) {
            System.out.println("Error al insertar "+e.getMessage());
            return false;
        }
    }//agregar

    @Override
    public List<CompraProducto> listarCompraProducto() {
        List<CompraProducto> listaCompraProducto = new ArrayList<>();
        String sql = "SELECT * FROM compra_producto";
        try {
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                CompraProducto compraProducto = new CompraProducto();
                compraProducto.setIdCompraProducto(rs.getInt("idcompra_producto"));
                compraProducto.setIdCompra(rs.getInt("id_compra"));
                compraProducto.setIdProducto(rs.getInt("id_producto"));
                compraProducto.setPartida(rs.getInt("partida"));
                compraProducto.setCantidad(rs.getString("cantidad"));
                compraProducto.setPrecioUnitario(rs.getDouble("precio_unitario"));
                compraProducto.setTotal(rs.getDouble("total"));

                listaCompraProducto.add(compraProducto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar "+e.getMessage());
        }
        return listaCompraProducto;
    }//listar

    @Override
    public boolean actualizarCompraProducto(CompraProducto compraProducto) {
        String sql = "UPDATE compra_producto SET id_compra=?, id_producto=?,partida=?,cantidad=?,precio_unitario=?,total=?" +
                "WHERE idcompra_producto=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,compraProducto.getIdCompra());
            ps.setInt(2,compraProducto.getIdProducto());
            ps.setInt(3,compraProducto.getPartida());
            ps.setString(4,compraProducto.getCantidad());
            ps.setDouble(5,compraProducto.getPrecioUnitario());
            ps.setDouble(6,compraProducto.getTotal());
            ps.setInt(7,compraProducto.getIdCompraProducto());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar");
            return false;
        }
    }//actualizar

    @Override
    public boolean eliminarCompraProducto(int idCompraProducto) {
        String sql = "DELETE FROM compra_producto WHERE idcompra_producto=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idCompraProducto);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar "+e.getMessage());
            return false;
        }
    }//eliminar

    @Override
    public boolean buscarCompraProductoPorId(int idCompraProducto) {
        return false;
    }
}
