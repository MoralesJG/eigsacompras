package com.eigsacompras.controlador;

import com.eigsacompras.dao.NotificacionDAO;
import com.eigsacompras.modelo.Notificacion;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class NotificacionControlador {
    NotificacionDAO notificacionDAO;

    public NotificacionControlador(){
        this.notificacionDAO = new NotificacionDAO();
    }

    public void agregarNotificacionControlador(LocalDate fecha, String mensaje, Integer idCompra){
        if(validarNotificacion(fecha,mensaje,idCompra)){
            Notificacion notificacion = new Notificacion(mensaje,fecha,idCompra);
            notificacionDAO.agregarNotificacion(notificacion);
        }else{
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío", JOptionPane.WARNING_MESSAGE);
        }
    }//agregar

    public List<Notificacion> listarNotificacion(){return notificacionDAO.listarNotificacion();}//listar

    public void actualizarNotificacion(LocalDate fecha, String mensaje, Integer idCompra, Integer idNotificacion){
        if(validarNotificacion(fecha,mensaje,idCompra)){
            Notificacion notificacion = new Notificacion(idNotificacion,mensaje,fecha,idCompra);
            notificacionDAO.actualizarNotificacion(notificacion);
        }else{
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío", JOptionPane.WARNING_MESSAGE);
        }
    }//actualizar

    public void eliminarNotificacion(Integer idNotificacion){
        notificacionDAO.eliminarNotificacion(idNotificacion);
    }//eliminar

    public boolean validarNotificacion(LocalDate fecha, String mensaje, Integer idCompra){
        if(fecha == null) return false;
        if(mensaje.isEmpty()) return false;
        if(idCompra==null) return false;

        return true;
     }//validar
}