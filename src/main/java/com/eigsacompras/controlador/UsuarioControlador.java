package com.eigsacompras.controlador;

import com.eigsacompras.dao.UsuarioDAO;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.modelo.Usuario;

import javax.swing.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsuarioControlador {
    private UsuarioDAO usuarioDAO;

    public void UsuarioControlador(){
        this.usuarioDAO= new UsuarioDAO();
    }

    public void agregarUsuario(String nombre, String correo, TipoAcceso tipo, String contrasena){
        if(validarUsuario(nombre,correo,tipo,contrasena)){
            Usuario usuario = new Usuario(contrasena,correo,nombre,tipo);
            usuarioDAO.agregarUsuario(usuario);
        }else{
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío", JOptionPane.WARNING_MESSAGE);
        }
    }//agregar

    public List<Usuario> listarUsuario(){
        return usuarioDAO.listarUsuario();
    }//listar

    public void actualizarUsuario(String nombre, String correo, TipoAcceso tipo, String contrasena, int idUsuario){
        if(validarUsuario(nombre,correo,tipo,contrasena)){
            Usuario usuario = new Usuario(idUsuario,contrasena,correo,nombre,tipo);
            usuarioDAO.agregarUsuario(usuario);
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío", JOptionPane.WARNING_MESSAGE);
        }
    }//actualizar

    public void eliminarUsuario(int idUsuario){
        usuarioDAO.eliminarUsuario(idUsuario);
    }//eliminar

    public boolean validarUsuario(String nombre,String correo,TipoAcceso tipo,String contrasena){
        if(nombre.isEmpty()) return false;
        if(!validarCorreo(correo) || correo.isEmpty()) return false;
        if(tipo == null) return false;
        if(contrasena.isEmpty()) return false;

        return true;
    }

    public boolean validarCorreo(String correo){
        String emailExpresion = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailExpresion);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();//retorna true si cumple con lo requerido
    }//validar correo

}
