package com.eigsacompras.dao;

import com.eigsacompras.modelo.Usuario;

import java.util.List;

public interface IUsuarioDAO {
    boolean agregarUsuario(Usuario usuario);
    List<Usuario> listarUsuario();
    boolean actualizarUsuario(Usuario usuario);
    boolean desactivarUsuario(int idUsuario);
    List<Usuario> buscarUsuarios(String termino);
    Usuario buscarUsuarioPorId(int idUsuario);
    String obtenerPassword(String correo);
    boolean cambiarPassword(int idUsuario, String password);

}
