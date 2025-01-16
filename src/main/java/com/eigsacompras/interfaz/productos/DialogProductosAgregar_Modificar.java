package com.eigsacompras.interfaz.productos;

import com.eigsacompras.controlador.ProductoControlador;
import javax.swing.table.DefaultTableCellRenderer;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.enums.TipoDisponibilidad;
import javax.swing.table.DefaultTableModel;
import com.eigsacompras.modelo.Producto;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class DialogProductosAgregar_Modificar extends JDialog {
    private JButton JB_Guardar,JB_Cancelar,JB_AgregarProveedor,JB_EliminarProducto;
    private JPanel contentPane,JP_DatosPrincipales,JP_TablayBotones;
    private DefaultTableModel modeloTabla;
    private JTextArea JTA_Descripcion;
    private JScrollPane scroll;
    private JLabel JTF_Titulo;
    private JTable JT_Tabla;
    private ProductoProveedor productoProveedor;
    private int idProducto;
    private int idUsuario;

    public DialogProductosAgregar_Modificar(int idProducto,int idUsuario) {
        this.idProducto=idProducto;
        this.idUsuario=idUsuario;
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxHeight = (int) (screenSize.height * 0.93);//usa el 93% de la altura total de la pantalla
        setSize(800, maxHeight);
        setLocationRelativeTo(null);//centrado
        setContentPane(contentPane);
        setModal(true);
        SwingUtilities.invokeLater(() -> contentPane.requestFocusInWindow());//elimina la seleccion al iniciar

        configuracionIncial();
        inicializarTabla();
        inicalizarEventos();
        if(idProducto!=0)
            mostrarDatosModificar();//si hay un número en idProveedor se convierte en modificar la interfaz
    }

    public void configuracionIncial(){
        //botones
        JB_AgregarProveedor.setFocusPainted(false);
        JB_AgregarProveedor.setBorderPainted(false);
        JB_AgregarProveedor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_EliminarProducto.setFocusPainted(false);
        JB_EliminarProducto.setBorderPainted(false);
        JB_EliminarProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Guardar.setFocusPainted(false);
        JB_Guardar.setBorderPainted(false);
        JB_Guardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTA_Descripcion.setLineWrap(true);
        JTA_Descripcion.setWrapStyleWord(true);
    }//configuraciones inicales

    public void inicializarTabla(){
        modeloTabla = new DefaultTableModel();
        JT_Tabla = new JTable(modeloTabla);
        //encabezado
        JT_Tabla.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD,14));
        JT_Tabla.getTableHeader().setBackground(new Color(236,240,241));
        //filas
        JT_Tabla.setShowGrid(false);//cuadriculas
        JT_Tabla.setRowHeight(25);
        JT_Tabla.setFont(new Font("Reboto Light", Font.PLAIN,12));
        JT_Tabla.setBackground(new Color(236,240,241));
        scroll = new JScrollPane(JT_Tabla);
        JP_TablayBotones.add(scroll);

        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);
        modeloTabla.addColumn("Proveedor");
        modeloTabla.addColumn("Precio unitario");
        //las filas se agregan en el metodo inicializarEventos en el JB_AgregarProveedor

        //centrar las filas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < JT_Tabla.getColumnCount(); i++) {
            JT_Tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }//tabla

    public void inicalizarEventos(){
        JB_AgregarProveedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogAgregarProveedor agregarProveedor = new DialogAgregarProveedor();
                agregarProveedor.setVisible(true);
                //agrega filas en la tabla
                if(agregarProveedor.getProveedor()!=null){
                    Object [] filas = {agregarProveedor.getProveedor(),"$"+agregarProveedor.getPrecio()};
                    modeloTabla.addRow(filas);
                }
            }//boton de agregar proveedor
        });

        JB_Cancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_Cancelar.setForeground(new Color(0,0,0,220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_Cancelar.setForeground(Color.WHITE);
            }
        });
        JB_Cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JT_Tabla.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) { //para el ToolTip

                int row = JT_Tabla.rowAtPoint(e.getPoint());
                int column = JT_Tabla.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = JT_Tabla.getValueAt(row, column);

                    if (value != null && !value.toString().trim().isEmpty()) {
                        JT_Tabla.setToolTipText(value.toString());
                    } else {
                        JT_Tabla.setToolTipText(null);
                    }
                }
            }

        });//tabla

        JB_EliminarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JT_Tabla.getSelectedRow()!=-1) {
                    modeloTabla.removeRow(JT_Tabla.getSelectedRow());
                }
            }
        });//boton eliminar producto
        JB_Guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ProductoProveedor> productoProveedores = datosTabla();
                if(idProducto==0){//si no hay id se agrega un nuevo producto
                    if(new ProductoControlador().agregarProducto(JTA_Descripcion.getText(),productoProveedores,idUsuario)){//este if para que limpie la interfaz solo cuando se agrega una Compra
                        limpiarInterfaz();//esto hace el if si todo se agrega correctamente
                    }//if compraControlador
                }else{
                    if(new ProductoControlador().actualizarProducto(JTA_Descripcion.getText(),idProducto,productoProveedores,idUsuario)){
                        dispose();//si se actualiza correctamente se cierra la ventana
                    }//if
                }//if idProveedor
            }
        });//boton guardar compra
    }//eventos

    public List<ProductoProveedor> datosTabla(){
        List<ProductoProveedor> listProductosProveedores = new ArrayList<>();
        int filas =JT_Tabla.getRowCount();
        int columnas = JT_Tabla.getColumnCount();
        for(int i=0;i<filas;i++){
            productoProveedor = new ProductoProveedor();
            productoProveedor.setIdProducto(idProducto);
            productoProveedor.setDisponibilidad(TipoDisponibilidad.DISPONIBLE);//se manda disponible por defecto
            for(int x=0;x<columnas;x++){
                switch (JT_Tabla.getColumnName(x)){
                    case "Proveedor":
                        String proveedor = String.valueOf(JT_Tabla.getValueAt(i,x));
                        productoProveedor.setIdProveedor(new DialogAgregarProveedor().getMapaProveedor().get(proveedor));//para obtener el id
                        break;
                    case "Precio unitario":
                        Double precio = Double.valueOf(String.valueOf(JT_Tabla.getValueAt(i,x)).replace("$",""));
                        productoProveedor.setPrecioOfrecido(precio);
                        break;
                }//switch
            }//for columnas
            listProductosProveedores.add(productoProveedor);
        }//for filas

        return listProductosProveedores;
    }//datos tabla

    public void mostrarDatosModificar(){
        JB_Guardar.setText("Modificar");
        JTF_Titulo.setText("Modificar producto");
        Producto producto;
        producto = new ProductoControlador().listarProductoPorId(idProducto);
        JTA_Descripcion.setText(producto.getDescripcion());
        //tabla de proveedor
        if(!producto.getProveedores().isEmpty()) {
            for (ProductoProveedor productoProveedor : producto.getProveedores()) {
                Object[] fila = {productoProveedor.getProveedor().getNombre(), "$" + productoProveedor.getPrecioOfrecido()};
                modeloTabla.addRow(fila);
            }//for
        }//if si producto no tiene proveedores
    }//mostrarDatos

    public void limpiarInterfaz(){
        JTA_Descripcion.setText("");
        modeloTabla.setRowCount(0);
    }//limpiar interfaz
}
