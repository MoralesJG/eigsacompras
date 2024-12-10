package com.eigsacompras.controlador;

import com.eigsacompras.dao.AuditoriaDAO;
import com.eigsacompras.modelo.Auditoria;

import java.util.Date;
import java.util.List;

public class AuditoriaControlador {
    private AuditoriaDAO auditoriaDAO;

    public AuditoriaControlador(){
        auditoriaDAO = new AuditoriaDAO();
    }

    public List<Auditoria> listarAuditoria(){
        return auditoriaDAO.listarAuditoria();
    }//listar



}