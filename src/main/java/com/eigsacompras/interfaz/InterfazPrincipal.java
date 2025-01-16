package com.eigsacompras.interfaz;

import com.eigsacompras.controlador.*;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.enums.TipoAccion;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.interfaz.notificaciones.NotificacionesPopup;
import com.eigsacompras.interfaz.reportes.ModeloTablaArbolReportes;
import com.eigsacompras.interfaz.compras.*;
import com.eigsacompras.interfaz.productos.*;
import com.eigsacompras.interfaz.proveedores.*;
import com.eigsacompras.interfaz.usuarios.UsuariosPopup;
import com.eigsacompras.modelo.*;
import com.eigsacompras.utilidades.GenerarPDFAuditoria;
import com.eigsacompras.utilidades.GenerarPDFCompra;
import org.jdesktop.swingx.JXTreeTable;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JToolTip;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

public class InterfazPrincipal extends JFrame {
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
    private JLabel JL_Logo;
    private JTable JT_TablaCompras;
    private JPanel JP_Menu;
    private JButton JB_comprasAgregar;
    private JButton JB_comprasModificar;
    private JButton JB_comprasEliminar;
    private JTextField JTF_BuscarCompras;
    private JPanel JP_TablaInicio;
    private JPanel JP_TablaCompras;
    private JPanel JP_TablaProveedores;
    private JTextField JTF_BuscarProveedores;
    private JButton JB_ProveedorAgregar;
    private JButton JB_ProveedorModificar;
    private JButton JB_ProveedorEliminar;
    private JTextField JTF_BuscarProductos;
    private JButton JB_ProductoAgregar;
    private JButton JB_ProductoModificar;
    private JButton JB_ProductoEliminar;
    private JPanel JP_TablaProductos;
    private JPanel JP_TablaReportes;
    private JTextField JTF_ReporteDesde;
    private JTextField JTF_ReporteHasta;
    private JCheckBox JCB_ReporteTodo;
    private JComboBox JCB_ReporteProveedor;
    private JComboBox JCB_ReporteProducto;
    private JComboBox JCB_ReporteEstatus;
    private JComboBox JCB_ReporteOrdenTrabajo;
    private JButton JB_ReporteAplicar;
    private JButton JB_ReporteGenerar;
    private JButton JB_ReporteImprimir;
    private JButton JB_ReporteReiniciar;
    private JButton JB_Notificaciones;
    private JButton JB_Usuarios;
    private JButton auditoriaButton;
    private JPanel JP_Auditoria;
    private JTextField JTF_AuditoriaDesde;
    private JTextField JTF_AuditoriaHasta;
    private JCheckBox JCB_AuditoriaTodo;
    private JComboBox JCB_AuditoriaTablaAfectada;
    private JComboBox JCB_AuditoriaUsuario;
    private JComboBox JCB_AuditoriaTipoAccion;
    private JButton JB_AuditoriaFiltrar;
    private JButton JB_AuditoriaReiniciar;
    private JTable JT_TablaAuditoria;
    private JPanel JP_TablaAuditoria;
    private JButton JB_AuditoriaGenerar;
    private CompraControlador compraControlador;
    private CardLayout cardLayout;
    private JScrollPane scrollInicio, scrollAuditoria;
    private DefaultTableModel modeloTablaInicio, modeloTablaAuditoria;
    private JXTreeTable tablaArbolCompras, tablaArbolProveedor, tablaArbolProducto, tablaArbolReporte;
    private Map<String, Integer> mapaCompras, mapaProveedores, mapaProductos;
    private ProveedorControlador proveedorControlador;
    private ProductoControlador productoControlador;
    private int totalCompras;
    private List<Compra> filtro;
    private List<Auditoria> filtrarAuditoria;
    private NotificacionesPopup notificacionesPopup;
    private NotificacionControlador notificacionControlador;
    private UsuarioControlador usuarioControlador;
    private UsuariosPopup usuariosPopup;
    private AuditoriaControlador auditoriaControlador;
    private int idUsuario;

    public InterfazPrincipal(int idUsuario) {
        inicializarComponentesPrincipales();
        barraMenu();
        this.idUsuario = idUsuario;
        //panel Inicio
        inicializarComponentesInicio();
        tablaInicio();
        panelResumenInicio();
        //panel Compras
        inicializarComponentesCompras();
        tablaCompras();
        inicializarEventosCompras();
        //panel Proveedores
        inicializarComponentesProveedores();
        tablaProveedores();
        inicializarEventosProveedores();
        //panel Productos
        inicializarComponentesProductos();
        tablaProductos();
        inicializarEventosProductos();
        //panel Reportes
        inicializarComponentesReportes();
        tablaReportes();
        inicializarEventosReportes();
        //panel Auditorias
        inicializarComponentesAuditoria();
        tablaAuditoria();
        inicializarEventosAuditoria();

        inicializarEventosPrincipales();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        restringirAccesos();//leer dentro del metodo
        setVisible(true);
        entregasProximas();//metodo que manda advertencias sobre compras proximas a entregar o desfasadas de tiempo
    }//constructor

    public void inicializarComponentesPrincipales() {
        //frame
        setSize(960,540);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);//mantiene la barra de tareas
        setContentPane(Principal);

        //configuración de los cardLayouts
        cardLayout = (CardLayout) JP_CardLayout.getLayout();
        JP_CardLayout.add(JP_Inicio, "Inicio");//cards
        JP_CardLayout.add(JP_Compras, "Compras");
        JP_CardLayout.add(JP_Proveedores, "Proveedores");
        JP_CardLayout.add(JP_Productos, "Productos");
        JP_CardLayout.add(JP_Reportes, "Reportes");
        JP_CardLayout.add(JP_Auditoria, "Auditoria");

        cardLayout.show(JP_CardLayout, "Inicio");//se muestra desde el arranque el panel Inicio

    }//principales

    public void barraMenu() {
        //iconos
        JL_Logo.setIcon(new ImageIcon(getClass().getResource("/imagenes/LogoEigsaMenu.png")));
        JB_Notificaciones.setIcon(new ImageIcon(getClass().getResource("/imagenes/Message.png")));
        JB_Notificaciones.setFocusPainted(false);
        JB_Notificaciones.setBorder(null);
        JB_Notificaciones.setContentAreaFilled(false);
        JB_Notificaciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Usuarios.setIcon(new ImageIcon(getClass().getResource("/imagenes/User.png")));
        JB_Usuarios.setFocusPainted(false);
        JB_Usuarios.setBorder(null);
        JB_Usuarios.setContentAreaFilled(false);
        JB_Usuarios.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //botones
        comprasButton.setFocusPainted(false);
        comprasButton.setBorder(null);
        comprasButton.setContentAreaFilled(false);
        comprasButton.setBorder(new LineBorder(new Color(255, 255, 255, 0), 10));
        comprasButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        proveedoresButton.setFocusPainted(false);
        proveedoresButton.setBorder(null);
        proveedoresButton.setContentAreaFilled(false);
        proveedoresButton.setBorder(new LineBorder(new Color(255, 255, 255, 0), 10));
        proveedoresButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        productosButton.setFocusPainted(false);
        productosButton.setBorder(null);
        productosButton.setContentAreaFilled(false);
        productosButton.setBorder(new LineBorder(new Color(255, 255, 255, 0), 10));
        productosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        reportesButton.setFocusPainted(false);
        reportesButton.setBorder(null);
        reportesButton.setContentAreaFilled(false);
        reportesButton.setBorder(new LineBorder(new Color(255, 255, 255, 0), 10));
        reportesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        auditoriaButton.setFocusPainted(false);
        auditoriaButton.setBorder(null);
        auditoriaButton.setContentAreaFilled(false);
        auditoriaButton.setBorder(new LineBorder(new Color(255, 255, 255, 0), 10));
        auditoriaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inicioButton.setFocusPainted(false);
        inicioButton.setBorder(null);
        inicioButton.setContentAreaFilled(false);
        inicioButton.setBorder(new LineBorder(new Color(255, 255, 255, 0), 10));
        inicioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//barra menu

    public void inicializarEventosPrincipales() {
        inicioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout, "Inicio");
                tablaInicio();
                panelResumenInicio();
            }
        });//inicio

        comprasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout, "Compras");
                tablaCompras();
            }
        });//compras

        proveedoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout, "Proveedores");
                tablaProveedores();
            }
        });//proveedores

        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout, "Productos");
                tablaProductos();
            }
        });//productos

        reportesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout, "Reportes");
                filtro = compraControlador.listarCompra();
                tablaReportes();
            }
        });//reportes

        auditoriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_CardLayout, "Auditoria");
                filtrarAuditoria = auditoriaControlador.listarAuditoria();
                inicializarComponentesAuditoria();
                tablaAuditoria();
            }
        });//auditoria

        JB_Notificaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notificacionesPopup = new NotificacionesPopup();
                notificacionesPopup.show(JB_Notificaciones, 0, JB_Notificaciones.getHeight()+13);//para mostrar el popup de notificaciones
            }
        });//boton de notificaciones

        JB_Usuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuariosPopup =  new UsuariosPopup(idUsuario);
                usuariosPopup.show(JB_Usuarios, 0, JB_Usuarios.getHeight()+13);//para mostrar el popup de usuarios
            }
        });

    }//eventos principales

    public void entregasProximas(){
        notificacionControlador = new NotificacionControlador();
        List<Notificacion> notificaciones = notificacionControlador.listarNotificacion();
        if(!notificaciones.isEmpty()){
            LocalDate hoy = LocalDate.now();//se extrae siempre la fecha de hoy
            for (Notificacion notificacion : notificaciones) {
                long diasFaltantes = ChronoUnit.DAYS.between(hoy, notificacion.getFecha());//días faltantes entre la fecha de entrega y la fecha de hoy
                if(diasFaltantes<=3 && diasFaltantes>0){
                    JOptionPane.showMessageDialog(null,"La "+notificacion.getMensaje()+" vence en solo "+diasFaltantes+" día(s)","Información",JOptionPane.INFORMATION_MESSAGE);
                }else if(diasFaltantes==0){
                    JOptionPane.showMessageDialog(null,"La "+notificacion.getMensaje()+" vence hoy.\nMarcar como 'Entregado' si ya se encuentra en Almacén","Información",JOptionPane.INFORMATION_MESSAGE);
                } else if(diasFaltantes<0) {
                    JOptionPane.showMessageDialog(null,"La "+notificacion.getMensaje()+" venció hace "+(-diasFaltantes) +" día(s). Revisar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }//if
            }//for
        }


    }

    //PANEL INICIO

    public void inicializarComponentesInicio() {
        //configuración de la tabla
        modeloTablaInicio = new DefaultTableModel();
        JT_TablaInicio = new JTable(modeloTablaInicio);
        //encabezados de columnas
        JT_TablaInicio.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD, 17));
        JT_TablaInicio.getTableHeader().setBackground(new Color(236, 240, 241));
        //fila de tabla
        JT_TablaInicio.setRowHeight(27);
        JT_TablaInicio.setFont(new Font("Reboto Light", Font.PLAIN, 16));
        JT_TablaInicio.setBackground(new Color(236, 240, 241));
        JT_TablaInicio.setShowGrid(false);//cuadriculas
        scrollInicio = new JScrollPane(JT_TablaInicio);//se agrega el scroll
        //scroll.setSize(new Dimension(JP_Inicio.getWidth(),200));
        JP_Inicio.add(scrollInicio);
    }//inicializar componentes del inicio

    public void tablaInicio() {

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
        compraControlador = new CompraControlador();
        List<Compra> listaCompra = compraControlador.listarCompra();
        for (Compra compra : listaCompra) {
            Object[] filas = {contador, compra.getOrdenCompra(), compra.getAgenteProveedor(), compra.getFechaEntrega(), compra.getOrdenTrabajo(), compra.getEstatus()};
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

    public void panelResumenInicio() {
        JL_PendienteEntrega.setText(String.valueOf(compraControlador.listarComprasPendientes()));
        JL_CompraMes.setText(String.valueOf(compraControlador.listarComprasDelMes()));
        JL_ComprasTotales.setText(String.valueOf(compraControlador.listarComprasTotales()));

        Compra compra = compraControlador.listarProximoEntregar();
        if (compra != null) {
            JL_OrdenCompra.setText(compra.getOrdenCompra());
            //conversion de fecha
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("d MMMM, yyyy");
            JL_FechaEntrega.setText(compra.getFechaEntrega().format(formato));
        } else {
            JL_OrdenCompra.setText("Sin datos");
            JL_FechaEntrega.setText("No hay fecha");
        }//iff

    }//panelResumen

    //PANEL COMPRAS

    public void inicializarComponentesCompras() {
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
        JTF_BuscarCompras.setText("Buscar en compras por OT, OC, condiciones, notas, proveedor, descripción.");
        JTF_BuscarCompras.setForeground(Color.GRAY);
        JTF_BuscarCompras.setBorder(new CompoundBorder(JTF_BuscarCompras.getBorder(), new EmptyBorder(5, 15, 5, 10))); // márgenes internos

    }//compras

    public void tablaCompras() {
        JP_TablaCompras.removeAll();//elimina todo lo que haya en ese panel
        compraControlador = new CompraControlador();
        List<Compra> listaCompra = compraControlador.listarCompra();
        totalCompras = listaCompra.size();//para usar al filtrar
        mapaCompras = new HashMap<>();
        ModeloTablaArbolCompras tablaArbolModelo = new ModeloTablaArbolCompras(listaCompra);//se crea un tabla árbol para mostrar los productos
        for (Compra compra : listaCompra) {//para almacenar el id de la compra y usarla para el boton eliminar
            mapaCompras.put(compra.getOrdenCompra(), compra.getIdCompra());
        }//for
        tablaArbolCompras = new JXTreeTable(tablaArbolModelo) {
            @Override
            public JToolTip createToolTip() {
                JToolTip toolTip = super.createToolTip();
                toolTip.setBackground(new Color(236, 240, 241));
                toolTip.setFont(new Font("Reboto Light", Font.PLAIN, 13));
                return toolTip;
            }//personalizar tool tip
        };

        tablaArbolCompras.setRowHeight(47);
        tablaArbolCompras.setFont(new Font("Reboto Light", Font.PLAIN, 16));
        tablaArbolCompras.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD, 18));
        tablaArbolCompras.getTableHeader().setBackground(new Color(236, 240, 241));
        tablaArbolCompras.setBackground(new Color(236, 240, 241));
        tablaArbolCompras.setLeafIcon(null);//icono de "archivo" no se muestra

        JScrollPane scrollPane = new JScrollPane(tablaArbolCompras);
        JP_TablaCompras.add(scrollPane);
        //para actualizar la interfaz
        JP_TablaCompras.revalidate();
        JP_TablaCompras.repaint();
    }

    public void inicializarEventosCompras() {
        JB_comprasAgregar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                JB_comprasAgregar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_comprasAgregar.setForeground(Color.BLACK);
            }
        });

        JB_comprasModificar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_comprasModificar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_comprasModificar.setForeground(Color.BLACK);
            }
        });

        JB_comprasEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_comprasEliminar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_comprasEliminar.setForeground(Color.WHITE);
            }
        });

        JTF_BuscarCompras.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (JTF_BuscarCompras.getText().equals("Buscar en compras por OT, OC, condiciones, notas, proveedor, descripción.")) {
                    JTF_BuscarCompras.setText("");
                    JTF_BuscarCompras.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (JTF_BuscarCompras.getText().isEmpty()) {
                    JTF_BuscarCompras.setText("Buscar en compras por OT, OC, condiciones, notas, proveedor, descripción.");
                    JTF_BuscarCompras.setForeground(Color.GRAY);
                }
            }
        });

        JTF_BuscarCompras.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_BuscarCompras.setFocusable(false);
                    JTF_BuscarCompras.setFocusable(true);
                }
            }
        });

        tablaArbolCompras.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                int row = tablaArbolCompras.rowAtPoint(e.getPoint());
                int column = tablaArbolCompras.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = tablaArbolCompras.getValueAt(row, column);

                    if (value != null && !value.toString().trim().isEmpty()) {
                        tablaArbolCompras.setToolTipText(value.toString());
                    } else {
                        tablaArbolCompras.setToolTipText(null);
                    }
                }
            }

        });//tabla

        JB_comprasAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogComprasAgregar_Modificar comprasAgregar = new DialogComprasAgregar_Modificar(0,idUsuario);//se manda 0 ya que pide el idCompra en caso de que sea modificar
                comprasAgregar.setVisible(true);
                tablaCompras();//al cerrarse la ventana se actualiza la tabla para mostrar los cambios
            }
        });//Boton de agregar

        JB_comprasEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seleccionado = tablaArbolCompras.getSelectedRow();
                if (seleccionado != -1) {
                    String ordenCompra = tablaArbolCompras.getStringAt(seleccionado, 1);//para acceder al id en el hash map
                    compraControlador.eliminarCompra(mapaCompras.get(ordenCompra),idUsuario);//se elimina en la base de datos
                    //actualizar la tabla volviendola a mostrar
                    tablaCompras();
                }//if
            }
        });//boton de eliminar

        JB_comprasModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {//para cuando se selecciona un subnodo de la tabla y se mande un mensaje de adventancia
                    if (tablaArbolCompras.getSelectedRow() != -1) {
                        String ordenCompra = tablaArbolCompras.getStringAt(tablaArbolCompras.getSelectedRow(), 1);//para acceder al id en el hash map
                        DialogComprasAgregar_Modificar modificar = new DialogComprasAgregar_Modificar(mapaCompras.get(ordenCompra),idUsuario);
                        modificar.setVisible(true);
                        tablaCompras();//al cerrarse la ventana se actualiza la tabla para mostrar los cambios
                    } else {
                        JOptionPane.showMessageDialog(null, "Selecciona primero un elemento de la lista", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Selecciona únicamente elementos principales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        JTF_BuscarCompras.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String termino = JTF_BuscarCompras.getText();
                List<Compra> resultados = compraControlador.buscarCompra(termino);
                actualizarVistaTablaCompra(resultados);
            }
        });
    }//eventos compras

    public void actualizarVistaTablaCompra(List<Compra> listaFiltrada) {
        ModeloTablaArbolCompras modeloTabla = new ModeloTablaArbolCompras(listaFiltrada);
        tablaArbolCompras.setTreeTableModel(modeloTabla);
        tablaArbolCompras.revalidate();
        tablaArbolCompras.repaint();
    }//vistaTabla (llamado por el metodo inicializarEventosCompras)

    //PANEL PROVEEDORES

    public void inicializarComponentesProveedores() {
        //botones
        JB_ProveedorAgregar.setFocusPainted(false);
        JB_ProveedorAgregar.setBorderPainted(false);
        JB_ProveedorAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_ProveedorModificar.setFocusPainted(false);
        JB_ProveedorModificar.setBorderPainted(false);
        JB_ProveedorModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_ProveedorEliminar.setFocusPainted(false);
        JB_ProveedorEliminar.setBorderPainted(false);
        JB_ProveedorEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //buscador
        JTF_BuscarProveedores.setText("Buscar en proveedores por nombre, correo, ubicación, teléfono....");
        JTF_BuscarProveedores.setForeground(Color.GRAY);
        JTF_BuscarProveedores.setBorder(new CompoundBorder(JTF_BuscarProveedores.getBorder(), new EmptyBorder(5, 15, 5, 10))); // márgenes internos

    }//inicializar componentes proveedores

    public void tablaProveedores() {
        JP_TablaProveedores.removeAll();//elimina todo lo que haya en ese panel
        proveedorControlador = new ProveedorControlador();
        List<Proveedor> listaProveedor = proveedorControlador.listarProveedor();
        mapaProveedores = new HashMap<>();
        ModeloTablaArbolProveedores tablaArbolModelo = new ModeloTablaArbolProveedores(listaProveedor);//se crea un tabla árbol para mostrar los productos
        for (Proveedor proveedor : listaProveedor) {//para almacenar el id del proveedor y usarla para el boton eliminar
            mapaProveedores.put(proveedor.getNombre(), proveedor.getIdProveedor());
        }//for
        tablaArbolProveedor = new JXTreeTable(tablaArbolModelo) {
            @Override
            public JToolTip createToolTip() {
                JToolTip toolTip = super.createToolTip();
                toolTip.setBackground(new Color(236, 240, 241));
                toolTip.setFont(new Font("Reboto Light", Font.PLAIN, 13));
                return toolTip;
            }//personalizar tool tip
        };

        tablaArbolProveedor.setRowHeight(47);
        tablaArbolProveedor.setFont(new Font("Reboto Light", Font.PLAIN, 16));
        tablaArbolProveedor.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD, 18));
        tablaArbolProveedor.getTableHeader().setBackground(new Color(236, 240, 241));
        tablaArbolProveedor.setBackground(new Color(236, 240, 241));
        tablaArbolProveedor.setLeafIcon(null);//icono de "archivo" no se muestra

        JScrollPane scrollPane = new JScrollPane(tablaArbolProveedor);
        JP_TablaProveedores.add(scrollPane);
        //para actualizar la interfaz
        JP_TablaProveedores.revalidate();
        JP_TablaProveedores.repaint();
    }//tabla proveedores

    public void inicializarEventosProveedores() {
        JB_ProveedorAgregar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {//al precionar el boton
                JB_ProveedorAgregar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_ProveedorAgregar.setForeground(Color.BLACK);
            }
        });

        JB_ProveedorModificar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//al presionar el boton
                JB_ProveedorModificar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_ProveedorModificar.setForeground(Color.BLACK);
            }
        });

        JB_ProveedorEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//al presionar
                JB_ProveedorEliminar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//al dejar de presionar
                JB_ProveedorEliminar.setForeground(Color.WHITE);
            }
        });

        JTF_BuscarProveedores.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {//al dar click en buscar se quita el texto de apoyo
                if (JTF_BuscarProveedores.getText().equals("Buscar en proveedores por nombre, correo, ubicación, teléfono....")) {
                    JTF_BuscarProveedores.setText("");
                    JTF_BuscarProveedores.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {//cuando buscar está vacio se agregae este texto
                if (JTF_BuscarProveedores.getText().isEmpty()) {
                    JTF_BuscarProveedores.setText("Buscar en proveedores por nombre, correo, ubicación, teléfono....");
                    JTF_BuscarProveedores.setForeground(Color.GRAY);
                }
            }
        });

        JTF_BuscarProveedores.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//para quitar el foco en caso de que se de ESC
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_BuscarProveedores.setFocusable(false);
                    JTF_BuscarProveedores.setFocusable(true);
                }
            }
        });

        tablaArbolProveedor.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {//para el toolTip de la tabla

                int row = tablaArbolProveedor.rowAtPoint(e.getPoint());
                int column = tablaArbolProveedor.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = tablaArbolProveedor.getValueAt(row, column);

                    if (value != null && !value.toString().trim().isEmpty()) {
                        tablaArbolProveedor.setToolTipText(value.toString());
                    } else {
                        tablaArbolProveedor.setToolTipText(null);
                    }
                }
            }

        });//tabla

        JB_ProveedorAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProveedor = 0;
                DialogProveedoresAgregar_Modificar proveedoresAgregar = new DialogProveedoresAgregar_Modificar(idProveedor,idUsuario);
                proveedoresAgregar.setVisible(true);
                tablaProveedores();//actualizar la tabla después de agregar
            }
        });//Boton de agregar

        JB_ProveedorEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seleccionado = tablaArbolProveedor.getSelectedRow();
                if (seleccionado != -1) {
                    String nombreProveedor = tablaArbolProveedor.getStringAt(seleccionado, 1);//para acceder al id en el hash map
                    proveedorControlador.eliminarProveedor(mapaProveedores.get(nombreProveedor),idUsuario);//se elimina en la base de datos
                    //actualizar la tabla volviendola a mostrar
                    tablaProveedores();
                }//if
            }
        });//boton de eliminar

        JB_ProveedorModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {//para cuando se selecciona un subnodo de la tabla y se mande un mensaje de advertencia
                    if (tablaArbolProveedor.getSelectedRow() != -1) {
                        String nombreProveedor = tablaArbolProveedor.getStringAt(tablaArbolProveedor.getSelectedRow(), 1);//para acceder al id en el hash map
                        DialogProveedoresAgregar_Modificar modificar = new DialogProveedoresAgregar_Modificar(mapaProveedores.get(nombreProveedor),idUsuario);
                        modificar.setVisible(true);
                        tablaProveedores();//al cerrarse la ventana se actualiza la tabla para mostrar los cambios
                    } else {
                        JOptionPane.showMessageDialog(null, "Selecciona primero un elemento de la lista", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Selecciona únicamente elementos principales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        JTF_BuscarProveedores.addKeyListener(new KeyAdapter() {//para filtrar las busquedas
            @Override
            public void keyReleased(KeyEvent e) {
                String termino = JTF_BuscarProveedores.getText();
                List<Proveedor> resultados = proveedorControlador.buscarProveedores(termino);
                actualizarVistaTablaProveedores(resultados);
            }
        });
    }//eventos proveedores

    public void actualizarVistaTablaProveedores(List<Proveedor> listaFiltrada) {
        ModeloTablaArbolProveedores modeloTabla = new ModeloTablaArbolProveedores(listaFiltrada);
        tablaArbolProveedor.setTreeTableModel(modeloTabla);
        tablaArbolProveedor.revalidate();
        tablaArbolProveedor.repaint();
    }//vistaTabla (llamado por el metodo inicializarEventosProveedores )

    //PANEL PRODUCTOS

    public void inicializarComponentesProductos() {
        //botones
        JB_ProductoAgregar.setFocusPainted(false);
        JB_ProductoAgregar.setBorderPainted(false);
        JB_ProductoAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_ProductoModificar.setFocusPainted(false);
        JB_ProductoModificar.setBorderPainted(false);
        JB_ProductoModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_ProductoEliminar.setFocusPainted(false);
        JB_ProductoEliminar.setBorderPainted(false);
        JB_ProductoEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //buscador
        JTF_BuscarProductos.setText("Buscar en productos por descripción, nombre proveedor.");
        JTF_BuscarProductos.setForeground(Color.GRAY);
        JTF_BuscarProductos.setBorder(new CompoundBorder(JTF_BuscarProductos.getBorder(), new EmptyBorder(5, 15, 5, 10))); // márgenes internos

    }//inicializar componentes productos

    public void tablaProductos() {
        JP_TablaProductos.removeAll();//elimina todo lo que haya en ese panel
        productoControlador = new ProductoControlador();
        List<Producto> listaProducto = productoControlador.listarProducto();
        mapaProductos = new HashMap<>();
        ModeloTablaArbolProductos tablaArbolModelo = new ModeloTablaArbolProductos(listaProducto);//se crea un tabla árbol para mostrar los productos
        for (Producto producto : listaProducto) {//para almacenar el id del producto y usarla para el boton eliminar
            mapaProductos.put(producto.getDescripcion(), producto.getIdProducto());
        }//for
        tablaArbolProducto = new JXTreeTable(tablaArbolModelo) {
            @Override
            public JToolTip createToolTip() {
                JToolTip toolTip = super.createToolTip();
                toolTip.setBackground(new Color(236, 240, 241));
                toolTip.setFont(new Font("Reboto Light", Font.PLAIN, 13));
                return toolTip;
            }//personalizar tool tip
        };

        tablaArbolProducto.setRowHeight(47);
        tablaArbolProducto.setFont(new Font("Reboto Light", Font.PLAIN, 16));
        tablaArbolProducto.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD, 18));
        tablaArbolProducto.getTableHeader().setBackground(new Color(236, 240, 241));
        tablaArbolProducto.setBackground(new Color(236, 240, 241));
        tablaArbolProducto.setLeafIcon(null);//icono de "archivo" no se muestra

        JScrollPane scrollPane = new JScrollPane(tablaArbolProducto);
        JP_TablaProductos.add(scrollPane);
        //para actualizar la interfaz
        JP_TablaProductos.revalidate();
        JP_TablaProductos.repaint();
    }//tabla productos

    public void inicializarEventosProductos() {
        JB_ProductoAgregar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {//al precionar el boton
                JB_ProductoAgregar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_ProductoAgregar.setForeground(Color.BLACK);
            }
        });

        JB_ProductoModificar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//al presionar el boton
                JB_ProductoModificar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_ProductoModificar.setForeground(Color.BLACK);
            }
        });

        JB_ProductoEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//al presionar
                JB_ProductoEliminar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//al dejar de presionar
                JB_ProductoEliminar.setForeground(Color.WHITE);
            }
        });

        JTF_BuscarProductos.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {//al dar click en buscar se quita el texto de apoyo
                if (JTF_BuscarProductos.getText().equals("Buscar en productos por descripción, nombre proveedor.")) {
                    JTF_BuscarProductos.setText("");
                    JTF_BuscarProductos.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {//cuando buscar está vacio se agrega este texto
                if (JTF_BuscarProductos.getText().isEmpty()) {
                    JTF_BuscarProductos.setText("Buscar en productos por descripción, nombre proveedor.");
                    JTF_BuscarProductos.setForeground(Color.GRAY);
                }
            }
        });

        JTF_BuscarProductos.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//para quitar el foco en caso de que se de ESC
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_BuscarProductos.setFocusable(false);
                    JTF_BuscarProductos.setFocusable(true);
                }
            }
        });

        tablaArbolProducto.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {//para el toolTip de la tabla

                int row = tablaArbolProducto.rowAtPoint(e.getPoint());
                int column = tablaArbolProducto.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = tablaArbolProducto.getValueAt(row, column);

                    if (value != null && !value.toString().trim().isEmpty()) {
                        tablaArbolProducto.setToolTipText(value.toString());
                    } else {
                        tablaArbolProducto.setToolTipText(null);
                    }
                }
            }

        });//tabla

        JB_ProductoAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProducto = 0;
                DialogProductosAgregar_Modificar productoAgregar = new DialogProductosAgregar_Modificar(idProducto,idUsuario);
                productoAgregar.setVisible(true);
                tablaProductos();//actualizar la tabla después de agregar
            }
        });//Boton de agregar

        JB_ProductoEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seleccionado = tablaArbolProducto.getSelectedRow();
                if (seleccionado != -1) {
                    String nombreProducto = tablaArbolProducto.getStringAt(seleccionado, 1);//para acceder al id en el hash map
                    productoControlador.eliminarProducto(mapaProductos.get(nombreProducto),idUsuario);//se elimina en la base de datos
                    //actualizar la tabla volviendola a mostrar
                    tablaProductos();
                }//if
            }
        });//boton de eliminar

        JB_ProductoModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {//para cuando se selecciona un subnodo de la tabla y se mande un mensaje de advertencia
                    if (tablaArbolProducto.getSelectedRow() != -1) {
                        String nombreProducto = tablaArbolProducto.getStringAt(tablaArbolProducto.getSelectedRow(), 1);
                        DialogProductosAgregar_Modificar modificar = new DialogProductosAgregar_Modificar(mapaProductos.get(nombreProducto),idUsuario);//para acceder al id en el hash map
                        modificar.setVisible(true);
                        tablaProductos();//al cerrarse la ventana se actualiza la tabla para mostrar los cambios
                    } else {
                        JOptionPane.showMessageDialog(null, "Selecciona primero un elemento de la lista", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Selecciona únicamente elementos principales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        JTF_BuscarProductos.addKeyListener(new KeyAdapter() {//para filtrar las busquedas
            @Override
            public void keyReleased(KeyEvent e) {
                String termino = JTF_BuscarProductos.getText();
                List<Producto> resultados = productoControlador.buscarProductos(termino);
                actualizarVistaTablaProductos(resultados);
            }
        });
    }//eventos productos

    public void actualizarVistaTablaProductos(List<Producto> listaFiltrada) {
        ModeloTablaArbolProductos modeloTabla = new ModeloTablaArbolProductos(listaFiltrada);
        tablaArbolProducto.setTreeTableModel(modeloTabla);
        tablaArbolProducto.revalidate();
        tablaArbolProducto.repaint();
    }//vistaTabla (llamado por el metodo inicializarEventosProductos )

    //PANEL REPORTES

    public void inicializarComponentesReportes() {
        //botones
        JB_ReporteAplicar.setFocusPainted(false);
        JB_ReporteAplicar.setBorderPainted(false);
        JB_ReporteAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_ReporteGenerar.setFocusPainted(false);
        JB_ReporteGenerar.setBorderPainted(false);
        JB_ReporteGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_ReporteImprimir.setFocusPainted(false);
        JB_ReporteImprimir.setBorderPainted(false);
        JB_ReporteImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_ReporteReiniciar.setFocusPainted(false);
        JB_ReporteReiniciar.setBorderPainted(false);
        JB_ReporteReiniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Text field's
        //para el color del borde y margenes internos
        JTF_ReporteDesde.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(125, 80, 190), 2), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        JTF_ReporteDesde.setText("AAAA-MM-DD");
        JTF_ReporteDesde.setHorizontalAlignment(0);//texto centrado
        JTF_ReporteDesde.setForeground(Color.GRAY);
        JTF_ReporteHasta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(125, 80, 190), 2), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        JTF_ReporteHasta.setText("AAAA-MM-DD");
        JTF_ReporteHasta.setHorizontalAlignment(0);
        JTF_ReporteHasta.setForeground(Color.GRAY);
        //Check box
        JCB_ReporteTodo.setBorderPainted(false);
        JCB_ReporteTodo.setFocusPainted(false);
        //Combo box
        JCB_ReporteProveedor.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        JCB_ReporteProveedor.setUI(new BasicComboBoxUI() {//para eliminar el borde y enfoque
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setContentAreaFilled(false);
                return arrowButton;
            }
        });
        JCB_ReporteProveedor.setBorder(BorderFactory.createLineBorder(Color.GRAY));//para darle un color al borde
        JCB_ReporteProveedor.setPreferredSize(new Dimension(JCB_ReporteProveedor.getWidth(), 25));//para el tamaño

        JCB_ReporteEstatus.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        JCB_ReporteEstatus.setUI(new BasicComboBoxUI() {//para eliminar el borde
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setContentAreaFilled(false);
                return arrowButton;
            }
        });
        JCB_ReporteEstatus.setBorder(BorderFactory.createLineBorder(Color.GRAY));//para darle un color al borde
        JCB_ReporteEstatus.setPreferredSize(new Dimension(JCB_ReporteEstatus.getWidth(), 25));//para el tamaño

        JCB_ReporteProducto.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        JCB_ReporteProducto.setUI(new BasicComboBoxUI() {//para eliminar el borde
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setContentAreaFilled(false);
                return arrowButton;
            }
        });
        JCB_ReporteProducto.setBorder(BorderFactory.createLineBorder(Color.GRAY));//para darle un color al borde
        JCB_ReporteProducto.setPreferredSize(new Dimension(JCB_ReporteProducto.getWidth(), 25));//para el tamaño

        JCB_ReporteOrdenTrabajo.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        JCB_ReporteOrdenTrabajo.setUI(new BasicComboBoxUI() {//para eliminar el borde
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setContentAreaFilled(false);
                return arrowButton;
            }
        });
        JCB_ReporteOrdenTrabajo.setBorder(BorderFactory.createLineBorder(Color.GRAY));//para darle un color al borde
        JCB_ReporteOrdenTrabajo.setPreferredSize(new Dimension(JCB_ReporteOrdenTrabajo.getWidth(), 25));//para el tamaño

        //datos de los combo box
        JCB_ReporteProveedor.addItem("Proveedor");
        for (Proveedor proveedor : new ProveedorControlador().listarProveedor()) {
            JCB_ReporteProveedor.addItem(proveedor.getNombre());
        }//for proveedor
        JCB_ReporteProducto.addItem("Producto");
        for (Producto producto : new ProductoControlador().listarProducto()) {
            JCB_ReporteProducto.addItem(producto.getDescripcion());
        }//for producto
        JCB_ReporteOrdenTrabajo.addItem("Orden de trabajo");
        for (Compra compra : new CompraControlador().listarCompra()) {
            JCB_ReporteOrdenTrabajo.addItem(compra.getOrdenTrabajo());
        }//for compra
        JCB_ReporteEstatus.addItem("Estatus");
        for (TipoEstatus tipoEstatus : TipoEstatus.values()) {
            JCB_ReporteEstatus.addItem(tipoEstatus.name());
        }//for estatus

        filtro = new CompraControlador().listarCompra();//para el filtro en reporte

    }//inicializar componentes reportes

    public void tablaReportes() {
        JP_TablaReportes.removeAll();//elimina todo lo que haya en ese panel
        compraControlador = new CompraControlador();
        List<Compra> listaCompra = compraControlador.listarCompra();
        ModeloTablaArbolReportes tablaArbolModelo = new ModeloTablaArbolReportes(listaCompra);//se crea un tabla árbol para mostrar los productos
        tablaArbolReporte = new JXTreeTable(tablaArbolModelo) {
            @Override
            public JToolTip createToolTip() {
                JToolTip toolTip = super.createToolTip();
                toolTip.setBackground(new Color(236, 240, 241));
                toolTip.setFont(new Font("Reboto Light", Font.PLAIN, 13));
                return toolTip;
            }//personalizar tool tip
        };

        tablaArbolReporte.setRowHeight(47);
        tablaArbolReporte.setFont(new Font("Reboto Light", Font.PLAIN, 16));
        tablaArbolReporte.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD, 18));
        tablaArbolReporte.getTableHeader().setBackground(new Color(236, 240, 241));
        tablaArbolReporte.setBackground(new Color(236, 240, 241));
        tablaArbolReporte.setLeafIcon(null);//icono de "archivo" no se muestra

        JScrollPane scrollPane = new JScrollPane(tablaArbolReporte);
        JP_TablaReportes.add(scrollPane);
        //para actualizar la interfaz
        JP_TablaReportes.revalidate();
        JP_TablaReportes.repaint();

        //"reiniciar" los combobox y text fields
        JCB_ReporteProducto.setSelectedIndex(0);
        JCB_ReporteProveedor.setSelectedIndex(0);
        JCB_ReporteOrdenTrabajo.setSelectedIndex(0);
        JCB_ReporteEstatus.setSelectedIndex(0);
        JCB_ReporteTodo.setSelected(false);
        JTF_ReporteDesde.setText("AAAA-MM-DD");
        JTF_ReporteDesde.setForeground(Color.GRAY);
        JTF_ReporteHasta.setText("AAAA-MM-DD");
        JTF_ReporteHasta.setForeground(Color.GRAY);
    }//tabla productos

    public void inicializarEventosReportes() {
        JB_ReporteAplicar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {//al precionar el boton
                JB_ReporteAplicar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_ReporteAplicar.setForeground(Color.BLACK);
            }
        });

        JB_ReporteGenerar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//al presionar el boton
                JB_ReporteGenerar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_ReporteGenerar.setForeground(Color.BLACK);
            }
        });

        JB_ReporteImprimir.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//al presionar
                JB_ReporteImprimir.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//al dejar de presionar
                JB_ReporteImprimir.setForeground(Color.BLACK);
            }
        });

        JTF_ReporteDesde.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {//al dar click en buscar se quita el texto de apoyo
                if (JTF_ReporteDesde.getText().equals("AAAA-MM-DD")) {
                    JTF_ReporteDesde.setText("");
                    JTF_ReporteDesde.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {//cuando buscar está vacio se agrege este texto
                if (JTF_ReporteDesde.getText().isEmpty()) {
                    JTF_ReporteDesde.setText("AAAA-MM-DD");
                    JTF_ReporteDesde.setForeground(Color.GRAY);
                }
            }
        });

        JTF_ReporteDesde.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//para quitar el foco en caso de que se de ESC
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_ReporteDesde.setFocusable(false);
                    JTF_ReporteDesde.setFocusable(true);
                }
            }
        });

        JTF_ReporteHasta.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {//al dar click en buscar se quita el texto de apoyo
                if (JTF_ReporteHasta.getText().equals("AAAA-MM-DD")) {
                    JTF_ReporteHasta.setText("");
                    JTF_ReporteHasta.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {//cuando buscar está vacio se agregae este texto
                if (JTF_ReporteHasta.getText().isEmpty()) {
                    JTF_ReporteHasta.setText("AAAA-MM-DD");
                    JTF_ReporteHasta.setForeground(Color.GRAY);
                }
            }
        });

        JTF_ReporteHasta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//para quitar el foco en caso de que se de ESC
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_ReporteHasta.setFocusable(false);
                    JTF_ReporteHasta.setFocusable(true);
                }
            }
        });

        tablaArbolReporte.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {//para el toolTip de la tabla

                int row = tablaArbolReporte.rowAtPoint(e.getPoint());
                int column = tablaArbolReporte.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = tablaArbolReporte.getValueAt(row, column);

                    if (value != null && !value.toString().trim().isEmpty()) {
                        tablaArbolReporte.setToolTipText(value.toString());
                    } else {
                        tablaArbolReporte.setToolTipText(null);
                    }
                }
            }

        });//tabla

        JB_ReporteAplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compraControlador = new CompraControlador();
                filtro = compraControlador.filtrarCompra((String) JCB_ReporteProducto.getSelectedItem(), (String) JCB_ReporteOrdenTrabajo.getSelectedItem(),
                        (String) JCB_ReporteEstatus.getSelectedItem(), (String) JCB_ReporteProveedor.getSelectedItem(),JTF_ReporteDesde.getText(),
                        JTF_ReporteHasta.getText(), JCB_ReporteTodo.isSelected());
                if (!filtro.isEmpty() && totalCompras!=filtro.size()){//si hay datos en la lista activa los botones y acutaliza la tabla
                    JB_ReporteGenerar.setEnabled(true);
                    JB_ReporteGenerar.setBackground(new Color(52,152,219));
                    JB_ReporteImprimir.setEnabled(true);
                    JB_ReporteImprimir.setBackground(new Color(52,152,219));
                    actualizarVistaTablaReportes(filtro);
                }else{//si no hay datos en la tabla se borran los datos de la tabla y se desactivan los botones
                    actualizarVistaTablaReportes(null);
                    JB_ReporteGenerar.setEnabled(false);
                    JB_ReporteGenerar.setBackground(new Color(199,200,202));
                    JB_ReporteImprimir.setEnabled(false);
                    JB_ReporteImprimir.setBackground(new Color(199,200,202));
                    filtro = compraControlador.listarCompra();
                    JOptionPane.showMessageDialog(null,"No hay compras que coincidan","Sin coincidencias",JOptionPane.INFORMATION_MESSAGE);

                }//if
            }
        });
        JB_ReporteReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablaReportes();//se mandan todos los datos a la tabla y se activan los botones
                JB_ReporteGenerar.setEnabled(true);
                JB_ReporteGenerar.setBackground(new Color(52,152,219));
                JB_ReporteImprimir.setEnabled(true);
                JB_ReporteImprimir.setBackground(new Color(52,152,219));
            }
        });

        JB_ReporteGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = System.getProperty("user.home") + "\\Documents\\ReporteCompras.pdf";//se guardará el reporte en Documentos
                new GenerarPDFCompra().generarReporte(filtro, path);
            }
        });
        JB_ReporteImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimir(filtro);//se le pasa la misma lista que se le pasa a 'Generar'
            }
        });

    }//eventos reportes

    public void actualizarVistaTablaReportes(List<Compra> filtro) {
        ModeloTablaArbolReportes modeloTabla = new ModeloTablaArbolReportes(filtro);
        tablaArbolReporte.setTreeTableModel(modeloTabla);
        tablaArbolReporte.revalidate();
        tablaArbolReporte.repaint();
    }//vistaTabla (llamado por el metodo inicializarEventosReportes )

    public void imprimir(List<Compra> compras) {
        File archivoTemp = null;

        try {
            //archivo temporal
            archivoTemp = File.createTempFile("Reporte", ".pdf");
            String path = archivoTemp.getAbsolutePath();//el path que se pasa es a los archivos temporales del sistema

            new GenerarPDFCompra().generarReporte(compras,path);//se genera el pdf que se va a imprimir
            PDDocument document = PDDocument.load(new File(path));

            //lo siguiente es para imprimir
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            if (job.printDialog()) {//aquí se muestra el recuadro que permite imprimir
                job.print();
            } else {
                JOptionPane.showMessageDialog(null, "Impresión cancelada por el usuario.", "Impresión cancelada", JOptionPane.INFORMATION_MESSAGE);
            }

            document.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {//el finally para eliminar el archivo temporal si existe
            if (archivoTemp != null && archivoTemp.exists()) {
                archivoTemp.deleteOnExit();
            }
        }
    }//imprimir

    //PANEL AUDITORIA

    public void inicializarComponentesAuditoria() {
        //tabla
        //configuración de la tabla
        modeloTablaAuditoria = new DefaultTableModel();
        JT_TablaAuditoria = new JTable(modeloTablaAuditoria) {
            @Override
            public JToolTip createToolTip() {
                JToolTip toolTip = super.createToolTip();
                toolTip.setBackground(new Color(236, 240, 241));
                toolTip.setFont(new Font("Reboto Light", Font.PLAIN, 13));
                return toolTip; }
        };//para el tool tip
        //encabezados de columnas
        JT_TablaAuditoria.getTableHeader().setFont(new Font("Reboto Light", Font.BOLD, 17));
        JT_TablaAuditoria.getTableHeader().setBackground(new Color(236, 240, 241));
        //fila de tabla
        JT_TablaAuditoria.setRowHeight(27);
        JT_TablaAuditoria.setFont(new Font("Reboto Light", Font.PLAIN, 16));
        JT_TablaAuditoria.setBackground(new Color(236, 240, 241));
        JT_TablaAuditoria.setShowGrid(false);//cuadriculas
        scrollAuditoria = new JScrollPane(JT_TablaAuditoria);//se agrega el scroll
        //scroll.setSize(new Dimension(JP_Inicio.getWidth(),200));
        JP_Auditoria.add(scrollAuditoria);

        //botones
        JB_AuditoriaFiltrar.setFocusPainted(false);
        JB_AuditoriaFiltrar.setBorderPainted(false);
        JB_AuditoriaFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_AuditoriaReiniciar.setFocusPainted(false);
        JB_AuditoriaReiniciar.setBorderPainted(false);
        JB_AuditoriaReiniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Text field's
        //para el color del borde y margenes internos
        JTF_AuditoriaDesde.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(125, 80, 190), 2), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        JTF_AuditoriaDesde.setText("AAAA-MM-DD");
        JTF_AuditoriaDesde.setHorizontalAlignment(0);//texto centrado
        JTF_AuditoriaDesde.setForeground(Color.GRAY);
        JTF_AuditoriaHasta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(125, 80, 190), 2), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        JTF_AuditoriaHasta.setText("AAAA-MM-DD");
        JTF_AuditoriaHasta.setHorizontalAlignment(0);
        JTF_AuditoriaHasta.setForeground(Color.GRAY);
        //Check box
        JCB_AuditoriaTodo.setBorderPainted(false);
        JCB_AuditoriaTodo.setFocusPainted(false);
        //Combo box
        JCB_AuditoriaTablaAfectada.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        JCB_AuditoriaTablaAfectada.setUI(new BasicComboBoxUI() {//para eliminar el borde y enfoque
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setContentAreaFilled(false);
                return arrowButton;
            }
        });
        JCB_AuditoriaTablaAfectada.setBorder(BorderFactory.createLineBorder(Color.GRAY));//para darle un color al borde
        JCB_AuditoriaTablaAfectada.setPreferredSize(new Dimension(JCB_AuditoriaTablaAfectada.getWidth(), 25));//para el tamaño

        JCB_AuditoriaUsuario.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        JCB_AuditoriaUsuario.setUI(new BasicComboBoxUI() {//para eliminar el borde
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setContentAreaFilled(false);
                return arrowButton;
            }
        });
        JCB_AuditoriaUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY));//para darle un color al borde
        JCB_AuditoriaUsuario.setPreferredSize(new Dimension(JCB_AuditoriaUsuario.getWidth(), 25));//para el tamaño

        JCB_AuditoriaTipoAccion.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        JCB_AuditoriaTipoAccion.setUI(new BasicComboBoxUI() {//para eliminar el borde
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = super.createArrowButton();
                arrowButton.setBorder(BorderFactory.createEmptyBorder());
                arrowButton.setContentAreaFilled(false);
                return arrowButton;
            }
        });
        JCB_AuditoriaTipoAccion.setBorder(BorderFactory.createLineBorder(Color.GRAY));//para darle un color al borde
        JCB_AuditoriaTipoAccion.setPreferredSize(new Dimension(JCB_AuditoriaTipoAccion.getWidth(), 25));//para el tamaño

        //datos de los combo box
        JCB_AuditoriaTablaAfectada.addItem("Tabla afectada");
        JCB_AuditoriaTablaAfectada.addItem("Compras");
        JCB_AuditoriaTablaAfectada.addItem("Proveedores");
        JCB_AuditoriaTablaAfectada.addItem("Productos");
        JCB_AuditoriaTablaAfectada.addItem("Usuarios");

        JCB_AuditoriaTipoAccion.addItem("Accion");//pendiente terminar tipo de accionn
        for (TipoAccion tipoAccion : TipoAccion.values()) {
            JCB_AuditoriaTipoAccion.addItem(tipoAccion.name());
        }//for tipo de accion
        JCB_AuditoriaUsuario.addItem("Usuario");
        for (Usuario usuario : new UsuarioControlador().listarUsuario()) {
            JCB_AuditoriaUsuario.addItem(usuario.getNombre());
        }//for usuario

        auditoriaControlador = new AuditoriaControlador();
        filtrarAuditoria = auditoriaControlador.listarAuditoria();//para el filtro de auditoria
    }//inicializar componentes auditoria

    public void tablaAuditoria() {
        //limpiar datos si existen
        modeloTablaAuditoria.setColumnCount(0);
        modeloTablaAuditoria.setRowCount(0);
        //columnas
        modeloTablaAuditoria.addColumn("Fecha");
        modeloTablaAuditoria.addColumn("Usuario");
        modeloTablaAuditoria.addColumn("Accion");
        modeloTablaAuditoria.addColumn("Tabla afectada");
        modeloTablaAuditoria.addColumn("Regitro afectado");
        modeloTablaAuditoria.addColumn("Descripción");

        //filas
        auditoriaControlador = new AuditoriaControlador();
        List<Auditoria> listaAuditoria = auditoriaControlador.listarAuditoria();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
        for (Auditoria auditoria : listaAuditoria) {
            String nombreUsuario = new UsuarioControlador().buscarUsuarioPorId(auditoria.getIdUsuario()).getNombre();
            Object[] filas = {auditoria.getFechaCambio().format(formatter),nombreUsuario,auditoria.getAccion(),auditoria.getTablaAfectada(),auditoria.getIdRegistroAfectado(),auditoria.getDescripcion()};
            modeloTablaAuditoria.addRow(filas);
        }

        //centrar las filas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < JT_TablaAuditoria.getColumnCount(); i++) {
            JT_TablaAuditoria.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }//tabla auditoria

    public void inicializarEventosAuditoria() {
        JB_AuditoriaFiltrar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {//al precionar el boton
                JB_AuditoriaFiltrar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_AuditoriaFiltrar.setForeground(Color.BLACK);
            }
        });

        JB_AuditoriaReiniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//al presionar el boton
                JB_AuditoriaReiniciar.setForeground(new Color(0, 0, 0, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {//cuando se deja de presionar el boton
                JB_AuditoriaReiniciar.setForeground(Color.BLACK);
            }
        });


        JTF_AuditoriaDesde.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {//al dar click en buscar se quita el texto de apoyo
                if (JTF_AuditoriaDesde.getText().equals("AAAA-MM-DD")) {
                    JTF_AuditoriaDesde.setText("");
                    JTF_AuditoriaDesde.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {//cuando buscar está vacio se agrege este texto
                if (JTF_AuditoriaDesde.getText().isEmpty()) {
                    JTF_AuditoriaDesde.setText("AAAA-MM-DD");
                    JTF_AuditoriaDesde.setForeground(Color.GRAY);
                }
            }
        });

        JTF_AuditoriaDesde.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//para quitar el foco en caso de que se de ESC
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_AuditoriaDesde.setFocusable(false);
                    JTF_AuditoriaDesde.setFocusable(true);
                }
            }
        });

        JTF_AuditoriaHasta.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {//al dar click en buscar se quita el texto de apoyo
                if (JTF_AuditoriaHasta.getText().equals("AAAA-MM-DD")) {
                    JTF_AuditoriaHasta.setText("");
                    JTF_AuditoriaHasta.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {//cuando buscar está vacio se agregae este texto
                if (JTF_AuditoriaHasta.getText().isEmpty()) {
                    JTF_AuditoriaHasta.setText("AAAA-MM-DD");
                    JTF_AuditoriaHasta.setForeground(Color.GRAY);
                }
            }
        });

        JTF_AuditoriaHasta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//para quitar el foco en caso de que se de ESC
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_AuditoriaHasta.setFocusable(false);
                    JTF_AuditoriaHasta.setFocusable(true);
                }
            }
        });

        JT_TablaAuditoria.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {//para el toolTip de la tabla

                int row = JT_TablaAuditoria.rowAtPoint(e.getPoint());
                int column = JT_TablaAuditoria.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = JT_TablaAuditoria.getValueAt(row, column);

                    if (value != null && !value.toString().trim().isEmpty()) {
                        JT_TablaAuditoria.setToolTipText(value.toString());
                    } else {
                        JT_TablaAuditoria.setToolTipText(null);
                    }
                }
            }

        });//tool tip

        JB_AuditoriaFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auditoriaControlador = new AuditoriaControlador();
                filtrarAuditoria = auditoriaControlador.filtrarAuditoria((String)JCB_AuditoriaTablaAfectada.getSelectedItem(),(String)JCB_AuditoriaUsuario.getSelectedItem(),
                        (String)JCB_AuditoriaTipoAccion.getSelectedItem(),JTF_AuditoriaDesde.getText(),JTF_AuditoriaHasta.getText(),JCB_AuditoriaTodo.isSelected());
                if (!filtrarAuditoria.isEmpty()){//si hay datos en la lista activa, los botones y acutaliza la tabla
                    JB_AuditoriaGenerar.setEnabled(true);
                    JB_AuditoriaGenerar.setBackground(new Color(52,152,219));
                    actualizarVistaTablaAuditoria(filtrarAuditoria);
                }else{//si no hay datos en la tabla se borran los datos de la tabla y se desactivan los botones
                    actualizarVistaTablaAuditoria(null);
                    JB_AuditoriaGenerar.setEnabled(false);
                    JB_AuditoriaGenerar.setBackground(new Color(199,200,202));
                    filtro = compraControlador.listarCompra();
                    JOptionPane.showMessageDialog(null,"No hay auditorias que coincidan con los filtros ingresados","Sin coincidencias",JOptionPane.INFORMATION_MESSAGE);

                }//if
            }
        });
        JB_AuditoriaReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //se reinicia la tabla, se activa el boton y se reinician los combobox
                tablaAuditoria();
                JB_AuditoriaGenerar.setEnabled(true);
                JB_AuditoriaGenerar.setBackground(new Color(52,152,219));
                JCB_AuditoriaTablaAfectada.setSelectedIndex(0);
                JCB_AuditoriaTipoAccion.setSelectedIndex(0);
                JCB_AuditoriaUsuario.setSelectedIndex(0);
            }
        });

        JB_AuditoriaGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = System.getProperty("user.home") + "\\Documents\\Auditorias.pdf";//se guardará el reporte en Documentos
                new GenerarPDFAuditoria().generarReporte(filtrarAuditoria, path);
            }
        });

    }//eventos auditorias

    public void actualizarVistaTablaAuditoria(List<Auditoria> filtro) {
        if(filtro == null){
            modeloTablaAuditoria.setRowCount(0);//se limpia la tabla
        }else{
            modeloTablaAuditoria.setRowCount(0);
            for (Auditoria auditoria : filtro) {
                String nombreUsuario = new UsuarioControlador().buscarUsuarioPorId(auditoria.getIdUsuario()).getNombre();
                Object[] filas = {auditoria.getFechaCambio(),nombreUsuario,auditoria.getAccion(),auditoria.getTablaAfectada(),auditoria.getIdRegistroAfectado(),auditoria.getDescripcion()};
                modeloTablaAuditoria.addRow(filas);
            }
        }//if - else
    }//vistaTabla (llamado por el metodo inicializarEventosAudtoria)


    
    public void restringirAccesos(){
        //este metodo restringe los accesos cuando el usuario es de tipo empleado, se hace a lo último ya que es después de inicializar
        //todo y así tener mejor control sobre los componentes e igual antes de mostrar la pantalla para que una vez se muestre ya esté
        //configurado correctamente de lo contrario si el usuario es administrador todo esto se ignora y el sistema corre normalmemte
        usuarioControlador = new UsuarioControlador();
        Usuario usuario = usuarioControlador.buscarUsuarioPorId(idUsuario);
        if(usuario.getTipo().equals(TipoAcceso.EMPLEADO)){
            //panel de compras
            JB_comprasModificar.setEnabled(false);
            JB_comprasEliminar.setEnabled(false);
            //panel de proveedorees
            JB_ProveedorModificar.setEnabled(false);
            JB_ProveedorEliminar.setEnabled(false);
            //panel productos
            JB_ProductoModificar.setEnabled(false);
            JB_ProductoEliminar.setEnabled(false);



        }
    }
}