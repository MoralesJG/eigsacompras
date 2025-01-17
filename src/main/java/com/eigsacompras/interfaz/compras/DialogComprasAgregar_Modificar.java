package com.eigsacompras.interfaz.compras;

import com.eigsacompras.controlador.CompraControlador;
import com.eigsacompras.controlador.ProveedorControlador;
import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;
import com.eigsacompras.modelo.Proveedor;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DialogComprasAgregar_Modificar extends JDialog {
    private JTextField JTF_Total,JTF_FechaEmision,JTF_Condiciones,JTF_Agente,JTF_FechaEntrega,JTF_Comprador,JTF_Revisado,JTF_Aprovado,JTF_FechaInicioRenta,JTF_FechaFinRenta;
    private JButton JB_AgregarProducto,JB_eliminarProducto,JB_Guardar,JB_Cancelar,JB_Imprimir;
    private JPanel contentPane,JP_MenuyDatosPrincipales,JP_TablayBotones;
    private JComboBox JCB_Tipo,JCB_Proveedor,JCB_Estatus;
    private JTextField JTF_OrdenCompra,JTF_OrdenTrabajo;
    private JLabel JL_FechaInicioRenta,JL_FechaFinRenta;
    private DefaultTableModel modeloTabla;
    private JTextArea JTA_NotasGenerales;
    private JScrollPane scroll;
    private JTable JT_Tabla;
    private JLabel JTF_Titulo;
    private Map<String,Integer> proveedorMapa = new HashMap<>();
    private CompraProducto compraProductos;
    private int idCompra, idUsuario;

    public DialogComprasAgregar_Modificar(int idCompra, int idUsuario) {
        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
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
        if(idCompra!=0)
            mostrarDatosModificar();
    }

    public void configuracionIncial(){
        //botones
        JB_AgregarProducto.setFocusPainted(false);
        JB_AgregarProducto.setBorderPainted(false);
        JB_AgregarProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_eliminarProducto.setFocusPainted(false);
        JB_eliminarProducto.setBorderPainted(false);
        JB_eliminarProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Guardar.setFocusPainted(false);
        JB_Guardar.setBorderPainted(false);
        JB_Guardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Imprimir.setVisible(false);
        JB_Imprimir.setFocusPainted(false);
        JB_Imprimir.setBorderPainted(false);
        JB_Imprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //text field's y combo box's
        JTF_OrdenCompra.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_OrdenTrabajo.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_Agente.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_FechaEmision.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_FechaEntrega.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_Condiciones.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_Comprador.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_Revisado.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_Aprovado.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_FechaInicioRenta.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_FechaFinRenta.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_Total.setHorizontalAlignment(JTextField.CENTER);
        JTF_Total.setText("$0.0");
        JTA_NotasGenerales.setLineWrap(true);
        JTA_NotasGenerales.setWrapStyleWord(true);
        JCB_Tipo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });//necesario para quitar el borde del ComboBox
        JCB_Tipo.setFont(new Font("Roboto Light",Font.PLAIN,13));
        JCB_Estatus.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });//necesario para quitar el borde del ComboBox
        JCB_Estatus.setFont(new Font("Roboto Light",Font.PLAIN,13));
        JCB_Proveedor.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });//necesario para quitar el borde del ComboBox
        JCB_Proveedor.setFont(new Font("Roboto Light",Font.PLAIN,13));

        //Ocultar cuando no es tipo renta la compra
        JTF_FechaInicioRenta.setVisible(false);
        JTF_FechaFinRenta.setVisible(false);
        JL_FechaInicioRenta.setVisible(false);
        JL_FechaFinRenta.setVisible(false);

        //ayuda en el formato de fechas
        JTF_FechaEmision.setText("ej. AAAA-MM-DD");
        JTF_FechaEmision.setForeground(Color.GRAY);
        JTF_FechaEntrega.setText("ej. AAAA-MM-DD");
        JTF_FechaEntrega.setForeground(Color.GRAY);
        JTF_FechaInicioRenta.setText("ej. AAAA-MM-DD");
        JTF_FechaInicioRenta.setForeground(Color.GRAY);
        JTF_FechaFinRenta.setText("ej. AAAA-MM-DD");
        JTF_FechaFinRenta.setForeground(Color.GRAY);

        //Datos del ComboBox
        for(TipoCompra compra: TipoCompra.values()){
            JCB_Tipo.addItem(compra);
        }
        for(TipoEstatus estatus: TipoEstatus.values()){
            JCB_Estatus.addItem(estatus);
        }
        for(Proveedor proveedor: new ProveedorControlador().listarProveedor()){
            proveedorMapa.put(proveedor.getNombre(),proveedor.getIdProveedor());//para almecenar el id del proveedor y usarlo al agregar la compra
            JCB_Proveedor.addItem(proveedor.getNombre());
        }
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
        modeloTabla.addColumn("Partida");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Precio unitario");
        modeloTabla.addColumn("Total");
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
                    double total = agregarProducto.getPrecio()*agregarProducto.getCantidad();
                    Object [] filas = {null,agregarProducto.getCantidad(),agregarProducto.getDescripcion(),"$"+agregarProducto.getPrecio(),"$"+total};
                    modeloTabla.addRow(filas);
                    conteoPartidas();//actualiza la columna partida
                    totalTablaProductos();//suma el total de los products

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

        JB_eliminarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JT_Tabla.getSelectedRow()!=-1) {
                    modeloTabla.removeRow(JT_Tabla.getSelectedRow());
                    conteoPartidas();
                    totalTablaProductos();
                }
            }
        });//boton eliminar producto
        JB_Guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {//este try porque si se intenta agregar una fecha vacia da error y no se valida en el CompraControlador
                    List<CompraProducto> compraProductos = datosTabla();
                    int idProveedor = proveedorMapa.get(JCB_Proveedor.getSelectedItem());
                    if(idCompra==0) {//valida si será agregar o modificar. si da TRUE entonces será para agregar

                        if (JCB_Tipo.getSelectedItem().equals(TipoCompra.COMPRA) || JCB_Tipo.getSelectedItem().equals(TipoCompra.REQUISICION)) {

                            if (new CompraControlador().agregarCompras(JTF_OrdenCompra.getText(), JTF_Condiciones.getText(), LocalDate.parse(JTF_FechaEmision.getText()), JTF_OrdenTrabajo.getText(),
                                    LocalDate.parse(JTF_FechaEntrega.getText()), JTF_Agente.getText(), JTF_Comprador.getText(), JTF_Revisado.getText(), JTF_Aprovado.getText(),
                                    TipoEstatus.valueOf(String.valueOf(JCB_Estatus.getSelectedItem())), JTA_NotasGenerales.getText(), TipoCompra.valueOf(String.valueOf(JCB_Tipo.getSelectedItem())), null,
                                    null, idProveedor,idUsuario, compraProductos)) {//este if para que limpie la interfaz solo cuando se agrega una Compra
                                limpiarInterfaz();//esto hace el if si es true
                            }//if compraControlador

                        } else if (JCB_Tipo.getSelectedItem().equals(TipoCompra.RENTA)) {
                            if (new CompraControlador().agregarCompras(JTF_OrdenCompra.getText(), JTF_Condiciones.getText(), LocalDate.parse(JTF_FechaEmision.getText()), JTF_OrdenTrabajo.getText(),
                                    null, JTF_Agente.getText(), JTF_Comprador.getText(), JTF_Revisado.getText(), JTF_Aprovado.getText(),
                                    TipoEstatus.valueOf(String.valueOf(JCB_Estatus.getSelectedItem())), JTA_NotasGenerales.getText(), TipoCompra.valueOf(String.valueOf(JCB_Tipo.getSelectedItem())),
                                    LocalDate.parse(JTF_FechaInicioRenta.getText()), LocalDate.parse(JTF_FechaFinRenta.getText()),
                                    idProveedor, 1, compraProductos)) {//este if para que limpie la interfaz solo cuando se agrega una Compra
                                limpiarInterfaz();//esto hace el if si es true
                            }
                        }//elif
                    }else{//si da FALSE entonces se va a modificar la compra
                        JB_Guardar.setText("Modificar");
                        if(JCB_Tipo.getSelectedItem().equals(TipoCompra.COMPRA) || JCB_Tipo.getSelectedItem().equals(TipoCompra.REQUISICION)){
                            if(new CompraControlador().actualizarCompra(JTF_OrdenCompra.getText(),JTF_Condiciones.getText(),LocalDate.parse(JTF_FechaEmision.getText()),JTF_OrdenTrabajo.getText(),
                                    LocalDate.parse(JTF_FechaEntrega.getText()),JTF_Agente.getText(),JTF_Comprador.getText(),JTF_Revisado.getText(),JTF_Aprovado.getText(),
                                    TipoEstatus.valueOf(String.valueOf(JCB_Estatus.getSelectedItem())),JTA_NotasGenerales.getText(),TipoCompra.valueOf(String.valueOf(JCB_Tipo.getSelectedItem())),null,
                                    null,idProveedor,idUsuario,idCompra,compraProductos)){//este if para que limpie la interfaz solo cuando se agrega una compra
                                dispose();
                            }//if
                        }else if(JCB_Tipo.getSelectedItem().equals(TipoCompra.RENTA)){
                            if(new CompraControlador().actualizarCompra(JTF_OrdenCompra.getText(),JTF_Condiciones.getText(),LocalDate.parse(JTF_FechaEmision.getText()),JTF_OrdenTrabajo.getText(),
                                    null,JTF_Agente.getText(),JTF_Comprador.getText(),JTF_Revisado.getText(),JTF_Aprovado.getText(),
                                    TipoEstatus.valueOf(String.valueOf(JCB_Estatus.getSelectedItem())),JTA_NotasGenerales.getText(),TipoCompra.valueOf(String.valueOf(JCB_Tipo.getSelectedItem())),
                                    LocalDate.parse(JTF_FechaInicioRenta.getText()),LocalDate.parse(JTF_FechaFinRenta.getText()),
                                    idProveedor,idUsuario,idCompra,compraProductos)){//este if para que limpie la interfaz solo cuando se agrega una compra
                                dispose();
                            }//if
                        }//elif
                    }//cierre if que valida si es agregar/modificar
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Hay uno o más campos vacíos, Revíselos", "Campo vacío", JOptionPane.WARNING_MESSAGE);
                }//try
            }
        });//boton guardar compra
        JCB_Tipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JCB_Tipo.getSelectedItem().equals(TipoCompra.RENTA)){
                    JTF_FechaInicioRenta.setVisible(true);
                    JTF_FechaFinRenta.setVisible(true);
                    JL_FechaInicioRenta.setVisible(true);
                    JL_FechaFinRenta.setVisible(true);
                    //se desactiva la fecha entrega ya que no es necesaria cuando la compra es renta
                    JTF_FechaEntrega.setVisible(false);
                }else{
                    JTF_FechaInicioRenta.setVisible(false);
                    JTF_FechaFinRenta.setVisible(false);
                    JL_FechaInicioRenta.setVisible(false);
                    JL_FechaFinRenta.setVisible(false);

                    JTF_FechaEntrega.setVisible(true);
                }
            }
        });

        JTF_FechaEntrega.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(JTF_FechaEntrega.getText().equals("ej. AAAA-MM-DD")){
                    JTF_FechaEntrega.setText("");
                    JTF_FechaEntrega.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(JTF_FechaEntrega.getText().isEmpty()){
                    JTF_FechaEntrega.setText("ej. AAAA-MM-DD");
                    JTF_FechaEntrega.setForeground(Color.GRAY);
                }
            }
        });

        JTF_FechaEmision.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(JTF_FechaEmision.getText().equals("ej. AAAA-MM-DD")){
                    JTF_FechaEmision.setText("");
                    JTF_FechaEmision.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(JTF_FechaEmision.getText().isEmpty()){
                    JTF_FechaEmision.setText("ej. AAAA-MM-DD");
                    JTF_FechaEmision.setForeground(Color.GRAY);
                }
            }
        });

        JTF_FechaInicioRenta.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(JTF_FechaInicioRenta.getText().equals("ej. AAAA-MM-DD")){
                    JTF_FechaInicioRenta.setText("");
                    JTF_FechaInicioRenta.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(JTF_FechaInicioRenta.getText().isEmpty()){
                    JTF_FechaInicioRenta.setText("ej. AAAA-MM-DD");
                    JTF_FechaInicioRenta.setForeground(Color.GRAY);
                }
            }
        });

        JTF_FechaFinRenta.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(JTF_FechaFinRenta.getText().equals("ej. AAAA-MM-DD")){
                    JTF_FechaFinRenta.setText("");
                    JTF_FechaFinRenta.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(JTF_FechaFinRenta.getText().isEmpty()){
                    JTF_FechaFinRenta.setText("ej. AAAA-MM-DD");
                    JTF_FechaFinRenta.setForeground(Color.GRAY);
                }
            }
        });
    }//eventos

    public List<CompraProducto> datosTabla(){
        List<CompraProducto> listaCompraProductos = new ArrayList<>();
        int filas =JT_Tabla.getRowCount();
        int columnas = JT_Tabla.getColumnCount();
        for(int i=0;i<filas;i++){
            compraProductos = new CompraProducto();
            for(int x=0;x<columnas;x++){
                switch (JT_Tabla.getColumnName(x)){
                    case "Partida":
                        int partida = (int) JT_Tabla.getValueAt(i,x);
                        compraProductos.setPartida(partida);
                        break;
                    case "Cantidad":
                        String cantidad = String.valueOf(JT_Tabla.getValueAt(i,x));
                        compraProductos.setCantidad(cantidad);
                        break;
                    case "Descripción":
                        String descripcion = (String) JT_Tabla.getValueAt(i,x);
                        compraProductos.setIdProducto(new DialogAgregarProducto().getMapaProducto().get(descripcion));
                        break;
                    case "Precio unitario":
                        String precioConSigno = (String )JT_Tabla.getValueAt(i,x);//signo de peso $
                        double precioUnitario = Double.valueOf(precioConSigno.replace("$",""));
                        compraProductos.setPrecioUnitario(precioUnitario);
                        break;
                    case "Total":
                        String totalConSigno = (String) JT_Tabla.getValueAt(i,x);//signo de peso $
                        double total = Double.valueOf(totalConSigno.replace("$",""));
                        compraProductos.setTotal(total);
                        break;
                }//switch
            }//for columnas
            listaCompraProductos.add(compraProductos);
        }//for filas

        return listaCompraProductos;
    }

    public void conteoPartidas(){
        for(int i=0;i<JT_Tabla.getRowCount();i++){
            JT_Tabla.setValueAt(i+1,i,0);
        }
    }//conteo

    public void totalTablaProductos(){
        Double total = 0.0;
        for(int i=0; i<JT_Tabla.getRowCount();i++){
            total=total+Double.valueOf(String.valueOf(JT_Tabla.getValueAt(i,4)).replace("$",""));
        }
        JTF_Total.setText("$"+total);
    }//total

    public void mostrarDatosModificar(){
        JB_Guardar.setText("Modificar");
        JTF_Titulo.setText("Modificar orden");
        Compra compra;
        compra = new CompraControlador().listarCompraPorId(idCompra);
        JTF_OrdenCompra.setText(compra.getOrdenCompra());
        JTF_OrdenTrabajo.setText(compra.getOrdenTrabajo());
        //combo box del proveedor
        Proveedor proveedor;
        proveedor = new ProveedorControlador().listarProveedorPorId(compra.getIdProveedor());
        JCB_Proveedor.setSelectedItem(proveedor.getNombre());

        JTF_Agente.setText(compra.getAgenteProveedor());
        JTF_FechaEmision.setText(String.valueOf(compra.getFechaEmision()));
        JTF_FechaEmision.setForeground(Color.BLACK);//ajustar el color
        JTF_Condiciones.setText(compra.getCondiciones());
        JTF_Comprador.setText(compra.getNombreComprador());
        JTA_NotasGenerales.setText(compra.getNotasGenerales());
        JTF_Aprovado.setText(compra.getAprobadoPor());
        JTF_Revisado.setText(compra.getRevisadoPor());
        JCB_Tipo.setSelectedItem(compra.getTipo());
        JCB_Estatus.setSelectedItem(compra.getEstatus());
        if(compra.getTipo().equals(TipoCompra.RENTA)){
            JTF_FechaInicioRenta.setText(String.valueOf(compra.getFechaInicioRenta()));
            JTF_FechaInicioRenta.setForeground(Color.BLACK);//ajustar el color
            JTF_FechaFinRenta.setText(String.valueOf(compra.getFechaFinRenta()));
            JTF_FechaFinRenta.setForeground(Color.BLACK);//ajustar el color
        }else{
            JTF_FechaEntrega.setText(String.valueOf(compra.getFechaEntrega()));
            JTF_FechaEntrega.setForeground(Color.BLACK);//ajustar el color
        }
        //tabla de productos
        int totalProductos = 0;
        for(CompraProducto producto: compra.getProductos()){
            Object [] fila = {producto.getPartida(),producto.getCantidad(),producto.getDescripcionProducto(),"$"+producto.getPrecioUnitario(),"$"+producto.getTotal()};
            modeloTabla.addRow(fila);
            totalProductos+=producto.getTotal();
        }//for
        JTF_Total.setText("$"+totalProductos);


    }//mostrarDatos

    public void limpiarInterfaz(){
        JTF_OrdenCompra.setText("");
        JTF_OrdenTrabajo.setText("");
        JCB_Proveedor.setSelectedIndex(0);
        JTF_Agente.setText("");
        JTF_FechaEmision.setText("");
        JTF_FechaEntrega.setText("");
        JTF_Condiciones.setText("");
        JTF_Comprador.setText("");
        JTA_NotasGenerales.setText("");
        JTF_Aprovado.setText("");
        JTF_Revisado.setText("");
        JCB_Tipo.setSelectedIndex(0);
        JCB_Estatus.setSelectedIndex(0);
        JTF_FechaInicioRenta.setText("");
        JTF_FechaFinRenta.setText("");
        modeloTabla.setRowCount(0);
    }//limpiar interfaz
}
