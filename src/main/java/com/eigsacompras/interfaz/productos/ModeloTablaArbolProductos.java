package com.eigsacompras.interfaz.productos;

import com.eigsacompras.controlador.ProveedorControlador;
import com.eigsacompras.modelo.Producto;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import java.util.List;

public class ModeloTablaArbolProductos extends AbstractTreeTableModel {
    private List<Producto> productos;

    public ModeloTablaArbolProductos(List<Producto> productos) {
        super(productos);
        this.productos = productos;
    }


    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "No.";
            case 1: return "Descripción";
            case 2: return "Precio unitario";
            case 3: return "Provedor(es)";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof Producto) {
            Producto producto = (Producto) node;
            switch (column) {
                case 0: return productos.indexOf(producto) + 1;
                case 1: return producto.getDescripcion();
                default: return "";
            }
        } else if (node instanceof ProductoProveedor) {
            ProductoProveedor producto = (ProductoProveedor) node;
            switch (column) {
                case 2: return "$"+producto.getPrecioOfrecido();
                case 3: return producto.getProveedor().getNombre();
                default: return "";
            }
        }
        return null;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof List) {
            return productos.get(index);
        } else if (parent instanceof Producto) {
            return ((Producto) parent).getProveedores().get(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof List) {
            return productos.size();
        } else if (parent instanceof Producto) {
            return ((Producto) parent).getProveedores().size();
        }
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof List) {
            return productos.indexOf(child);
        } else if (parent instanceof Producto && child instanceof ProductoProveedor) {
            return ((Producto) parent).getProveedores().indexOf(child);
        }
        return -1;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof ProductoProveedor;
    }
}
