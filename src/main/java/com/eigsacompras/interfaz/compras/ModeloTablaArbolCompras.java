package com.eigsacompras.interfaz.compras;

import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import java.util.List;

public class ModeloTablaArbolCompras extends AbstractTreeTableModel {
    private List<Compra> compras;

    public ModeloTablaArbolCompras(List<Compra> compras) {
        super(compras);
        this.compras = compras;
    }


    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Partida";
            case 1: return "Orden compra";
            case 2: return "Orden trabajo";
            case 3: return "Fecha entrega";
            case 4: return "Cantidad";
            case 5: return "Descripción";
            case 6: return "Precio unitario";
            case 7: return "Total";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof Compra) {
            Compra compra = (Compra) node;
            switch (column) {
                case 1: return compra.getOrdenCompra();
                case 2: return compra.getOrdenTrabajo();
                case 3: return compra.getFechaEntrega();
                case 7: return "$" + compra.getProductos().stream().mapToDouble(CompraProducto::getTotal).sum();
                default: return "";
            }
        } else if (node instanceof CompraProducto) {
            CompraProducto producto = (CompraProducto) node;
            switch (column) {
                case 0: return producto.getPartida();
                case 4: return producto.getCantidad();
                case 5: return producto.getDescripcionProducto();
                case 6: return "$" + producto.getPrecioUnitario();
                case 7: return "$" + producto.getTotal();
                default: return "";
            }
        }
        return null;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof List) {
            return compras.get(index);
        } else if (parent instanceof Compra) {
            return ((Compra) parent).getProductos().get(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof List) {
            return compras.size();
        } else if (parent instanceof Compra) {
            return ((Compra) parent).getProductos().size();
        }
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof List) {
            return compras.indexOf(child);
        } else if (parent instanceof Compra && child instanceof CompraProducto) {
            return ((Compra) parent).getProductos().indexOf(child);
        }
        return -1;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof CompraProducto;
    }
}
