package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoDisponibilidad;
import com.eigsacompras.modelo.Producto;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;
import javax.swing.*;
import java.sql.*;
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
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//para que pueda retornar la llave primaria generada
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
            conexion = Conexion.getConexion();
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
                productoProveedor.setDisponibilidad(TipoDisponibilidad.valueOf(rs.getString("disponibilidad").toUpperCase()));
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
        Proveedor proveedor = null;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT prov.*, pp.precio_ofrecido, pp.disponibilidad, prod.descripcion AS descripcion_producto " +
                    "FROM proveedor prov " +
                    "JOIN producto_proveedor pp ON prov.id_proveedor = pp.id_proveedor " +
                    "JOIN producto prod ON pp.id_producto = prod.id_producto " +
                    "WHERE prov.id_proveedor = ? ";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProveedor);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (proveedor == null) {
                    proveedor = new Proveedor();
                    proveedor.setIdProveedor(rs.getInt("id_proveedor"));
                    proveedor.setNombre(rs.getString("nombre"));
                    proveedor.setCorreo(rs.getString("email"));
                    proveedor.setTelefono(rs.getString("telefono"));
                    proveedor.setUbicacion(rs.getString("ubicacion"));
                    proveedor.setProductos(new ArrayList<>());
                }//if

                Producto producto = new Producto();
                producto.setDescripcion(rs.getString("descripcion_producto"));

                ProductoProveedor productoProveedor = new ProductoProveedor();
                productoProveedor.setPrecioOfrecido(rs.getDouble("precio_ofrecido"));
                productoProveedor.setDisponibilidad(TipoDisponibilidad.valueOf(rs.getString("disponibilidad").toUpperCase()));
                productoProveedor.setProducto(producto);

                proveedor.getProductos().add(productoProveedor);
            }//while
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el proveedor \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return proveedor;
    }//buscar por id

    @Override
    public List<Proveedor> buscarProveedores(String filtro) {
        List<Proveedor> listaProveedor = new ArrayList<>();
        Map<Integer, Proveedor> proveedorMap = new HashMap<>();//para que nno se agreguen duplicados
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT prov.*, pp.precio_ofrecido, pp.disponibilidad, prod.descripcion AS descripcion_producto " +
                    "FROM proveedor prov " +
                    "LEFT JOIN producto_proveedor pp ON prov.id_proveedor = pp.id_proveedor " +
                    "LEFT JOIN producto prod ON pp.id_producto = prod.id_producto " +
                    "WHERE prov.nombre LIKE ? OR " +
                    "prov.email LIKE ? OR " +
                    "prov.telefono LIKE ? OR " +
                    "prov.ubicacion LIKE ? OR " +
                    "prod.descripcion LIKE ? OR " +
                    "pp.precio_ofrecido LIKE ? OR " +
                    "pp.disponibilidad LIKE ? " +
                    "ORDER BY prov.id_proveedor";
            ps = conexion.prepareStatement(sql);
            String valorFiltro = "%" + filtro + "%";//para buscar en todo
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, valorFiltro);
            }
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
                    proveedorMap.put(idProveedor, proveedor);
                }
                Producto producto = new Producto();
                producto.setDescripcion(rs.getString("descripcion_producto"));

                ProductoProveedor productoProveedor = new ProductoProveedor();
                productoProveedor.setPrecioOfrecido(rs.getDouble("precio_ofrecido"));
                productoProveedor.setDisponibilidad(TipoDisponibilidad.valueOf(rs.getString("disponibilidad").toUpperCase()));
                productoProveedor.setProducto(producto);

                proveedor.getProductos().add(productoProveedor);
            }
            listaProveedor.addAll(proveedorMap.values());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaProveedor;
    }//buscar proveedores

}
