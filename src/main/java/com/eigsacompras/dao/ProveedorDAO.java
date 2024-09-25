package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.modelo.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            System.out.println("Error al insertar"+e.getMessage());
            return idGenerado;
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
    }//agregar

    @Override
    public List<Proveedor> listarProveedor() {
        List<Proveedor> listaProveedor = new ArrayList<>();
        try{
            String sql = "SELECT * FROM proveedor";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(rs.getInt("id_proveedor"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setCorreo(rs.getString("email"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setUbicacion(rs.getString("ubicacion"));
                listaProveedor.add(proveedor);
            }//while
        }catch (SQLException e){
            System.out.println("Error al listar proveedor "+e.getMessage());
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
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
            System.out.println("Error al actualizar "+e.getMessage());
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
            System.out.println("Error al eliminar "+e.getMessage());
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
