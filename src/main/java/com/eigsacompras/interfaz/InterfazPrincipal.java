package com.eigsacompras.interfaz;

import com.eigsacompras.controlador.CompraControlador;
import com.eigsacompras.modelo.Compra;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InterfazPrincipal extends JFrame{
    private JPanel Principal;
    private JButton inicioButton;
    private JButton comprasButton;
    private JButton proveedoresButton;
    private JButton productosButton;
    private JButton reportesButton;
    private JPanel JP_Menu;
    private JPanel JP_CardLayout;
    private JPanel Inicio;
    private JPanel JP_Compras;
    private JPanel JP_Inicio;
    private JPanel JP_Proveedores;
    private JPanel JP_Productos;
    private JPanel JP_Reportes;
    private JLabel JL_PendienteEntrega;
    private JLabel JL_CompraMes;
    private JLabel JL_ComprasTotales;
    private JTable JT_TablaInicio;
    private JPanel JP_Tabla;
    private JLabel JL_OrdenCompra;
    private JLabel JL_FechaEntrega;
    private JLabel JL_Message;
    private JLabel JL_User;
    private JLabel JL_Logo;
    private JButton JB_comprasAgregar;
    private JButton JB_comprasModificar;
    private JButton JB_comprasEliminar;
    private DefaultTableModel modeloTabla;
    private CompraControlador compraControlador;
    private CardLayout cardLayout;

    public InterfazPrincipal(){
        inicializarComponentes();
        barraMenu();
        //panel Inicio
        tablaInicio();
        panelResumen();
        //panel compras
        configuracionCompras();

        inicializarEventos();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        JB_comprasAgregar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_comprasAgregar.setForeground(new Color(28,33,115));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_comprasAgregar.setForeground(Color.BLACK);
            }
        });
    }//constructor

    public void panelResumen(){
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

    }

    public void inicializarComponentes(){
        //frame inicial
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

        //configuración de la tabla
        modeloTabla = new DefaultTableModel();
        JT_TablaInicio = new JTable(modeloTabla);
        //encabezados de columnas
        JT_TablaInicio.getTableHeader().setFont(new Font("Reboto Light",Font.BOLD,17));
        JT_TablaInicio.getTableHeader().setBackground(new Color(236,240,241));
        //fila de tabla
        JT_TablaInicio.setRowHeight(27);
        JT_TablaInicio.setFont(new Font("Reboto Light",Font.PLAIN,16));
        JT_TablaInicio.setBackground(new Color(236,240,241));
        JT_TablaInicio.setShowGrid(false);//cuadriculas
        JScrollPane scroll = new JScrollPane(JT_TablaInicio);//se agrega el scroll
        scroll.setSize(new Dimension(JP_Inicio.getWidth(),200));
        JP_Inicio.add(scroll);
    }

    public void tablaInicio(){

        //limpiar datos si existen
        modeloTabla.setColumnCount(0);
        modeloTabla.setRowCount(0);
        //columnas
        modeloTabla.addColumn("No.");
        modeloTabla.addColumn("Orden compra");
        modeloTabla.addColumn("Nombre proveedor");
        modeloTabla.addColumn("Fecha entrega");
        modeloTabla.addColumn("Orden trabajo");
        modeloTabla.addColumn("Estatus");

        //filas
        int contador = 1;
        compraControlador =new CompraControlador();
        List<Compra> listaCompra = compraControlador.listarCompra();
        for(Compra compra:listaCompra){
            Object [] filas = {contador,compra.getOrdenCompra(),compra.getAgenteProveedor(),compra.getFechaEntrega(),compra.getOrdenTrabajo(),compra.getEstatus()};
            modeloTabla.addRow(filas);
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
        //JL_Logo.setIcon(new ImageIcon(getClass().getResource("/imagenes/logo-eisga.png")));
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

    public void configuracionCompras(){
        JB_comprasAgregar.setFocusPainted(false);
        JB_comprasAgregar.setBorderPainted(false);
        JB_comprasAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_comprasModificar.setFocusPainted(false);
        JB_comprasEliminar.setFocusPainted(false);
    }//compras

    public static void main(String[] args) {

        // Crear la instancia de InterfazPrincipal
        InterfazPrincipal frame = new InterfazPrincipal();
        frame.setVisible(true); // Hacer visible la ventana
    }

    public void inicializarEventos(){
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

    }//eventos


}

