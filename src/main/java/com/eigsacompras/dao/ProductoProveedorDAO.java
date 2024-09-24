package com.eigsacompras.dao;

import com.eigsacompras.enums.TipoDisponibilidad;
import com.eigsacompras.modelo.ProductoProveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoProveedorDAO implements IProductoProveedorDAO{
    private PreparedStatement ps;
    private Connection conexion;

    public ProductoProveedorDAO(Connection conexion){
        this.conexion=conexion;
    }

    @Override
    public boolean agregarProductoProveedor(ProductoProveedor productoProveedor) {
        String sql = "INSERT INTO producto_proveedor (precio_ofrecido,disponibilidad,id_producto,id_proveedor)" +
                "VALUES (?,?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setDouble(1,productoProveedor.getPrecioOfrecido());
            ps.setString(2,productoProveedor.getDisponibilidad().name());
            ps.setInt(3,productoProveedor.getIdProducto());
            ps.setInt(4,productoProveedor.getIdProveedor());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar"+e.getMessage());
            return false;
        }
    }//agregar

    @Override
    public List<ProductoProveedor> listarProductoProveedor() {
        List<ProductoProveedor> listarProductoProveedor = new ArrayList<>();
        String sql = "SELECT * FROM producto_proveedor";
        try {
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ProductoProveedor productoProveedor = new ProductoProveedor();
                productoProveedor.setIdProductoProveedor(rs.getInt("idproducto_proveedor"));
                productoProveedor.setPrecioOfrecido(rs.getDouble("precio_ofrecido"));
                productoProveedor.setDisponibilidad(TipoDisponibilidad.valueOf(rs.getString("disponibilidad").toUpperCase()));
                productoProveedor.setIdProducto(rs.getInt("id_producto"));
                productoProveedor.setIdProveedor(rs.getInt("id_proveedor"));

                listarProductoProveedor.add(productoProveedor);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar"+e.getMessage());
        }
        return listarProductoProveedor;
    }

    @Override
    public boolean actualizarProductoProveedor(ProductoProveedor productoProveedor) {
        String sql = "UPDATE producto_proveedor SET precio_ofrecido=?,disponibilidad=?,id_producto=?,id_proveedor=? WHERE idproducto_proveedor=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setDouble(1,productoProveedor.getPrecioOfrecido());
            ps.setString(2,productoProveedor.getDisponibilidad().name());
            ps.setInt(3,productoProveedor.getIdProveedor());
            ps.setInt(4,productoProveedor.getIdProveedor());
            ps.setInt(5,productoProveedor.getIdProductoProveedor());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar "+e.getMessage());
            return false;
        }
    }//actualizar

    @Override
    public boolean eliminarProductoProveedor(int idProductoProveedor) {
        String sql = "DELETE FROM producto_proveedor WHERE idproducto_proveedor = ?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idProductoProveedor);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar "+e.getMessage());
            return false;
        }
    }//eliminar

    @Override
    public boolean buscarPorIdProductoProveedor(int idProductoProveedor) {
        return false;
    }
}
