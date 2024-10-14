package com.eigsacompras.dao;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompraDAO implements ICompraDAO{
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public CompraDAO(){
    }
    @Override
    public int agregarCompra(Compra compra, List<CompraProducto> compraProducto) {
        int idGenerado = -1;
        try{
            conexion = Conexion.getConexion();
            String sql = "INSERT INTO compra (orden_compra,condiciones,fecha_emision,orden_trabajo, fecha_entrega,agente_proveedor,nombre_comprador,revisado_por,aprobado_por,estatus,notas_generales,tipo,fecha_inicio_renta,fecha_fin_renta,id_proveedor,id_usuario)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, compra.getOrdenCompra());
            ps.setString(2, compra.getCondiciones());
            ps.setDate(3,java.sql.Date.valueOf(compra.getFechaEmision()));
            ps.setString(4, compra.getOrdenTrabajo());
            ps.setDate(5, java.sql.Date.valueOf(compra.getFechaEntrega()));
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
            JOptionPane.showMessageDialog(null, "Error al agregar la compra" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            String sql = "SELECT c.*, p.nombre AS nombreProveedor, cp.partida, cp.cantidad, cp.precio_unitario, cp.total, prod.descripcion AS descripcion_producto" +
                    "FROM compra c JOIN compra_producto cp ON c.id_compra = cp.id_compra" +
                    "JOIN producto prod ON cp.id_producto = prod.id_producto" +
                    "JOIN proveedor p ON c.id_proveedor = p.id_proveedor" +
                    "ORDER BY c.id_compra";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int idCompra = rs.getInt("id_compra");

                Compra compra = compraMap.get(idCompra);
                if (compra == null) {
                    compra = new Compra();
                    compra.setIdCompra(idCompra);
                    compra.setOrdenCompra(rs.getString("orden_compra"));
                    compra.setCondiciones(rs.getString("Condiciones"));
                    compra.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
                    compra.setOrdenTrabajo(rs.getString("orden_trabajo"));
                    compra.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
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
                //Agregar el producto asociado a esta compra
                CompraProducto compraProducto = new CompraProducto();
                compraProducto.setPartida(rs.getInt("partida"));
                compraProducto.setCantidad(rs.getString("cantidad"));
                compraProducto.setPrecioUnitario(rs.getDouble("precio_unitario"));
                compraProducto.setTotal(rs.getDouble("total"));
                compraProducto.setDescripcionProducto(rs.getString("descripcion_producto"));

                compra.getProductos().add(compraProducto);
            }//cierre WHILE
            listaCompras.addAll(compraMap.values());//se convierte el mapa a una lista de compras

        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar la compras" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            Conexion.cerrar(conexion, ps, rs);
        }//cierre finally
        return listaCompras;
    }//listar

    @Override
    public boolean actualizarCompra(Compra compra, List<CompraProducto> compraProducto) {
        try {
            conexion = Conexion.getConexion();
            String sql = "UPDATE compra SET orden_compra = ?, condiciones = ?, fecha_emision = ?, orden_trabajo = ?, fecha_entrega = ?, agente_proveedor = ?, nombre_comprador = ?, revisado_por = ?, aprobado_por = ?, estatus = ?, notas_generales = ?, tipo = ?, fecha_inicio_renta=?,fecha_fin_renta=?,id_proveedor=?,id_usuario=? " +
                    "WHERE id_compra = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, compra.getOrdenCompra());
            ps.setString(2, compra.getCondiciones());
            ps.setDate(3, java.sql.Date.valueOf(compra.getFechaEmision()));
            ps.setString(4, compra.getOrdenTrabajo());
            ps.setDate(5, java.sql.Date.valueOf(compra.getFechaEntrega()));
            ps.setString(6, compra.getAgenteProveedor());
            ps.setString(7, compra.getNombreComprador());
            ps.setString(8, compra.getRevisadoPor());
            ps.setString(9, compra.getAprobadoPor());
            ps.setString(10, compra.getEstatus().name());
            ps.setString(11, compra.getNotasGenerales());
            ps.setString(12, compra.getTipo().name());
            if (compra.getTipo() == TipoCompra.RENTA) {// Si tipo renta se actuliza
                ps.setDate(13, java.sql.Date.valueOf(compra.getFechaInicioRenta()));
                ps.setDate(14, java.sql.Date.valueOf(compra.getFechaFinRenta()));
            } else {//sino se manda null
                ps.setNull(13, java.sql.Types.DATE);
                ps.setNull(14, java.sql.Types.DATE);
            }
            ps.setInt(15, compra.getIdProveedor());
            ps.setInt(16, compra.getIdUsuario());
            ps.setInt(17, compra.getIdCompra());//para el where
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar la compra" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Error al eliminar la compra" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }finally {
            Conexion.cerrar(conexion, ps, null);
        }//cierre finally
    }//eliminar

    @Override
    public Compra buscarCompraPorId(int idCompra) {
        return null;
    }
}
