package com.eigsacompras.test;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.AuditoriaDAO;
import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.modelo.Auditoria;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestAuditoriaDAO {
    public static void main(String[] args) {
        Connection conexion = Conexion.getConexion();
        AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

        Auditoria auditoria = new Auditoria();
        auditoria.setTablaAfectada("Compra");
        auditoria.setIdRegistroAfectado(1);
        auditoria.setAccion(TipoAccion.INSERTAR);
        //auditoria.setFechaCambio(LocalDate.now());
        auditoria.setDescripcion("La tabla compra fue alterada con un imsert ");
        auditoria.setIdUsuario(1);

        if(auditoriaDAO.agregarAuditoria(auditoria))
            System.out.println("Agregado exitosamente");
        else
            System.out.println("Error al agregar");
        System.out.println("-----LISTAR-----");
        for(Auditoria a:auditoriaDAO.listarAuditoria()){
            System.out.println(a);
        }
    }
}
