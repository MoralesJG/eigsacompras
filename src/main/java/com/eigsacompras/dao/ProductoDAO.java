package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
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
    private ResultSet rs;

    public ProductoDAO(){
    }

    @Override
    public int agregarProducto(Producto producto) {
        int idGenerado = -1;
        try {
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO producto (descripcion,precio) VALUES (?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,producto.getDescripcion());
            ps.setDouble(2,producto.getPrecio());
            ps.executeUpdate();

            rs=ps.getGeneratedKeys();//se agrega el id de la compra al ser agregado
            if(rs.next())
                idGenerado=rs.getInt(1);
            return idGenerado;//se retorna el id que lo tomará el producto controlador
        } catch (SQLException e) {
            System.out.println("Error al insertar "+e.getMessage());
            return idGenerado;
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
    }//insertar

    @Override
    public List<Producto> listarProducto() {
        List<Producto> listaProducto = new ArrayList<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT * FROM producto";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                listaProducto.add(producto);
            }//while
        } catch (SQLException e) {
            System.out.println("Error al listar "+e.getMessage());
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
        return listaProducto;
    }//listar

    @Override
    public boolean actualizarProducto(Producto producto) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE producto set descripcion=?,precio=? WHERE id_producto=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1,producto.getDescripcion());
            ps.setDouble(2,producto.getPrecio());
            ps.setInt(3,producto.getIdProducto());
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
    public boolean eliminarProducto(int idProducto) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM producto WHERE id_producto = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idProducto);
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
    public Producto buscarProductoPorId(int idProducto) {
        return null;
    }
}
