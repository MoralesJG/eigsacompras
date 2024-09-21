package com.eigsacompras.dao;

import com.eigsacompras.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IProductoDAO{
    private Connection conexion;
    private PreparedStatement ps;

    public ProductoDAO(Connection conexion){
        this.conexion=conexion;
    }

    @Override
    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO producto (descripcion,precio) VALUES (?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1,producto.getDescripcion());
            ps.setDouble(2,producto.getPrecio());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar "+e.getMessage());
            return false;
        }
    }//insertar

    @Override
    public List<Producto> listarProducto() {
        List<Producto> listaProducto = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                listaProducto.add(producto);
            }//while
        } catch (SQLException e) {
            System.out.println("Error al listar "+e.getMessage());
        }
        return listaProducto;
    }//listar

    @Override
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE producto set descripcion=?,precio=? WHERE id_producto=?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1,producto.getDescripcion());
            ps.setDouble(2,producto.getPrecio());
            ps.setInt(3,producto.getIdProducto());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar "+e.getMessage());
            return false;
        }
    }//actualizar

    @Override
    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1,idProducto);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar "+e.getMessage());
            return false;
        }
    }//eliminar

    @Override
    public Producto buscarProductoPorId(int idProducto) {
        return null;
    }
}
