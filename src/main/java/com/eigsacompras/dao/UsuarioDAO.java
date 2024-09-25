package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UsuarioDAO implements IUsuarioDAO{
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public UsuarioDAO(){
    }
    @Override
    public boolean agregarUsuario(Usuario usuario) {
        try {
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO usuario (nombre,email,tipo,contrasena) VALUES(?,?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,usuario.getNombre());
            ps.setString(2,usuario.getCorreo());
            ps.setString(3,usuario.getTipo().name());
            ps.setString(4,usuario.getContrasena());
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar"+ e.getMessage());
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//agregar

    @Override
    public List<Usuario> listarUsuario() {
        List<Usuario> listaUsuario = new ArrayList<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT * FROM usuario";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("email"));
                usuario.setTipo(TipoAcceso.valueOf(rs.getString("tipo").toUpperCase()));
                usuario.setContrasena(rs.getString("contrasena"));
                listaUsuario.add(usuario);
            }//while
        } catch (SQLException e) {
            System.out.println("Error al listar "+e.getMessage());
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally

        return listaUsuario;
    }//listar

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE usuario SET nombre=?,email=?,tipo=?,contrasena=? WHERE id_usuario=?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,usuario.getNombre());
            ps.setString(2,usuario.getCorreo());
            ps.setString(3,usuario.getTipo().name());
            ps.setString(4,usuario.getContrasena());
            ps.setInt(5,usuario.getIdUsuario());
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
    public boolean eliminarUsuario(int idUsuario) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM usuario WHERE id_usuario=?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idUsuario);
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
    public Usuario buscarUsuarioPorId(int idUsuario) {
        return null;
    }
}
