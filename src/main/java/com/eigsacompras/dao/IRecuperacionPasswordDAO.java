package com.eigsacompras.dao;

import com.eigsacompras.modelo.RecuperacionPassword;

import java.util.List;

public interface IRecuperacionPasswordDAO {
    boolean agregarCodigoRecuperacion(RecuperacionPassword recuperacionPassword);
    boolean validarCodigoRecuperacion(String codigo, int idUsuario);
    boolean eliminarRecuperacionPassword(String codigo);
    int idUsuarioPorEmail(String email);

}
