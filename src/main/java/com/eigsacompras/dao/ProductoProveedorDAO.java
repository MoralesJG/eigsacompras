package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoDisponibilidad;
import com.eigsacompras.modelo.ProductoProveedor;

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
            System.out.println("Error al agregar"+e.getMessage());
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
            System.out.println("Error al listar"+e.getMessage());
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
            System.out.println("Error al actualizar "+e.getMessage());
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//actualizar

    @Override
    public boolean eliminarProductoProveedor(int idProductoProveedor) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM producto_proveedor WHERE idproducto_proveedor = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idProductoProveedor);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar "+e.getMessage());
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar

    @Override
    public boolean buscarPorIdProductoProveedor(int idProductoProveedor) {
        return false;
    }
}
