package com.eigsacompras.controlador;

import com.eigsacompras.dao.AuditoriaDAO;
import com.eigsacompras.dao.UsuarioDAO;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.modelo.RecuperacionPassword;
import com.eigsacompras.modelo.Usuario;
import com.eigsacompras.utilidades.EncriptarPassword;

import javax.swing.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsuarioControlador {
    private UsuarioDAO usuarioDAO;
    private AuditoriaControlador auditoriaControlador;

    public UsuarioControlador() {
        this.usuarioDAO = new UsuarioDAO();
        this.auditoriaControlador = new AuditoriaControlador();
    }

    public boolean agregarUsuario(String nombre, String correo, TipoAcceso tipo, String contrasena, String contrasenaConfirmada) {
        if (!nombre.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty()) {
            if(validarCorreo(correo)){
                if(validarPassword(contrasena)){
                    if(contrasena.equals(contrasenaConfirmada)) {
                        String hash = EncriptarPassword.encriptar(contrasena);//se encripta la contraseña
                        Usuario usuario = new Usuario(hash, correo, nombre, tipo);
                        usuarioDAO.agregarUsuario(usuario);
                        JOptionPane.showMessageDialog(null, "Usuario agregado correctamente.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }else{
                        JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden, revisar","Validación de contraseñas",JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"La contraseña debe incluir, como mínimo, caracteres alfabéticos y numéricos.","Validación de contraseña",JOptionPane.WARNING_MESSAGE);
                    return false;
                }//if validar contraseña
            }else{
                JOptionPane.showMessageDialog(null, "Correo inválido, revíselo", "Validación de coreo", JOptionPane.WARNING_MESSAGE);
                return false;
            }//if validar correo
        } else {
            JOptionPane.showMessageDialog(null, "Hay uno o más campos vacíos, revíselos", "Validación de campos", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }//if validar campos vacios
    }//agregar

    public List<Usuario> listarUsuario() {
        return usuarioDAO.listarUsuario();
    }//listar

    public boolean actualizarUsuario(String nombre, String correo, TipoAcceso tipo, int idUsuario) {
        if (!nombre.isEmpty() && !correo.isEmpty()) {
            if(validarCorreo(correo)) {
                Usuario usuario = new Usuario(idUsuario, correo, nombre, tipo);
                usuarioDAO.actualizarUsuario(usuario);
                JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }else{
                JOptionPane.showMessageDialog(null, "Correo inválido", "Correo", JOptionPane.WARNING_MESSAGE);
                return false;
            }//if validar correo
        } else {
            JOptionPane.showMessageDialog(null, "Hay uno o más campos vacíos, Revíselos", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }// if validar vacíos
    }//actualizar

    public void desactivarUsuario(int idUsuario) {
        int opc = JOptionPane.showConfirmDialog(null, "¿Está seguro de desactivar este usuario?\n Esto le bloqueará el acceso al sistema", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            if(usuarioDAO.desactivarUsuario(idUsuario)) {
                JOptionPane.showMessageDialog(null, "Usuario desactivado correctamente.", "Desactivado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al desactivar el usuario. Por favor, inténtelo nuevamente.", "No desactivado", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//eliminar

    public List<Usuario> buscarUsuarios(String termino){
        return usuarioDAO.buscarUsuarios(termino);
    };//buscar usuario

    public Usuario buscarUsuarioPorId(int idUsuario){
        return usuarioDAO.buscarUsuarioPorId(idUsuario);
    }//buscar por id

    public boolean cambiarPassword(int idUsuario, String nuevoPassword, String passwordConfirmada){
        if (!nuevoPassword.isEmpty() && !passwordConfirmada.isEmpty()) {
                if(validarPassword(nuevoPassword)){
                    if(nuevoPassword.equals(passwordConfirmada)) {
                        String hash = EncriptarPassword.encriptar(nuevoPassword);//se encripta la contraseña
                        if(usuarioDAO.cambiarPassword(idUsuario,hash)){
                            JOptionPane.showMessageDialog(null, "Contraseña modificada satisfactoriamente .", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                            return true;
                        }else return false;
                    }else{
                        JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden, revisar","Validación de contraseñas",JOptionPane.WARNING_MESSAGE);
                        return false;
                    }//if nuevo password y password confirmada
                }else{
                    JOptionPane.showMessageDialog(null,"La contraseña debe incluir, como mínimo, caracteres alfabéticos y numéricos.","Validación de contraseña",JOptionPane.WARNING_MESSAGE);
                    return false;
                }//if validar contraseña

        } else {
            JOptionPane.showMessageDialog(null, "Hay campos vacíos, revíselos", "Validación de campos", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }//if validar campos vacios
    }//cambiar password

    public boolean validarCorreo(String correo) {
        String emailExpresion = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailExpresion);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();//retorna true si cumple con lo requerido
    }//validar correo

    public boolean validarPassword(String contrasena) {
        String passwordExpresion ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\S]{8,}$";//minimo pide numeros y letras
        Pattern pattern = Pattern.compile(passwordExpresion);
        Matcher matcher = pattern.matcher(contrasena);
        return matcher.matches();
    }//para validar que sea segura antes de encriptar

    public boolean validarPasswordRecuperacion(String correo, String password) {
        String hash = usuarioDAO.obtenerPassword(correo);
        if (hash != null) {
            return EncriptarPassword.verificar(password, hash);
        }
        return false;
    }//validar contraseña en la recuperación
    
}