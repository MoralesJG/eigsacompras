package com.eigsacompras.dao;

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

    public UsuarioDAO(Connection conexion){
        this.conexion=conexion;
    }
    @Override
    public boolean agregarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre,email,tipo,contrasena) VALUES(?,?,?,?)";
        try {
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
        }
    }//agregar

    @Override
    public List<Usuario> listarUsuario() {
        List<Usuario> listaUsuario = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try {
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
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
        }

        return listaUsuario;
    }//listar

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre=?,email=?,tipo=?,contrasena=? WHERE id_usuario=?";
        try {
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
        }
    }//actualizar

    @Override
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario=?";
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idUsuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar "+e.getMessage());
            return false;
        }
    }//eliminar

    @Override
    public Usuario buscarUsuarioPorId(int idUsuario) {
        return null;
    }
}
