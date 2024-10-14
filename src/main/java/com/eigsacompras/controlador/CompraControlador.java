package com.eigsacompras.controlador;

import com.eigsacompras.dao.CompraDAO;
import com.eigsacompras.dao.CompraProductoDAO;
import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;
import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class CompraControlador {
    private CompraDAO compraDAO;
    private LocalDate fechaActual = LocalDate.now();
    private CompraProductoDAO compraProductoDAO;


    public CompraControlador(){
        this.compraDAO = new CompraDAO();
    }

    public void agregarCompras(String ordenCompra, String condiciones, LocalDate fechaEmision, String ordenTrabajo, LocalDate fechaEntrega, String agenteProveedor, String nombreComprador, String revisadoPor, String aprobadoPor, TipoEstatus estatus, String notasGenerales, TipoCompra tipo,LocalDate fechaInicioRenta,LocalDate fechaFinRenta,int idProveedor,int idUsuario, List<CompraProducto> compraProductos){
        if(validarCompra(ordenCompra, condiciones, fechaEmision, ordenTrabajo, fechaEntrega, agenteProveedor, nombreComprador, revisadoPor, aprobadoPor, estatus, notasGenerales, tipo,fechaInicioRenta,fechaFinRenta,idProveedor,idUsuario)){
            Compra compra = new Compra(condiciones,ordenCompra,ordenTrabajo,fechaEmision,fechaEntrega,nombreComprador,agenteProveedor,revisadoPor,notasGenerales,aprobadoPor,estatus,fechaInicioRenta,idProveedor,fechaFinRenta,tipo,idUsuario);
            try{
                int idCompra = compraDAO.agregarCompra(compra);

                //se agrega a la tabla CompraProducto
                for(CompraProducto producto: compraProductos){
                    producto.setIdCompra(idCompra);
                    compraProductoDAO.agregarCompraProducto(producto);
                }

                JOptionPane.showMessageDialog(null, "Compra agregada correctamente.", "Agregado", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error al agregar la compra. Por favor, inténtelo nuevamente. Error: "+e, "No agregado", JOptionPane.ERROR_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }
    }//agregar

    public List<Compra> listarCompra(){
        return compraDAO.listarCompras();
    };

    public void actualizarCompra(String ordenCompra, String condiciones, LocalDate fechaEmision, String ordenTrabajo, LocalDate fechaEntrega, String agenteProveedor, String nombreComprador, String revisadoPor, String aprobadoPor, TipoEstatus estatus, String notasGenerales, TipoCompra tipo,LocalDate fechaInicioRenta,LocalDate fechaFinRenta,int idProveedor,int idUsuario,int idCompra, List<CompraProducto> compraProductos){
        if(validarCompra(ordenCompra, condiciones, fechaEmision, ordenTrabajo, fechaEntrega, agenteProveedor, nombreComprador, revisadoPor, aprobadoPor, estatus, notasGenerales, tipo,fechaInicioRenta,fechaFinRenta,idProveedor,idUsuario)){
            Compra compra = new Compra(idCompra,condiciones,ordenCompra,ordenTrabajo,fechaEmision,fechaEntrega,nombreComprador,agenteProveedor,revisadoPor,notasGenerales,aprobadoPor,estatus,fechaInicioRenta,idProveedor,fechaFinRenta,tipo,idUsuario);

            if(compraDAO.actualizarCompra(compra)) {
                //se actualizar compraProducto
                for (CompraProducto producto : compraProductos) {
                    compraProductoDAO.actualizarCompraProducto(producto);
                }//for
                JOptionPane.showMessageDialog(null, "Todo actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Error al actualizar la compra. Por favor, inténtelo nuevamente.", "No actualizado", JOptionPane.ERROR_MESSAGE);
            }//if compraDAO
        }else{
            JOptionPane.showMessageDialog(null,"Hay uno o más campos vacíos, Revíselos","Campo vacío",JOptionPane.WARNING_MESSAGE);
        }//if validar
    }//actualizarCompra

    public void eliminarCompra(int idCompra){
        if(compraDAO.eliminarCompra(idCompra)){
            compraProductoDAO.eliminarCompraProducto(idCompra);
            JOptionPane.showMessageDialog(null, "Compra eliminada correctamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Error al eliminar la compra. Por favor, inténtelo nuevamente.", "No eliminado", JOptionPane.ERROR_MESSAGE);
        }
    }//eliminar

    public boolean validarCompra(String ordenCompra, String condiciones, LocalDate fechaEmision, String ordenTrabajo, LocalDate fechaEntrega, String agenteProveedor, String nombreComprador, String revisadoPor, String aprobadoPor, TipoEstatus estatus, String notasGenerales, TipoCompra tipo, LocalDate fechaInicioRenta, LocalDate fechaFinRenta, int idProveedor, int idUsuario){
        if(ordenCompra.isEmpty())  return false;
        if(condiciones.isEmpty()) return false;
        if(ordenTrabajo.isEmpty()) return false;
        if(fechaEntrega == null) return false;
        if(agenteProveedor.isEmpty()) return false;
        if(nombreComprador.isEmpty()) return false;
        if(revisadoPor.isEmpty()) return false;
        if(aprobadoPor.isEmpty()) return false;
        if(estatus == null) return false;
        if(tipo == null) return false;
        if(fechaEmision == null) return false;
        //evular el tipo de compra
        if(tipo == TipoCompra.RENTA){
            if(fechaInicioRenta == null) return false;
            if(fechaFinRenta == null) return false;
        }//tipo renta

        //evaluar la fecha de emision
        int diferencias = (int) ChronoUnit.DAYS.between(fechaActual,fechaEmision);
        if(diferencias>30 || diferencias<-30){
            int opc = JOptionPane.showConfirmDialog(null,"La fecha de emisión es muy diferente a la fecha actual, ¿Continuar de esta manera?","Confirmacion",JOptionPane.YES_NO_OPTION);
            if(opc==JOptionPane.NO_OPTION || opc == JOptionPane.CLOSED_OPTION){
                return false;
            }//Si el usuario da "Si" el flujo continua normalmentegit

        }//fechaemision

        //evaluar la fecha de entrega
        if(fechaEntrega.isBefore(fechaEmision)) {
            JOptionPane.showMessageDialog(null,"¡La fecha de entrega no puede ser menor a la de emision!");
            return false;
        };
        return true;
    }//validar
}
