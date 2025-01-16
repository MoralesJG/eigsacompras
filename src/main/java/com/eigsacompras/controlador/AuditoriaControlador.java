package com.eigsacompras.controlador;

import com.eigsacompras.dao.AuditoriaDAO;
import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.modelo.Auditoria;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AuditoriaControlador {
    private AuditoriaDAO auditoriaDAO;
    private Auditoria auditoria;

    public AuditoriaControlador(){
        auditoriaDAO = new AuditoriaDAO();
    }

    public boolean agregarAuditoria(String tabla, int idRegistro, TipoAccion accion, LocalDateTime fecha, String descripcion, int idUsuario){
        auditoria = new Auditoria(idUsuario,descripcion,fecha,accion,idRegistro,tabla);//no se valida campos vacios porque todo es interno
        return auditoriaDAO.agregarAuditoria(auditoria);
    }//agregar

    public List<Auditoria> filtrarAuditoria(String tablaAfectada, String usuario, String accion, String desde, String hasta, boolean todo){
        switch (validarFiltroFecha(desde, hasta, todo)) {
            case 1:
                //lo siguiente se valida con operadores ternarios
                return auditoriaDAO.filtrarAuditorias(null,null,
                        usuario.equals("Usuario") ? "" : usuario,
                        tablaAfectada.equals("Tabla afectada") ? "" : tablaAfectada,
                        accion.equals("Accion") ? "" : accion,
                        todo);
            case 2:
                return auditoriaDAO.filtrarAuditorias(LocalDate.parse(desde), LocalDate.parse(hasta),
                        usuario.equals("Usuario") ? "" : usuario,
                        tablaAfectada.equals("Tabla afectada") ? "" : tablaAfectada,
                        accion.equals("Accion") ? "" : accion,
                        todo);
            default:
                return null;
        }
    }

    public List<Auditoria> listarAuditoria(){
        return auditoriaDAO.listarAuditoria();
    }//listar

    public int validarFiltroFecha(String desde, String hasta, boolean todo){
        if(todo){
            return 1;
        }else{
            if(desde.equals("AAAA-MM-DD") || hasta.equals("AAAA-MM-DD")){
                JOptionPane.showMessageDialog(null, "Seleccionar 'Sin rango' para abarcar todas las fechas o agregar un rango de fechas","Aviso",JOptionPane.INFORMATION_MESSAGE);
                return 0;
            }else {
                try {
                    LocalDate.parse(desde);
                    LocalDate.parse(hasta);
                    return 2;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Fecha inválida. ¡Revisar!", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return 0;
                }//try catch
            }//if else desde/hasta vacio
        }//if else todo
    }//validar filtro fecha

}