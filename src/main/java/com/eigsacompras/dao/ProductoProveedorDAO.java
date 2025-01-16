package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoDisponibilidad;
import com.eigsacompras.modelo.ProductoProveedor;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoProveedorDAO implements IProductoProveedorDAO{
    private Connection conexion;
    private ResultSet rs;
    private PreparedStatement ps;

    public ProductoProveedorDAO(){
    }

    @Override
    public boolean agregarProductoProveedor(ProductoProveedor productoProveedor) {
        try {
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO producto_proveedor (precio_ofrecido,disponibilidad,id_producto,id_proveedor)" +
                    "VALUES (?,?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setDouble(1,productoProveedor.getPrecioOfrecido());
            ps.setString(2,productoProveedor.getDisponibilidad().name());
            ps.setInt(3,productoProveedor.getIdProducto());
            ps.setInt(4,productoProveedor.getIdProveedor());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar a Producto Proveedor \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//agregar

    @Override
    public List<ProductoProveedor> listarProductoProveedor() {
        List<ProductoProveedor> listarProductoProveedor = new ArrayList<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT * FROM producto_proveedor";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
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
            JOptionPane.showMessageDialog(null, "Error al mostrar los productos proveedores \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally

        return listarProductoProveedor;
    }//listar

    @Override
    public boolean actualizarProductoProveedor(ProductoProveedor productoProveedor) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE producto_proveedor SET precio_ofrecido=?,disponibilidad=?,id_producto=?,id_proveedor=? WHERE idproducto_proveedor=?";
            ps = conexion.prepareStatement(sql);
            ps.setDouble(1,productoProveedor.getPrecioOfrecido());
            ps.setString(2,productoProveedor.getDisponibilidad().name());
            ps.setInt(3,productoProveedor.getIdProveedor());
            ps.setInt(4,productoProveedor.getIdProveedor());
            ps.setInt(5,productoProveedor.getIdProductoProveedor());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar los productos proveedoresmes \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//actualizar

    @Override
    public boolean eliminarProductoProveedorPorIdProveedor(int idProveedor) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM producto_proveedor WHERE id_proveedor = ?";//se elimina a traves del id proveedor
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idProveedor);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar un producto proveedor \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar por id proveedor

    @Override
    public boolean eliminarProductoProveedorPorIdProducto(int idProducto){
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM producto_proveedor WHERE id_producto = ?";//se elimina a traves del id producto
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idProducto);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar un producto proveedor \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar producto proveedor

    @Override
    public boolean buscarPorIdProductoProveedor(int idProductoProveedor) {
        return false;
    }
}
