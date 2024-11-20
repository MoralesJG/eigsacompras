package com.eigsacompras.interfaz;

import com.eigsacompras.controlador.CompraControlador;
import com.eigsacompras.modelo.Compra;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JToolTip;

public class InterfazPrincipal extends JFrame{
    private JPanel Principal;
    private JButton inicioButton;
    private JButton comprasButton;
    private JButton proveedoresButton;
    private JButton productosButton;
    private JButton reportesButton;
    private JPanel JP_CardLayout;
    private JPanel JP_Compras;
    private JPanel JP_Inicio;
    private JPanel JP_Proveedores;
    private JPanel JP_Productos;
    private JPanel JP_Reportes;
    private JLabel JL_PendienteEntrega;
    private JLabel JL_CompraMes;
    private JLabel JL_ComprasTotales;
    private JTable JT_TablaInicio;
    private JLabel JL_OrdenCompra;
    private JLabel JL_FechaEntrega;
    private JLabel JL_Message;
    private JLabel JL_User;
    private JLabel JL_Logo;
    private JTable JT_TablaCompras;
    private JPanel JP_Menu;
    private JButton JB_comprasAgregar;
    private JButton JB_comprasModificar;
    private JButton JB_comprasEliminar;
    private JTextField JTF_Buscar;
    private JPanel JP_TablaInicio;
    private JPanel JP_TablaCompras;
    private CompraControlador compraControlador;
    private CardLayout cardLayout;
    private JScrollPane scroll;
    private DefaultTableModel modeloTablaInicio, modeloTablaCompras;
    private JXTreeTable tablaArbol;
    private Map<String,Integer> mapaCompras;
    private Compra compras;


    public InterfazPrincipal(){
        inicializarComponentesPrincipales();
        barraMenu();
        //panel Inicio
        inicializarComponentesInicio();
        tablaInicio();
        panelResumenInicio();
        //panel compras
        inicializarComponentesCompras();
        tablaCompras();
        inicializarEventosCompras();

        inicializarEventosPrincipales();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }//constructor

    public void inicializarComponentesPrincipales(){
        //frame
        setSize(1920,1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);//mantiene la barra de tareas
        setContentPane(Principal);

        //configuración de los cardLayouts
        cardLayout = (CardLayout) JP_CardLayout.getLayout();
        JP_CardLayout.add(JP_Inicio,"Inicio");//cards
        JP_CardLayout.add(JP_Compras,"Compras");
        JP_CardLayout.add(JP_Proveedores,"Proveedores");
        JP_CardLayout.add(JP_Productos,"Productos");
        JP_CardLayout.add(JP_Reportes,"Reportes");
        cardLayout.show(JP_CardLayout,"Inicio");//se muestra desde el arranque el panel Inicio
    }//principales

    public void panelResumenInicio(){
        JL_PendienteEntrega.setText(String.valueOf(compraControlador.listarComprasPendientes()));
        JL_CompraMes.setText(String.valueOf(compraControlador.listarComprasDelMes()));
        JL_ComprasTotales.setText(String.valueOf(compraControlador.listarComprasTotales()));

        Compra compra = compraControlador.listarProximoEntregar();
        if(compra!=null){
            JL_OrdenCompra.setText(compra.getOrdenCompra());
            //conversion de fecha
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("d MMMM, yyyy");
            JL_FechaEntrega.setText(compra.getFechaEntrega().format(formato));
        }else {
            JL_OrdenCompra.setText("Sin datos");
            JL_FechaEntrega.setText("No hay fecha");
        }//iff

    }//panelResumen

    public void inicializarComponentesInicio(){
        //configuración de la tabla
        modeloTablaInicio = new DefaultTableModel();
        JT_TablaInicio = new JTable(modeloTablaInicio);
        //encabezados de columnas
        JT_TablaInicio.getTableHeader().setFont(new Font("Reboto Light",Font.BOLD,17));
        JT_TablaInicio.getTableHeader().setBackground(new Color(236,240,241));
        //fila de tabla
        JT_TablaInicio.setRowHeight(27);
        JT_TablaInicio.setFont(new Font("Reboto Light",Font.PLAIN,16));
        JT_TablaInicio.setBackground(new Color(236,240,241));
        JT_TablaInicio.setShowGrid(false);//cuadriculas
        scroll = new JScrollPane(JT_TablaInicio);//se agrega el scroll
        //scroll.setSize(new Dimension(JP_Inicio.getWidth(),200));
        JP_Inicio.add(scroll);
    }//inicializar componentes del inicio

    public void tablaInicio(){

        //limpiar datos si existen
        modeloTablaInicio.setColumnCount(0);
        modeloTablaInicio.setRowCount(0);
        //columnas
        modeloTablaInicio.addColumn("No.");
        modeloTablaInicio.addColumn("Orden compra");
        modeloTablaInicio.addColumn("Nombre proveedor");
        modeloTablaInicio.addColumn("Fecha entrega");
        modeloTablaInicio.addColumn("Orden trabajo");
        modeloTablaInicio.addColumn("Estatus");

        //filas
        int contador = 1;
        compraControlador =new CompraControlador();
        List<Compra> listaCompra = compraControlador.listarCompra();
        for(Compra compra:listaCompra){
            Object [] filas = {contador,compra.getOrdenCompra(),compra.getAgenteProveedor(),compra.getFechaEntrega(),compra.getOrdenTrabajo(),compra.getEstatus()};
            modeloTablaInicio.addRow(filas);
            contador++;
        }

        //centrar las filas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < JT_TablaInicio.getColumnCount(); i++) {
            JT_TablaInicio.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }//tabla

    public void barraMenu(){
        //iconos
        JL_Logo.setIcon(new ImageIcon(getClass().getResource("/imagenes/logo-eisga.png")));
        JL_Message.setIcon(new ImageIcon(getClass().getResource("/imagenes/Message.png")));
        JL_Message.setBorder(new EmptyBorder(0,0,0,6));
        JL_Message.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JL_User.setIcon(new ImageIcon(getClass().getResource("/imagenes/User.png")));
        JL_User.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JL_User.setBorder(new EmptyBorder(0,6,0,17));
        //botones
        comprasButton.setFocusPainted(false);
        comprasButton.setBorder(null);
        comprasButton.setContentAreaFilled(false);
        comprasButton.setBorder(new LineBorder(new Color(255,255,255,0),10));
        comprasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        proveedoresButton.setFocusPainted(false);
        proveedoresButton.setBorder(null);
        proveedoresButton.setContentAreaFilled(false);
        proveedoresButton.setBorder(new LineBorder(new Color(255,255,255,0),10));
        proveedoresButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        productosButton.setFocusPainted(false);
        productosButton.setBorder(null);
        productosButton.setContentAreaFilled(false);
        productosButton.setBorder(new LineBorder(new Color(255,255,255,0),10));
        productosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        reportesButton.setFocusPainted(false);
        reportesButton.setBorder(null);
        reportesButton.setContentAreaFilled(false);
        reportesButton.setBorder(new LineBorder(new Color(255,255,255,0),10));
        reportesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inicioButton.setFocusPainted(false);
        inicioButton.setBorder(null);
        inicioButton.setContentAreaFilled(false);
        inicioButton.setBorder(new LineBorder(new Color(255,255,255,0),10));
        inicioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//barra menu

    public void inicializarComponentesCompras(){
        //botones
        JB_comprasAgregar.setFocusPainted(false);
        JB_comprasAgregar.setBorderPainted(false);
        JB_comprasAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_comprasModificar.setFocusPainted(false);
        JB_comprasModificar.setBorderPainted(false);
        JB_comprasModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_comprasEliminar.setFocusPainted(false);
        JB_comprasEliminar.setBorderPainted(false);
        JB_comprasEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //buscador
        JTF_Buscar.setText("Buscar en compras por OT, OC, condiciones, notas, proveedor, descripción.");
        JTF_Buscar.setForeground(Color.GRAY);
        JTF_Buscar.setBorder(new CompoundBorder(JTF_Buscar.getBorder(), new EmptyBorder(5, 15, 5, 10))); // márgenes internos

    }//compras

    public void tablaCompras(){
        JP_TablaCompras.removeAll();//elimina todo lo que haya en ese panel
        compraControlador = new CompraControlador();
        List<Compra> listaCompra = compraControlador.listarCompra();
        mapaCompras = new HashMap<>();
        ModeloTablaArbolCompras tablaArbolModelo = new ModeloTablaArbolCompras(listaCompra);//se crea un tabla árbol para mostrar los productos
        for(Compra compra:listaCompra){//para almacenar el id de la compra y usarla para el boton eliminar
            mapaCompras.put(compra.getOrdenCompra(),compra.getIdCompra());
        }//for
        tablaArbol = new JXTreeTable(tablaArbolModelo){
            @Override
            public JToolTip createToolTip() {
                JToolTip toolTip = super.createToolTip();
                toolTip.setBackground(new Color(236, 240, 241));
                toolTip.setFont(new Font("Reboto Light",Font.PLAIN,13));
                return toolTip;
            }//personalizar tool tip
        };

        tablaArbol.setRowHeight(47);
        tablaArbol.setFont(new Font("Reboto Light", Font.PLAIN, 16));
        tablaArbol.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD, 18));
        tablaArbol.getTableHeader().setBackground(new Color(236, 240, 241));
        tablaArbol.setBackground(new Color(236, 240, 241));
        tablaArbol.setLeafIcon(null);//icono de "archivo" no se muestra

        JScrollPane scrollPane = new JScrollPane(tablaArbol);
        JP_TablaCompras.add(scrollPane);
        //para actualizar la interfaz
        JP_TablaCompras.revalidate();
        JP_TablaCompras.repaint();
    }

    public void actualizarVistaTabla(List<Compra> listaFiltrada){
        ModeloTablaArbolCompras modeloTabla = new ModeloTablaArbolCompras(listaFiltrada);
        tablaArbol.setTreeTableModel(modeloTabla);
        tablaArbol.revalidate();
        tablaArbol.repaint();
    }

    public static void main(String[] args) {
        //crea la instancia de InterfazPrincipal
        InterfazPrincipal frame = new InterfazPrincipal();
        frame.setVisible(true); // Hacer visible la ventana
    }//main

    public void inicializarEventosCompras(){
        JB_comprasAgregar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                JB_comprasAgregar.setForeground(new Color(0,0,0,220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_comprasAgregar.setForeground(Color.BLACK);
            }
        });

        JB_comprasModificar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_comprasModificar.setForeground(new Color(0,0,0,220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_comprasModificar.setForeground(Color.BLACK);
            }
        });

        JB_comprasEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_comprasEliminar.setForeground(new Color(0,0,0,220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_comprasEliminar.setForeground(Color.WHITE);
            }
        });

        JTF_Buscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(JTF_Buscar.getText().equals("Buscar en compras por OT, OC, condiciones, notas, proveedor, descripción.")){
                    JTF_Buscar.setText("");
                    JTF_Buscar.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(JTF_Buscar.getText().isEmpty()){
                    JTF_Buscar.setText("Buscar en compras por OT, OC, condiciones, notas, proveedor, descripción.");
                    JTF_Buscar.setForeground(Color.GRAY);
                }
            }
        });

        JTF_Buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    JTF_Buscar.setFocusable(false);
                    JTF_Buscar.setFocusable(true);
                }
            }
        });

        tablaArbol.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                int row = tablaArbol.rowAtPoint(e.getPoint());
                int column = tablaArbol.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = tablaArbol.getValueAt(row, column);

                    if (value != null && !value.toString().trim().isEmpty()) {
                        tablaArbol.setToolTipText(value.toString());
                    } else {
                        tablaArbol.setToolTipText(null);
                    }
                }
            }

        });//tabla

        JB_comprasAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogComprasAgregar comprasAgregar = new DialogComprasAgregar();
                comprasAgregar.setVisible(true);
            }
        });//Boton de agregar

        JB_comprasEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seleccionado = tablaArbol.getSelectedRow();
                if(seleccionado!=-1){
                    String ordenCompra = tablaArbol.getStringAt(seleccionado,1);//para acceder al id en el hash map
                    compraControlador.eliminarCompra(mapaCompras.get(ordenCompra));//se elimina en la base de datos
                    //actualizar la tabla volviendola a mostrar
                    tablaCompras();
                }//if
            }
        });//boton de eliminar

        JB_comprasModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {//para cuando se selecciona un subnodo de la tabla y se mande un mensaje de adventancia
                    if(tablaArbol.getSelectedRow()!=-1){
                        String ordenCompra = tablaArbol.getStringAt(tablaArbol.getSelectedRow(),1);//para acceder al id en el hash map
                        DialogComprasModificar modificar = new DialogComprasModificar(mapaCompras.get(ordenCompra));
                        modificar.setVisible(true);
                        tablaCompras();//al cerrarse la ventana se actualiza la tabla para mostrar los cambios
                    }else{
                        JOptionPane.showMessageDialog(null, "Selecciona primero un elemento de la lista", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Selecciona únicamente elementos principales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        JTF_Buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String termino = JTF_Buscar.getText();
                List<Compra> resultados = compraControlador.buscarCompra(termino);
                actualizarVistaTabla(resultados);
            }
        });
    }//eventos compras

    public void inicializarEventosPrincipales(){
        inicioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout,"Inicio");
            }
        });//inicio

        comprasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout,"Compras");
            }
        });//compras

        proveedoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout,"Proveedores");
            }
        });//proveedores

        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout,"Productos");
            }
        });//productos

        reportesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout,"Reportes");
            }
        });//reportes

    }//eventos principales
}