package com.eigsacompras.dao;

import com.eigsacompras.modelo.Notificacion;

import java.util.List;

public interface INotificacionDAO {
    boolean agregarNotificacion(Notificacion notificacion);
    List<Notificacion> listarNotificacion();
    boolean actualizarNotificacion(Notificacion notificacion);
    boolean eliminarNotificacion(int idNotificacion);
    List<Notificacion> buscarNotificaciones(String filtro);


}
