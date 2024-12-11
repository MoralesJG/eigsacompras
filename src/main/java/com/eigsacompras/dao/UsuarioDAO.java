package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.modelo.Notificacion;
import com.eigsacompras.modelo.Usuario;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            JOptionPane.showMessageDialog(null, "Error al agregar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            String sql = "SELECT * FROM usuario WHERE estatus = 'activo'";
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
            JOptionPane.showMessageDialog(null, "Error al listar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally

        return listaUsuario;
    }//listar

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE usuario SET nombre=?,email=?,tipo=? WHERE id_usuario=?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,usuario.getNombre());
            ps.setString(2,usuario.getCorreo());
            ps.setString(3,usuario.getTipo().name());
            ps.setInt(4,usuario.getIdUsuario());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//actualizar

    @Override
    public boolean desactivarUsuario(int idUsuario) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE usuario SET estatus = 'inactivo' WHERE id_usuario = ? ";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,idUsuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al desactivar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar

    @Override
    public List<Usuario> buscarUsuarios(String filtro){
        List<Usuario> listaUsuario = new ArrayList<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT * FROM usuario " +
                    "WHERE (nombre LIKE ? OR " +
                    "email LIKE ? OR " +
                    "tipo LIKE ?) " +
                    "AND estatus = 'activo' " +
                    "ORDER BY id_usuario";

            ps = conexion.prepareStatement(sql);
            String valorFiltro = "%" + filtro + "%";//para buscar en todo
            for (int i = 1; i <= 3; i++) {
                ps.setString(i, valorFiltro);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario(rs.getInt("id_usuario"),rs.getString("email"),rs.getString("nombre"),TipoAcceso.valueOf(rs.getString("tipo").toUpperCase()));
                listaUsuario.add(usuario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaUsuario;
    }//usuarios

    @Override
    public Usuario buscarUsuarioPorId(int idUsuario) {
        Usuario usuario = null;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT * FROM usuario WHERE estatus = 'activo' AND id_usuario = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()){
                if(usuario == null) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("email"));
                    usuario.setTipo(TipoAcceso.valueOf(rs.getString("tipo").toUpperCase()));
                }
            }//while
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally

        return usuario;
    }

    @Override
    public String obtenerPassword(String correoUsuario){
        String password = null;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT contrasena FROM usuario WHERE (email= ? OR nombre = ?) AND estatus = 'activo'";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,correoUsuario);
            ps.setString(2,correoUsuario);
            rs = ps.executeQuery();
            if (rs.next()){
                password =  rs.getString("contrasena");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al validar la contraseña " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return password;
    }

    @Override
    public boolean cambiarPassword(int idUsuario, String nuevoPassword){
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE usuario SET contrasena = ? WHERE id_usuario=?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1,nuevoPassword);
            ps.setInt(2,idUsuario);
            ps.executeUpdate();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al cambiar la contraseña. Intentar nuevamente." + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//cambioContraseña
}
