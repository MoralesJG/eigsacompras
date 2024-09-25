package com.eigsacompras.test;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.NotificacionDAO;
import com.eigsacompras.modelo.Notificacion;

import java.sql.Connection;
import java.time.LocalDate;

public class TesNotificacion {
    public static void main(String[] args) {
        Connection conexion = Conexion.getConexion();
        NotificacionDAO notificacionDAO = new NotificacionDAO(conexion);

        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDate.now());
        notificacion.setMensaje("9333 muy desfazada");
        notificacion.setIdCompra(1);
        //notificacion.setIdNotificacion(2);

        if(notificacionDAO.agregarNotificacion(notificacion))
            System.out.println("Agrgado con exito");
        else
            System.out.println("Fallo agregar");

        System.out.println("-----LISTAR-----");
        for(Notificacion n:notificacionDAO.listarNotificacion()){
            System.out.println(n);
        }

    }
}
