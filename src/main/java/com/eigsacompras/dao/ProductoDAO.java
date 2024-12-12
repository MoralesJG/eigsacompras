package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.modelo.Producto;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAO implements IProductoDAO {
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProductoDAO() {
    }

    @Override
    public int agregarProducto(Producto producto) {
        int idGenerado = -1;
        try {
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO producto (descripcion) VALUES (?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, producto.getDescripcion());
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();//id generado al agregar un producto
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return idGenerado;
    }//insertar

    @Override
    public List<Producto> listarProducto() {
        List<Producto> listaProductos = new ArrayList<>();
        Map<Integer, Producto> productoMap = new HashMap<>();//evita duplicados
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT prod.*, pp.precio_ofrecido, prov.nombre AS nombre_proveedor " +
                    "FROM producto prod " +
                    "LEFT JOIN producto_proveedor pp ON prod.id_producto = pp.id_producto " +
                    "LEFT JOIN proveedor prov ON pp.id_proveedor = prov.id_proveedor ";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                Producto producto = productoMap.get(idProducto);
                if (producto == null) {
                    producto = new Producto();
                    producto.setIdProducto(idProducto);
                    producto.setDescripcion(rs.getString("descripcion"));
                    productoMap.put(idProducto, producto);
                }

                if(rs.getString("precio_ofrecido")!=null) {
                    ProductoProveedor productoProveedor = new ProductoProveedor();
                    productoProveedor.setPrecioOfrecido(rs.getDouble("precio_ofrecido"));

                    Proveedor proveedor = new Proveedor();
                    proveedor.setNombre(rs.getString("nombre_proveedor"));

                    productoProveedor.setProveedor(proveedor);

                    producto.getProveedores().add(productoProveedor);
                }//if
            }//while
            listaProductos.addAll(productoMap.values());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaProductos;
    }//listar

    @Override
    public boolean actualizarProducto(Producto producto) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE producto set descripcion=? WHERE id_producto=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, producto.getDescripcion());
            ps.setInt(2, producto.getIdProducto());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//actualizar

    @Override
    public boolean eliminarProducto(int idProducto) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM producto WHERE id_producto = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar un producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar

    @Override
    public Producto buscarProductoPorId(int idProducto) {
        Producto producto = null;
        Map<Integer, ProductoProveedor> proveedorMap = new HashMap<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT prod.*, pp.precio_ofrecido, pp.id_proveedor, prov.nombre AS nombre_proveedor " +
                    "FROM producto prod " +
                    "LEFT JOIN producto_proveedor pp ON prod.id_producto = pp.id_producto " +
                    "LEFT JOIN proveedor prov ON pp.id_proveedor = prov.id_proveedor " +
                    "WHERE prod.id_producto = ? ";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProducto);

            rs = ps.executeQuery();
            while (rs.next()) {
                if(producto == null) {
                    producto = new Producto();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setProveedores(new ArrayList<>());
                }
                int idProveedor = rs.getInt("id_proveedor");
                if (idProveedor != 0 && !proveedorMap.containsKey(idProveedor)) {//si no hay proveedor continua normal el codigo
                    ProductoProveedor productoProveedor = new ProductoProveedor();
                    productoProveedor.setPrecioOfrecido(rs.getDouble("precio_ofrecido"));

                    Proveedor proveedor = new Proveedor();
                    proveedor.setNombre(rs.getString("nombre_proveedor"));
                    productoProveedor.setProveedor(proveedor);

                    producto.getProveedores().add(productoProveedor);
                    proveedorMap.put(idProveedor, productoProveedor);

                }//if
            }//while
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar producto por Id: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return producto;
    }//buscar por id

    @Override
    public List<Producto> buscarProductos(String termino) {
        List<Producto> listaProductos = new ArrayList<>();
        Map<Integer, Producto> productoMap = new HashMap<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT prod.*, pp.precio_ofrecido, pp.id_proveedor, prov.nombre AS nombre_proveedor " +
                    "FROM producto prod " +
                    "LEFT JOIN producto_proveedor pp ON prod.id_producto = pp.id_producto " +
                    "LEFT JOIN proveedor prov ON pp.id_proveedor = prov.id_proveedor " +
                    "WHERE prod.descripcion LIKE ? OR prov.nombre LIKE ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + termino + "%");
            ps.setString(2, "%" + termino + "%");

            rs = ps.executeQuery();
            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                Producto producto = productoMap.get(idProducto);
                if (producto == null) {
                    producto = new Producto();
                    producto.setIdProducto(idProducto);
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setProveedores(new ArrayList<>());
                    productoMap.put(idProducto, producto);
                }
                if(rs.getString("pp.id_proveedor")!=null) {//si no hay proveedores no se asigna nada
                    ProductoProveedor productoProveedor = new ProductoProveedor();
                    productoProveedor.setIdProveedor(rs.getInt("pp.id_proveedor"));
                    productoProveedor.setPrecioOfrecido(rs.getDouble("precio_ofrecido"));

                    Proveedor proveedor = new Proveedor();
                    proveedor.setNombre(rs.getString("nombre_proveedor"));
                    productoProveedor.setProveedor(proveedor);

                    producto.getProveedores().add(productoProveedor);
                }//if
            }//while

            listaProductos.addAll(productoMap.values());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaProductos;
    }//buscar productos


}
