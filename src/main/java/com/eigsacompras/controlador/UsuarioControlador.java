package com.eigsacompras.controlador;

import com.eigsacompras.dao.UsuarioDAO;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.modelo.Usuario;
import com.eigsacompras.utilidades.EncriptarPassword;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public boolean agregarUsuario(String nombre, String correo, TipoAcceso tipo, String contrasena, String contrasenaConfirmada,int idUsuarioActivo) {
        if (!nombre.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty() && !contrasenaConfirmada.isEmpty()) {
            if(validarCorreo(correo)){
                if(validarPassword(contrasena)){
                    if(contrasena.equals(contrasenaConfirmada)) {
                        String hash = EncriptarPassword.encriptar(contrasena);//se encripta la contraseña
                        Usuario usuario = new Usuario(hash, correo, nombre, tipo);
                        int idUsuarioGenerado = usuarioDAO.agregarUsuario(usuario);
                        if(idUsuarioGenerado!=-1) {
                            mandarAuditoria(idUsuarioGenerado , TipoAccion.INSERTAR,"Se insertó un usuario con id = "+idUsuarioGenerado,idUsuarioActivo);
                            JOptionPane.showMessageDialog(null, "Usuario agregado correctamente.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                            return true;
                        }else{
                            return false;
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden, revisar","Validación de contraseñas",JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"La contraseña debe incluir, como mínimo, 8 caracteres entre alfabéticos y numéricos.","Validación de contraseña",JOptionPane.WARNING_MESSAGE);
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

    public boolean agregarUsuarioDesdeLogin(String nombre, String correo, TipoAcceso tipo, String contrasena, String contrasenaConfirmada){
        //este agregar es para evitar agregar una auditoria para cuando se añade un usuario desde el inicio de sesion/crear cuenta
        if (!nombre.isEmpty() && !correo.isEmpty() && !contrasena.isEmpty() && !contrasenaConfirmada.isEmpty()) {
            if(validarCorreo(correo)){
                if(validarPassword(contrasena)){
                    if(contrasena.equals(contrasenaConfirmada)) {
                        String hash = EncriptarPassword.encriptar(contrasena);//se encripta la contraseña
                        Usuario usuario = new Usuario(hash, correo, nombre, tipo);
                        if(usuarioDAO.agregarUsuario(usuario)!=-1){
                            JOptionPane.showMessageDialog(null, "Usuario agregado correctamente.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                            return true;
                        }else{
                            JOptionPane.showMessageDialog(null,"Usuario no agregado. Inténtalo nuevamente","Advertencia",JOptionPane.WARNING_MESSAGE);
                            return false;
                        }//if insercción de usuario
                    }else{
                        JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden, revisar","Validación de contraseñas",JOptionPane.WARNING_MESSAGE);
                        return false;
                    }//if constraseñas iguales
                }else{
                    JOptionPane.showMessageDialog(null,"La contraseña debe incluir, como mínimo, 8 caracteres entre alfabéticos y numéricos.","Validación de contraseña",JOptionPane.WARNING_MESSAGE);
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
    }//agregar usuario desde login

    public List<Usuario> listarUsuario() {
        return usuarioDAO.listarUsuario();
    }//listar

    public boolean actualizarUsuario(String nombre, String correo, TipoAcceso tipo, int idUsuarioActualizar, int idUsuarioActivo) {
        if (!nombre.isEmpty() && !correo.isEmpty()) {
            if(validarCorreo(correo)) {
                Usuario usuario = new Usuario(idUsuarioActualizar, correo, nombre, tipo);
                usuarioDAO.actualizarUsuario(usuario);
                mandarAuditoria(idUsuarioActualizar , TipoAccion.ACTUALIZAR,"Se actualizó el usuario con id = "+idUsuarioActualizar,idUsuarioActivo);
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

    public boolean desactivarUsuario(int idUsuarioDesactivar, int idUsuarioActivo) {
        int opc = JOptionPane.showConfirmDialog(null, "¿Está seguro de desactivar este usuario?\n Esto le bloqueará el acceso al sistema", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            if(usuarioDAO.desactivarUsuario(idUsuarioDesactivar)) {
                mandarAuditoria(idUsuarioDesactivar , TipoAccion.ELIMINAR,"Se eliminó el usuario con id = "+idUsuarioDesactivar,idUsuarioActivo);
                JOptionPane.showMessageDialog(null, "Usuario desactivado correctamente.", "Desactivado", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error al desactivar el usuario. Por favor, inténtelo nuevamente.", "No desactivado", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }//eliminar

    public List<Usuario> buscarUsuarios(String termino){
        return usuarioDAO.buscarUsuarios(termino);
    };//buscar usuario

    public Usuario buscarUsuarioPorId(int idUsuario){
        return usuarioDAO.buscarUsuarioPorId(idUsuario);
    }//buscar por id

    public int contarUsuarios(){
        return usuarioDAO.contarUsuarios();
    }//contar usuarios para crear el primer usuario tipo administrador

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

    public boolean validarUsuarioLogin(String correoUsuario, String password) {
        String hash = usuarioDAO.obtenerPassword(correoUsuario);
        if (hash != null) {
            return EncriptarPassword.verificar(password, hash);
        }
        return false;
    }//validar contraseña en la recuperación

    public int buscarUsuarioPorCorreoNombre(String parametro){
        return usuarioDAO.buscarUsuarioPorCorreoNombre(parametro);
    }//buscar por correo

    public void mandarAuditoria(int idRegistro, TipoAccion accion,String descripcion,int idUsuario){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime fecha = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        auditoriaControlador.agregarAuditoria("USUARIOS",idRegistro,accion,fecha,descripcion,idUsuario);
    }//auditoria
    
}