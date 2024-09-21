package com.eigsacompras.dao;

import com.eigsacompras.modelo.Usuario;

import java.util.List;

public interface IUsuarioDAO {
    boolean agregarUsuario(Usuario usuario);
    List<Usuario> listarUsuario();
    boolean actualizarUsuario(Usuario usuario);
    boolean eliminarUsuario(int idUsuario);
    Usuario buscarUsuarioPorId(int idUsuario);
}
