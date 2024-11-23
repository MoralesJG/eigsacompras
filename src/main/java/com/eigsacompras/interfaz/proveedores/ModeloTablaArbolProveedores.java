package com.eigsacompras.interfaz.proveedores;

import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import java.util.List;

public class ModeloTablaArbolProveedores extends AbstractTreeTableModel {
    private List<Proveedor> proveedores;

    public ModeloTablaArbolProveedores(List<Proveedor> proveedores) {
        super(proveedores);
        this.proveedores = proveedores;
    }


    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "No.";
            case 1: return "Proveedor";
            case 2: return "Correo electrónico";
            case 3: return "Teléfono";
            case 4: return "Ubicación";
            case 5: return "Productos";
            case 6: return "Precios";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof Proveedor) {
            Proveedor proveedor = (Proveedor) node;
            switch (column) {
                case 0: return proveedores.indexOf(proveedor) + 1;
                case 1: return proveedor.getNombre();
                case 2: return proveedor.getCorreo();
                case 3: return proveedor.getTelefono();
                case 4: return proveedor.getUbicacion();
                default: return "";
            }
        } else if (node instanceof ProductoProveedor) {
            ProductoProveedor producto = (ProductoProveedor) node;
            switch (column) {
                case 5: return producto.getProducto().getDescripcion();
                case 6: return "$"+producto.getPrecioOfrecido();
                default: return "";
            }
        }
        return null;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof List) {
            return proveedores.get(index);
        } else if (parent instanceof Proveedor) {
            return ((Proveedor) parent).getProductos().get(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof List) {
            return proveedores.size();
        } else if (parent instanceof Proveedor) {
            return ((Proveedor) parent).getProductos().size();
        }
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof List) {
            return proveedores.indexOf(child);
        } else if (parent instanceof Proveedor && child instanceof ProductoProveedor) {
            return ((Proveedor) parent).getProductos().indexOf(child);
        }
        return -1;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof ProductoProveedor;
    }
}
