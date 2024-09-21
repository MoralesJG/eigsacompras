package com.eigsacompras.dao;

import com.eigsacompras.modelo.Auditoria;

import java.util.List;

public interface IAuditoriaDAO {
    boolean agregarAuditoria(Auditoria auditoria);
    List<Auditoria> listarAuditoria();
    boolean buscarAuditoriaPorId(int idAuditoria);

}
