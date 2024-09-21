package com.eigsacompras.dao;

import com.eigsacompras.modelo.Proveedor;
import java.util.List;

public interface IProveedorDAO {
    boolean agregarProveedor(Proveedor proveedor);
    List<Proveedor> listarProveedor();
    boolean actualizarProveedor(Proveedor proveedor);
    boolean eliminarProveedor(int idProveedor);
    Proveedor buscarProveedorPorId(int idProveedor);
}
