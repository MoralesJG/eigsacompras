package com.eigsacompras.dao;

import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.modelo.Auditoria;

import java.time.LocalDate;
import java.util.List;

public interface IAuditoriaDAO {
    boolean agregarAuditoria(Auditoria auditoria);
    List<Auditoria> listarAuditoria();
    List<Auditoria> filtrarAuditorias(LocalDate fechaDesde, LocalDate fechaHasta, String usuario, String tabla, String accion, boolean todo);

}
