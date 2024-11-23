package com.eigsacompras.interfaz.proveedores;

import com.eigsacompras.controlador.ProveedorControlador;
import com.eigsacompras.enums.TipoDisponibilidad;
import com.eigsacompras.modelo.ProductoProveedor;
import com.eigsacompras.modelo.Proveedor;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DialogProveedoresAgregar_Modificar extends JDialog {
    private JButton JB_Guardar,JB_Imprimir,JB_Cancelar,JB_AgregarProducto,JB_EliminarProducto;
    private JPanel contentPane,JP_DatosPrincipales,JP_TablayBotones;
    private JTextField JTF_Nombre,JTF_Correo,JTF_Telefono;
    private DefaultTableModel modeloTabla;
    private JComboBox JCB_Disponilidad;
    private JTextArea JTA_Ubicación;
    private JScrollPane scroll;
    private JTable JT_Tabla;
    private ProductoProveedor productoProveedor;
    private int idProveedor;

    public DialogProveedoresAgregar_Modificar(int idProveedor) {
        this.idProveedor=idProveedor;
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
        if(idProveedor!=0)
            mostrarDatosModificar();//si hay un número en idProveedor se convierte en modificar la interfaz

    }

    public void configuracionIncial(){
        //botones
        JB_AgregarProducto.setFocusPainted(false);
        JB_AgregarProducto.setBorderPainted(false);
        JB_AgregarProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_EliminarProducto.setFocusPainted(false);
        JB_EliminarProducto.setBorderPainted(false);
        JB_EliminarProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Guardar.setFocusPainted(false);
        JB_Guardar.setBorderPainted(false);
        JB_Guardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Imprimir.setFocusPainted(false);
        JB_Imprimir.setBorderPainted(false);
        JB_Imprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //text field's
        JTF_Nombre.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        JTF_Correo.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        JTF_Telefono.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        //JComboBox
        JCB_Disponilidad.addItem("DISPONIBLE");
        JCB_Disponilidad.addItem("NO DISPONIBLE");
        JCB_Disponilidad.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });//necesario para quitar el borde del ComboBox
        JCB_Disponilidad.setFont(new Font("Roboto Light",Font.PLAIN,13));
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
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Precio");
        //las filas se agregan en el metodo inicializarEventos en el JB_AgregarProducto

        //centrar las filas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < JT_Tabla.getColumnCount(); i++) {
            JT_Tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }//tabla

    public void inicalizarEventos(){
        JB_AgregarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogAgregarProducto agregarProducto = new DialogAgregarProducto();
                agregarProducto.setVisible(true);
                //agrega filas en la tabla
                if(agregarProducto.getDescripcion()!=null){
                    Object [] filas = {agregarProducto.getDescripcion(),"$"+agregarProducto.getPrecio()};
                    modeloTabla.addRow(filas);
                }
            }//boton de agregar producto
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
                if(idProveedor==0){//si no hay id se agrega un nuevo proveedor
                    if(new ProveedorControlador().agregarProveedor(JTF_Nombre.getText(),JTF_Correo.getText(),JTF_Telefono.getText(),JTA_Ubicación.getText(),productoProveedores)){//este if para que limpie la interfaz solo cuando se agrega una Compra
                        limpiarInterfaz();//esto hace el if si todo se agrega correctamente
                    }//if compraControlador
                }else{
                    if(new ProveedorControlador().actualizarProveedor(JTF_Nombre.getText(),JTF_Correo.getText(),JTF_Telefono.getText(),JTA_Ubicación.getText(),idProveedor,productoProveedores)){
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
            productoProveedor.setIdProveedor(idProveedor);
            if(JCB_Disponilidad.getSelectedItem().equals("DISPONIBLE")){
                productoProveedor.setDisponibilidad(TipoDisponibilidad.DISPONIBLE);
            }else{
                productoProveedor.setDisponibilidad(TipoDisponibilidad.NO_DISPONIBLE);
            }
            for(int x=0;x<columnas;x++){
                switch (JT_Tabla.getColumnName(x)){
                    case "Descripción":
                        String descripcion = String.valueOf(JT_Tabla.getValueAt(i,x));
                        productoProveedor.setIdProducto(new DialogAgregarProducto().getMapaProducto().get(descripcion));//para obtener el id
                        break;
                    case "Precio":
                        Double precio = Double.valueOf(String.valueOf(JT_Tabla.getValueAt(i,x)).replace("$",""));
                        productoProveedor.setPrecioOfrecido(precio);
                        break;
                }//switch
            }//for columnas
            listProductosProveedores.add(productoProveedor);
        }//for filas

        return listProductosProveedores;
    }

    public void mostrarDatosModificar(){
        JB_Guardar.setText("Modificar");
        Proveedor proveedor;
        proveedor = new ProveedorControlador().listarProveedorPorId(idProveedor);
        JTF_Nombre.setText(proveedor.getNombre());
        JTF_Correo.setText(proveedor.getCorreo());
        JTF_Telefono.setText(proveedor.getTelefono());
        JTA_Ubicación.setText(proveedor.getUbicacion());
        //tabla de productos
        for(ProductoProveedor producto: proveedor.getProductos()){
            if(producto.getDisponibilidad().equals(TipoDisponibilidad.DISPONIBLE)){
                JCB_Disponilidad.setSelectedItem("DISPONIBLE");
            }else{
                JCB_Disponilidad.setSelectedItem("NO DISPONIBLE");
            }
            Object [] fila = {producto.getProducto().getDescripcion(),"$"+producto.getPrecioOfrecido()};
            modeloTabla.addRow(fila);
        }//for

    }//mostrarDatos

    public void limpiarInterfaz(){
        JTF_Nombre.setText("");
        JTF_Correo.setText("");
        JTF_Telefono.setText("");
        JTA_Ubicación.setText("");
        modeloTabla.setRowCount(0);
    }//limpiar interfaz
}
