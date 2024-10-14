package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoDisponibilidad;
import com.eigsacompras.modelo.Producto;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProveedorDAO implements IProveedorDAO{
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProveedorDAO(){
    }

    @Override
    public int agregarProveedor(Proveedor proveedor) {
        int idGenerado = -1;
        try {
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO proveedor (nombre,email,telefono,ubicacion) VALUES (?,?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,proveedor.getNombre());
            ps.setString(2,proveedor.getCorreo());
            ps.setString(3,proveedor.getTelefono());
            ps.setString(4,proveedor.getUbicacion());

            ps.executeUpdate();

            rs=ps.getGeneratedKeys();//se agrega el id de la compra al ser agregado
            if(rs.next())
                idGenerado=rs.getInt(1);
            return idGenerado;//se retorna el id que lo tomará el proveedor controlador

        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al agregar el Proveedor" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return idGenerado;
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
    }//agregar

    @Override
    public List<Proveedor> listarProveedor() {
        List<Proveedor> listaProveedor = new ArrayList<>();
        Map<Integer, Proveedor> proveedorMap = new HashMap<>();//para evitar duplicar proveedor
        try {
            String sql = "SELECT prov.*, pp.precio_ofrecido, pp.disponibilidad, prod.descripcion AS descripcion_producto " +
                    "FROM proveedor prov " +
                    "JOIN producto_proveedor pp ON prov.id_proveedor = pp.id_proveedor " +
                    "JOIN producto prod ON pp.id_producto = prod.id_producto " +
                    "ORDER BY prov.id_proveedor";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int idProveedor = rs.getInt("id_proveedor");
                Proveedor proveedor = proveedorMap.get(idProveedor);
                if (proveedor == null) {
                    proveedor = new Proveedor();
                    proveedor.setIdProveedor(idProveedor);
                    proveedor.setNombre(rs.getString("nombre"));
                    proveedor.setCorreo(rs.getString("email"));
                    proveedor.setTelefono(rs.getString("telefono"));
                    proveedor.setUbicacion(rs.getString("ubicacion"));
                    proveedor.setProductos(new ArrayList<>());
                    proveedorMap.put(idProveedor, proveedor);//lista al mapa
                }

                //Se crea el producto
                Producto producto = new Producto();
                producto.setDescripcion(rs.getString("descripcion_producto"));

                //se crea productoProveedor
                ProductoProveedor productoProveedor = new ProductoProveedor();
                productoProveedor.setPrecioOfrecido(rs.getDouble("precio_ofrecido"));
                productoProveedor.setDisponibilidad(TipoDisponibilidad.valueOf(rs.getString("disponibilidad")));
                productoProveedor.setProducto(producto);
                // Agregar el producto-proveedor a la lista de productos del proveedor
                proveedor.getProductos().add(productoProveedor);//se agrega productoProveedor a la lista en proveedor
            }
            listaProveedor.addAll(proveedorMap.values());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar el proveedor" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaProveedor;
    }//listar

    @Override
    public boolean actualizarProveedor(Proveedor proveedor) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE proveedor SET nombre=?,email=?,telefono=?,ubicacion=? WHERE id_proveedor=?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,proveedor.getNombre());
            ps.setString(2,proveedor.getCorreo());
            ps.setString(3,proveedor.getTelefono());
            ps.setString(4,proveedor.getUbicacion());
            ps.setInt(5,proveedor.getIdProveedor());//para el where
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el proveedor" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//actualizar

    @Override
    public boolean eliminarProveedor(int idProveedor) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM proveedor WHERE id_proveedor=?";
            ps=conexion.prepareStatement(sql);
            ps.setInt(1,idProveedor);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el proveedor" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar

    @Override
    public Proveedor buscarProveedorPorId(int idProveedor) {
        return null;
    }
}
