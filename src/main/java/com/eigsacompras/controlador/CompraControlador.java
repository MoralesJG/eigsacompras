package com.eigsacompras.controlador;

import com.eigsacompras.dao.CompraDAO;
import com.eigsacompras.dao.CompraProductoDAO;
import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class CompraControlador {
    private CompraDAO compraDAO;
    private LocalDate fechaActual = LocalDate.now();
    private AuditoriaControlador auditoriaControlador;


    public CompraControlador() {
        this.compraDAO = new CompraDAO();
        auditoriaControlador = new AuditoriaControlador();
    }

    public boolean agregarCompras(String ordenCompra, String condiciones, LocalDate fechaEmision, String ordenTrabajo, LocalDate fechaEntrega, String agenteProveedor, String nombreComprador, String revisadoPor, String aprobadoPor, TipoEstatus estatus, String notasGenerales, TipoCompra tipo, LocalDate fechaInicioRenta, LocalDate fechaFinRenta, int idProveedor, int idUsuario, List<CompraProducto> compraProductos) {
        if (validarCompra(ordenCompra, condiciones, fechaEmision, ordenTrabajo, fechaEntrega, agenteProveedor, nombreComprador, revisadoPor, aprobadoPor, estatus, notasGenerales, tipo, fechaInicioRenta, fechaFinRenta, idProveedor, idUsuario)) {
            Compra compra = new Compra(condiciones, ordenCompra, ordenTrabajo, fechaEmision, fechaEntrega, nombreComprador, agenteProveedor, revisadoPor, notasGenerales, aprobadoPor, estatus, fechaInicioRenta, idProveedor, fechaFinRenta, tipo, idUsuario);

            if (!compraProductos.isEmpty()) {//revisar que haya productos en la lista
                try {
                    int idCompra = compraDAO.agregarCompra(compra);

                    //se agrega a la tabla CompraProducto
                    for (CompraProducto producto : compraProductos) {
                        producto.setIdCompra(idCompra);
                        new CompraProductoDAO().agregarCompraProducto(producto);
                    }//for
                    new NotificacionControlador().agregarNotificacion(fechaEntrega,"Orden No. "+ordenCompra.replaceAll("\\D+",""),idCompra);//se agrega la notificación
                    mandarAuditoria(idCompra,TipoAccion.INSERTAR,"Se insertó una compra con id = "+idCompra,idUsuario);
                    JOptionPane.showMessageDialog(null, "Compra agregada correctamente.", "Agregado", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al agregar la compra. Por favor, inténtelo nuevamente. Error: " + e, "No agregado", JOptionPane.ERROR_MESSAGE);
                    return false;
                }//try de agregar compra

            } else {
                JOptionPane.showMessageDialog(null, "Agregue al menos 1 producto.", "Sin productos", JOptionPane.ERROR_MESSAGE);
                return false;
            }//if compraProductos vacios
        } else {
            JOptionPane.showMessageDialog(null, "Hay uno o más campos vacíos, Revíselos", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }

    }//agregar

    public List<Compra> listarCompra() {
        return compraDAO.listarCompras();
    }

    public int listarComprasPendientes() {
        return compraDAO.listarComprasPendientes();
    }//comprasPendientes

    public int listarComprasDelMes() {
        return compraDAO.listarComprasDelMes();
    }//compras mes

    public int listarComprasTotales() {
        return compraDAO.listarComprasTotales();
    }//comprasTotales

    public Compra listarProximoEntregar() {
        return compraDAO.listarProximoEntregar();
    }//promixo a entrega

    public Compra listarCompraPorId(int idCompra) {
        return compraDAO.buscarCompraPorId(idCompra);
    }//buscar por id

    public boolean actualizarCompra(String ordenCompra, String condiciones, LocalDate fechaEmision, String ordenTrabajo, LocalDate fechaEntrega, String agenteProveedor, String nombreComprador, String revisadoPor, String aprobadoPor, TipoEstatus estatus, String notasGenerales, TipoCompra tipo, LocalDate fechaInicioRenta, LocalDate fechaFinRenta, int idProveedor, int idUsuario, int idCompra, List<CompraProducto> compraProductos) {
        if (validarCompra(ordenCompra, condiciones, fechaEmision, ordenTrabajo, fechaEntrega, agenteProveedor, nombreComprador, revisadoPor, aprobadoPor, estatus, notasGenerales, tipo, fechaInicioRenta, fechaFinRenta, idProveedor, idUsuario)) {

            Compra compra = new Compra(idCompra, condiciones, ordenCompra, ordenTrabajo, fechaEmision, fechaEntrega, nombreComprador, agenteProveedor, revisadoPor, notasGenerales, aprobadoPor, estatus, fechaInicioRenta, idProveedor, fechaFinRenta, tipo, idUsuario);
            if (!compraProductos.isEmpty()) {//revisar que haya productos en la lista
                if (compraDAO.actualizarCompra(compra)) {
                    //eliminar compraProducto
                    new CompraProductoDAO().eliminarCompraProducto(idCompra);
                    //agregar compraProductos
                    for (CompraProducto producto : compraProductos) {
                        producto.setIdCompra(idCompra);
                        new CompraProductoDAO().agregarCompraProducto(producto);
                    }//for
                    //se eliminan todos los productos asociados a la compra ya que si se modifica la compra y se quieren agregar otros productos si solo se actualiza no se podrá agregar lo nuevos productos a la compra...
                    //...por lo que para asegurar que se actualice correctamente incluyendo los nuevos productos que se quieren agregar es necesario eliminar todo y agregar todo lo nuevo
                    if(estatus.equals(TipoEstatus.ENTREGADO) || estatus.equals(TipoEstatus.CANCELADO)){
                        new NotificacionControlador().eliminarNotificacion(idCompra);//se elimina la notificacion si ya fue entregada la compra
                    }else{
                        new NotificacionControlador().actualizarNotificacion(fechaEntrega,"Orden No. "+ordenCompra.replaceAll("\\D+",""),idCompra);//se actualiza la notificacion
                    }//cierre if
                    mandarAuditoria(idCompra,TipoAccion.ACTUALIZAR,"Se actualizó la compra con id = "+idCompra,idUsuario);
                    JOptionPane.showMessageDialog(null, "Todo actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar la compra. Por favor, inténtelo nuevamente.", "No actualizado", JOptionPane.ERROR_MESSAGE);
                    return false;
                }//if compraDAO
            }else{
                JOptionPane.showMessageDialog(null, "Agregue al menos 1 producto.", "Sin productos", JOptionPane.ERROR_MESSAGE);
                return false;
            }//if que valida si hay compraproductos
        } else {
            JOptionPane.showMessageDialog(null, "Hay uno o más campos vacíos, Revíselos", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }//if validar
    }//actualizarCompra

    public void eliminarCompra(int idCompra,int idUsuario) {
        int opc = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar esta compra?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            if (new CompraProductoDAO().eliminarCompraProducto(idCompra)) {
                new NotificacionControlador().eliminarNotificacion(idCompra);
                compraDAO.eliminarCompra(idCompra);
                JOptionPane.showMessageDialog(null, "Compra eliminada correctamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                mandarAuditoria(idCompra,TipoAccion.ELIMINAR,"Se eliminó la compra con id = "+idCompra,idUsuario);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar la compra. Por favor, inténtelo nuevamente.", "No eliminado", JOptionPane.ERROR_MESSAGE);
            }
        }//JOPane

    }//eliminar

    public List<Compra> buscarCompra(String termino) {
        return compraDAO.buscarCompras(termino);
    }//buscar compra

    public List<Compra> filtrarCompra(String producto, String ordenTrabajo, String estatus, String proveedor, String desde, String hasta, boolean todo){
        switch (validarFiltroFecha(desde, hasta, todo)){
            case 1:
                //lo siguiente se valida con operadores ternarios
                return compraDAO.filtrarCompras(producto.equals("Producto") ? "" : producto,
                        ordenTrabajo.equals("Orden de trabajo") ? "" : ordenTrabajo,
                        estatus.equals("Estatus") ? "" : estatus,
                        proveedor.equals("Proveedor") ? "" : proveedor,
                        null,null,todo);
            case 2:
                return compraDAO.filtrarCompras(producto.equals("Producto") ? "" : producto,
                        ordenTrabajo.equals("Orden de trabajo") ? "" : ordenTrabajo,
                        estatus.equals("Estatus") ? "" : estatus,
                        proveedor.equals("Proveedor") ? "" : proveedor,
                        LocalDate.parse(desde),LocalDate.parse(hasta),todo);
            default:
                return null;
        }//swtich
    }//filtrar compra

    public boolean validarCompra(String ordenCompra, String condiciones, LocalDate fechaEmision, String ordenTrabajo, LocalDate fechaEntrega, String agenteProveedor, String nombreComprador, String revisadoPor, String aprobadoPor, TipoEstatus estatus, String notasGenerales, TipoCompra tipo, LocalDate fechaInicioRenta, LocalDate fechaFinRenta, int idProveedor, int idUsuario) {
        if (ordenCompra.isEmpty()) return false;
        if (condiciones.isEmpty()) return false;
        if (ordenTrabajo.isEmpty()) return false;
        if (tipo.equals(TipoCompra.COMPRA) || tipo.equals(TipoCompra.REQUISICION)) {
            if (fechaEntrega == null) return false;
        } else {//de lo contrario sería tipo renta y se evalua
            if (fechaInicioRenta == null) return false;
            if (fechaFinRenta == null) return false;
        }
        if (agenteProveedor.isEmpty()) return false;
        if (nombreComprador.isEmpty()) return false;
        if (revisadoPor.isEmpty()) return false;
        if (aprobadoPor.isEmpty()) return false;
        if (estatus == null) return false;
        if (tipo == null) return false;
        if (fechaEmision == null) return false;

        //evaluar la fecha de emision
        int diferencias = (int) ChronoUnit.DAYS.between(fechaActual, fechaEmision);
        if (diferencias > 30 || diferencias < -30) {
            int opc = JOptionPane.showConfirmDialog(null, "La fecha de emisión es muy diferente a la fecha actual, ¿Continuar de esta manera?", "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (opc == JOptionPane.NO_OPTION || opc == JOptionPane.CLOSED_OPTION) {
                return false;
            }//Si el usuario da "Si" el flujo continua normalmentegit

        }//fechaemision

        //evaluar la fecha de entrega SOLO si es tipo COMPRA o REQUISICION
        if (tipo.equals(TipoCompra.COMPRA) || tipo.equals(TipoCompra.REQUISICION)) {
            if (fechaEntrega.isBefore(fechaEmision)) {
                JOptionPane.showMessageDialog(null, "¡La fecha de entrega no puede ser menor a la de emision!","Advertencia",JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }//iff tipo trabajando
        return true;
    }//validar

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

    public void mandarAuditoria(int idRegistro, TipoAccion accion,String descripcion,int idUsuario){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"); LocalDateTime fecha =
                LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        auditoriaControlador.agregarAuditoria("COMPRAS",idRegistro,accion,fecha,descripcion,idUsuario);
    }//auditoria
}

