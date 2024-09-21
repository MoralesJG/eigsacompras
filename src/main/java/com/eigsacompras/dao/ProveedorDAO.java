package com.eigsacompras.dao;

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

    public ProveedorDAO(Connection conexion){
        this.conexion=conexion;
    }

    @Override
    public boolean agregarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO proveedor (nombre,email,telefono,ubicacion) VALUES (?,?,?,?)";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1,proveedor.getNombre());
            ps.setString(2,proveedor.getCorreo());
            ps.setString(3,proveedor.getTelefono());
            ps.setString(4,proveedor.getUbicacion());

            ps.executeUpdate();
            return true;
        }catch (SQLException e){
            System.out.println("Error al insertar"+e.getMessage());
            return false;
        }
    }//agregar

    @Override
    public List<Proveedor> listarProveedor() {
        List<Proveedor> listaProveedor = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";
        try{
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
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
        }//try
        return listaProveedor;
    }//listar

    @Override
    public boolean actualizarProveedor(Proveedor proveedor) {
        String sql = "UPDATE proveedor SET nombre=?,email=?,telefono=?,ubicacion=? WHERE id_proveedor=?";
        try {
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
        }
    }//actualizar

    @Override
    public boolean eliminarProveedor(int idProveedor) {
        String sql = "DELETE FROM proveedor WHERE id_proveedor=?";
        try {
            ps=conexion.prepareStatement(sql);
            ps.setInt(1,idProveedor);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar "+e.getMessage());
            return false;
        }
    }//eliminar

    @Override
    public Proveedor buscarProveedorPorId(int idProveedor) {
        return null;
    }
}
