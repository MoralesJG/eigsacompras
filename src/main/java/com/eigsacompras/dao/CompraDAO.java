package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class CompraDAO implements ICompraDAO{
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public CompraDAO(){
    }
    @Override
    public int agregarCompra(Compra compra) {
        int idGenerado = -1;
        try{
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO compra (orden_compra,condiciones,fecha_emision,orden_trabajo, fecha_entrega,agente_proveedor,nombre_comprador,revisado_por,aprobado_por,estatus,notas_generales,tipo,fecha_inicio_renta,fecha_fin_renta,id_proveedor,id_usuario)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conexion.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);//para que pueda retornar la llave primaria generada
            ps.setString(1, compra.getOrdenCompra());
            ps.setString(2, compra.getCondiciones());
            ps.setDate(3,java.sql.Date.valueOf(compra.getFechaEmision()));
            ps.setString(4, compra.getOrdenTrabajo());
            if(compra.getTipo()==TipoCompra.COMPRA || compra.getTipo()==TipoCompra.REQUISICION){//fecha entrega no aplica en renta
                ps.setDate(5, java.sql.Date.valueOf(compra.getFechaEntrega()));
            }else{
                ps.setDate(5,null);
            }
            ps.setString(6, compra.getAgenteProveedor());
            ps.setString(7, compra.getNombreComprador());
            ps.setString(8, compra.getRevisadoPor());
            ps.setString(9, compra.getAprobadoPor());
            ps.setString(10, compra.getEstatus().name());
            ps.setString(11, compra.getNotasGenerales());
            ps.setString(12, compra.getTipo().name());
            if(compra.getTipo()==TipoCompra.RENTA){//si es de tipo renta la orden se agrega esto
                ps.setDate(13,java.sql.Date.valueOf(compra.getFechaInicioRenta()));
                ps.setDate(14,java.sql.Date.valueOf(compra.getFechaFinRenta()));
            }else{
                ps.setNull(13,java.sql.Types.DATE);
                ps.setNull(14, java.sql.Types.DATE);
            }
            ps.setInt(15, compra.getIdProveedor());
            ps.setInt(16, compra.getIdUsuario());

            ps.executeUpdate();

            rs=ps.getGeneratedKeys();//se agrega el id de la compra al ser agregado
            if(rs.next())
                idGenerado=rs.getInt(1);
            return idGenerado;//se retorna el id que lo tomará el compra controlador

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error al agregar la compra \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return idGenerado;
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }
    }//cierre de agregarCompra

    @Override
    public List<Compra> listarCompras() {
        List<Compra> listaCompras = new ArrayList<>();
        Map<Integer, Compra> compraMap = new HashMap<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT c.*, p.nombre AS nombreProveedor, cp.partida, cp.cantidad, cp.precio_unitario, cp.total, prod.descripcion AS descripcion_producto " +
                    "FROM compra c JOIN compra_producto cp ON c.id_compra = cp.id_compra " +
                    "JOIN producto prod ON cp.id_producto = prod.id_producto " +
                    "JOIN proveedor p ON c.id_proveedor = p.id_proveedor " +
                    "ORDER BY c.id_compra, cp.partida";

            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int idCompra = rs.getInt("id_compra");
                Compra compra = compraMap.get(idCompra);
                if (compra == null) {
                    compra = new Compra();
                    compra.setIdCompra(idCompra);
                    compra.setOrdenCompra(rs.getString("orden_compra"));
                    compra.setOrdenTrabajo(rs.getString("orden_trabajo"));
                    compra.setCondiciones(rs.getString("Condiciones"));
                    compra.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
                    compra.setOrdenTrabajo(rs.getString("orden_trabajo"));
                    if(rs.getString("fecha_entrega")==null){//cuando es tipo renta no hay fehca entrega
                        compra.setFechaEntrega(null);
                    }else{
                        compra.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
                    }
                    compra.setAgenteProveedor(rs.getString("agente_proveedor"));
                    compra.setNombreComprador(rs.getString("nombre_comprador"));
                    compra.setRevisadoPor(rs.getString("revisado_por"));
                    compra.setAprobadoPor(rs.getString("aprobado_por"));
                    compra.setEstatus(TipoEstatus.valueOf(rs.getString("estatus").toUpperCase()));
                    compra.setNotasGenerales(rs.getString("notas_generales"));
                    compra.setTipo(TipoCompra.valueOf(rs.getString("tipo").toUpperCase()));

                    if (rs.getString("tipo").equalsIgnoreCase("renta")) {
                        compra.setFechaInicioRenta(rs.getDate("fecha_inicio_renta").toLocalDate());
                        compra.setFechaFinRenta(rs.getDate("fecha_fin_renta").toLocalDate());
                    }
                    compra.setIdProveedor(rs.getInt("id_proveedor"));
                    compra.setIdUsuario(rs.getInt("id_usuario"));

                    compra.setProductos(new ArrayList<>());
                    compraMap.put(idCompra, compra);
                }//cierre del if
                //agregar el producto asociado a la compra
                CompraProducto compraProducto = new CompraProducto();
                compraProducto.setPartida(rs.getInt("partida"));
                compraProducto.setCantidad(rs.getString("cantidad"));
                compraProducto.setPrecioUnitario(rs.getDouble("precio_unitario"));
                compraProducto.setTotal(rs.getDouble("total"));
                compraProducto.setDescripcionProducto(rs.getString("descripcion_producto"));

                compra.getProductos().add(compraProducto);
            }//cierre WHILE
            listaCompras.addAll(compraMap.values());
            listaCompras.sort(Comparator.comparing(Compra::getOrdenCompra));//ordena la lista ya que sin esto al mostrarse en la interfaz aparece mal ordenada

        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar la compras \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
        return listaCompras;
    }//listar

    @Override
    public int listarComprasPendientes(){
        int registros = -1;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT COUNT(*) AS total FROM compra WHERE estatus = 'pendiente'";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                registros = rs.getInt("total");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar las compras pendientes \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return registros;
    }//listarPendientes

    @Override
    public int listarComprasDelMes(){
        int registros = -1;
        LocalDate fecha = LocalDate.now();
        int mesActual = fecha.getMonthValue();
        int periodoActual = fecha.getYear(); //año actual

        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT COUNT(*) AS total FROM compra WHERE MONTH(fecha_entrega)=? AND YEAR(fecha_entrega)=?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1,mesActual);
            ps.setInt(2,periodoActual);
            rs=ps.executeQuery();
            if (rs.next()){
                registros=rs.getInt("total");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar las compras del mes \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return registros;
    }//listar compras del mes

    @Override
    public int listarComprasTotales(){
        int registros = -1;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT COUNT(*) AS total FROM compra";
            ps=conexion.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                registros=rs.getInt("total");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar las compras totales \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return registros;
    }//comprasTotales

    @Override
    public Compra listarProximoEntregar(){
        Compra compra = null;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT orden_compra, fecha_entrega FROM compra " +
                    "WHERE fecha_entrega >= CURDATE() " +
                    "ORDER BY fecha_entrega ASC LIMIT 1";
            ps = conexion.prepareStatement(sql);
            rs= ps.executeQuery();
            if(rs.next()){
                compra = new Compra();
                compra.setOrdenCompra(rs.getString("orden_compra"));
                compra.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar la compra más reciente \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return compra;
    }

    @Override
    public boolean actualizarCompra(Compra compra) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE compra SET orden_compra = ?, condiciones = ?, fecha_emision = ?, orden_trabajo = ?, fecha_entrega = ?, agente_proveedor = ?, nombre_comprador = ?, revisado_por = ?, aprobado_por = ?, estatus = ?, notas_generales = ?, tipo = ?, fecha_inicio_renta=?,fecha_fin_renta=?,id_proveedor=?,id_usuario=? " +
                    "WHERE id_compra = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, compra.getOrdenCompra());
            ps.setString(2, compra.getCondiciones());
            ps.setDate(3, java.sql.Date.valueOf(compra.getFechaEmision()));
            ps.setString(4, compra.getOrdenTrabajo());
            if(compra.getTipo().equals(TipoCompra.REQUISICION) || compra.getTipo().equals(TipoCompra.COMPRA)){
                ps.setDate(5, java.sql.Date.valueOf(compra.getFechaEntrega()));
            }else{
                ps.setDate(5, null);
            }
            ps.setString(6, compra.getAgenteProveedor());
            ps.setString(7, compra.getNombreComprador());
            ps.setString(8, compra.getRevisadoPor());
            ps.setString(9, compra.getAprobadoPor());
            ps.setString(10, compra.getEstatus().name());
            ps.setString(11, compra.getNotasGenerales());
            ps.setString(12, compra.getTipo().name());
            if (compra.getTipo() == TipoCompra.RENTA) {//si es tipo renta se actuliza
                ps.setDate(13, java.sql.Date.valueOf(compra.getFechaInicioRenta()));
                ps.setDate(14, java.sql.Date.valueOf(compra.getFechaFinRenta()));
            } else {//sino se manda null
                ps.setDate(13, null);
                ps.setDate(14, null);
            }
            ps.setInt(15, compra.getIdProveedor());
            ps.setInt(16, compra.getIdUsuario());
            ps.setInt(17, compra.getIdCompra());//para el where
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar la compra \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }
    }//actualizar

    @Override
    public boolean eliminarCompra(int idCompra) {
        try {
            conexion = Conexion.getConexion();
            String sql = "DELETE FROM compra WHERE id_compra = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCompra);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la compra \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar

    @Override
    public Compra buscarCompraPorId(int idCompra) {
        Compra compra = null;
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT c.*, p.nombre AS nombreProveedor, cp.partida, cp.cantidad, cp.precio_unitario, cp.total, prod.descripcion AS descripcion_producto " +
                    "FROM compra c LEFT JOIN compra_producto cp ON c.id_compra = cp.id_compra " +
                    "LEFT JOIN producto prod ON cp.id_producto = prod.id_producto " +
                    "LEFT JOIN proveedor p ON c.id_proveedor = p.id_proveedor " +
                    "WHERE c.id_compra = ?";

            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCompra);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (compra == null) {
                    compra = new Compra();
                    compra.setIdCompra(idCompra);
                    compra.setOrdenCompra(rs.getString("orden_compra"));
                    compra.setOrdenTrabajo(rs.getString("orden_trabajo"));
                    compra.setCondiciones(rs.getString("Condiciones"));
                    compra.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
                    if (rs.getString("fecha_entrega") == null) { // Maneja si la fecha_entrega es null
                        compra.setFechaEntrega(null);
                    } else {
                        compra.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
                    }
                    compra.setAgenteProveedor(rs.getString("agente_proveedor"));
                    compra.setNombreComprador(rs.getString("nombre_comprador"));
                    compra.setRevisadoPor(rs.getString("revisado_por"));
                    compra.setAprobadoPor(rs.getString("aprobado_por"));
                    compra.setEstatus(TipoEstatus.valueOf(rs.getString("estatus").toUpperCase()));
                    compra.setNotasGenerales(rs.getString("notas_generales"));
                    compra.setTipo(TipoCompra.valueOf(rs.getString("tipo").toUpperCase()));

                    if (rs.getString("tipo").equalsIgnoreCase("renta")) {
                        compra.setFechaInicioRenta(rs.getDate("fecha_inicio_renta").toLocalDate());
                        compra.setFechaFinRenta(rs.getDate("fecha_fin_renta").toLocalDate());
                    }
                    compra.setIdProveedor(rs.getInt("id_proveedor"));
                    compra.setIdUsuario(rs.getInt("id_usuario"));

                    compra.setProductos(new ArrayList<>());
                }
                CompraProducto compraProducto = new CompraProducto();
                compraProducto.setPartida(rs.getInt("partida"));
                compraProducto.setCantidad(rs.getString("cantidad"));
                compraProducto.setPrecioUnitario(rs.getDouble("precio_unitario"));
                compraProducto.setTotal(rs.getDouble("total"));
                compraProducto.setDescripcionProducto(rs.getString("descripcion_producto"));

                compra.getProductos().add(compraProducto);
            }
            //ordena los productos por partida
            if (compra != null) {
                compra.getProductos().sort(Comparator.comparingInt(CompraProducto::getPartida));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar la compra \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }

        return compra;
    }//buscar por id

    @Override
    public List<Compra> buscarCompras(String termino) {
        List<Compra> listaCompras = new ArrayList<>();
        Map<Integer, Compra> compraMap = new HashMap<>();
        try {
            conexion = Conexion.getConexion();
            String sql = "SELECT c.*, p.nombre AS nombreProveedor, cp.partida, cp.cantidad, cp.precio_unitario, cp.total, prod.descripcion AS descripcion_producto " +
                    "FROM compra c " +
                    "JOIN compra_producto cp ON c.id_compra = cp.id_compra " +
                    "JOIN producto prod ON cp.id_producto = prod.id_producto " +
                    "JOIN proveedor p ON c.id_proveedor = p.id_proveedor " +
                    "WHERE c.orden_compra LIKE ? " +
                    "OR c.orden_trabajo LIKE ? " +
                    "OR c.condiciones LIKE ? " +
                    "OR c.notas_generales LIKE ? " +
                    "OR p.nombre LIKE ? " +
                    "OR cp.cantidad LIKE ? " +
                    "OR c.agente_proveedor LIKE ? " +
                    "OR prod.descripcion LIKE ? " +
                    "ORDER BY c.id_compra";
            ps = conexion.prepareStatement(sql);
            String busqueda = "%" + termino + "%";//busqueda parcial
            for (int i = 1; i <= 8; i++) {//busca en todas las columnas
                ps.setString(i, busqueda);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                int idCompra = rs.getInt("id_compra");
                Compra compra = compraMap.get(idCompra);
                if (compra == null) {
                    compra = new Compra();
                    compra.setIdCompra(idCompra);
                    compra.setOrdenCompra(rs.getString("orden_compra"));
                    compra.setOrdenTrabajo(rs.getString("orden_trabajo"));
                    compra.setCondiciones(rs.getString("Condiciones"));
                    compra.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
                    compra.setOrdenTrabajo(rs.getString("orden_trabajo"));
                    if(rs.getString("fecha_entrega")==null){//cuando es tipo renta no hay fehca entrega
                        compra.setFechaEntrega(null);
                    }else{
                        compra.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
                    }
                    compra.setAgenteProveedor(rs.getString("agente_proveedor"));
                    compra.setNombreComprador(rs.getString("nombre_comprador"));
                    compra.setRevisadoPor(rs.getString("revisado_por"));
                    compra.setAprobadoPor(rs.getString("aprobado_por"));
                    compra.setEstatus(TipoEstatus.valueOf(rs.getString("estatus").toUpperCase()));
                    compra.setNotasGenerales(rs.getString("notas_generales"));
                    compra.setTipo(TipoCompra.valueOf(rs.getString("tipo").toUpperCase()));

                    if (rs.getString("tipo").equalsIgnoreCase("renta")) {
                        compra.setFechaInicioRenta(rs.getDate("fecha_inicio_renta").toLocalDate());
                        compra.setFechaFinRenta(rs.getDate("fecha_fin_renta").toLocalDate());
                    }
                    compra.setIdProveedor(rs.getInt("id_proveedor"));
                    compra.setIdUsuario(rs.getInt("id_usuario"));

                    compra.setProductos(new ArrayList<>());
                    compraMap.put(idCompra, compra);
                }//cierre del if
                //agregar el producto asociado a la compra
                CompraProducto compraProducto = new CompraProducto();
                compraProducto.setPartida(rs.getInt("partida"));
                compraProducto.setCantidad(rs.getString("cantidad"));
                compraProducto.setPrecioUnitario(rs.getDouble("precio_unitario"));
                compraProducto.setTotal(rs.getDouble("total"));
                compraProducto.setDescripcionProducto(rs.getString("descripcion_producto"));

                compra.getProductos().add(compraProducto);
            }//while
            compraMap.values().forEach(c -> c.getProductos().sort(Comparator.comparingInt(CompraProducto::getPartida)));//se convierte el mapa a una lista de compras y la ordena
            listaCompras.addAll(compraMap.values());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar compras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexion.cerrar(conexion, ps, rs);
        }
        return listaCompras;
    }//buscar compras

}
