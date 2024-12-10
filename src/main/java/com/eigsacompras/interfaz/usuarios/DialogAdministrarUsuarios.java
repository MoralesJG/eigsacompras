package com.eigsacompras.interfaz.usuarios;

import com.eigsacompras.controlador.UsuarioControlador;
import com.eigsacompras.modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogAdministrarUsuarios extends JDialog {
    private JPanel contentPane;
    private JPanel JP_DatosPrincipales;
    private JTextField JTF_Buscar;
    private JLabel JL_Titulo;
    private JButton JB_Modificar;
    private JButton JB_Desactivar;
    private JPanel JP_Tabla;
    private JTable JT_Tabla;
    private DefaultTableModel modeloTabla;
    private JScrollPane scroll;
    private UsuarioControlador usuarioControlador;
    private Usuario usuario;
    private Map<String,Integer> mapa;

    public DialogAdministrarUsuarios() {
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxHeight = (int) (screenSize.height * 0.93);//usa el 93% de la altura total de la pantalla
        setSize(750, maxHeight);
        setLocationRelativeTo(null);//centrado
        setContentPane(contentPane);
        setModal(true);
        SwingUtilities.invokeLater(() -> contentPane.requestFocusInWindow());//elimina la seleccion al iniciar

        inicializarComponentes();
        inicalizarEventos();

    }

    public void inicializarComponentes(){
        usuarioControlador = new UsuarioControlador();//se inicia el controlador
        JB_Modificar.setFocusPainted(false);
        JB_Modificar.setBorderPainted(false);
        JB_Modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Desactivar.setFocusPainted(false);
        JB_Desactivar.setBorderPainted(false);
        JB_Desactivar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTF_Buscar.setText("Buscar usuarios");
        JTF_Buscar.setBorder(BorderFactory.createEmptyBorder(2,5,2,5));
        JTF_Buscar.setForeground(Color.GRAY);

        //TABLA
        modeloTabla = new DefaultTableModel();
        JT_Tabla = new JTable(modeloTabla);
        //encabezado
        JT_Tabla.getTableHeader().setFont(new Font("Roboto Light", Font.BOLD,17));
        JT_Tabla.getTableHeader().setBackground(new Color(236,240,241));
        //filas
        JT_Tabla.setShowGrid(false);//cuadriculas
        JT_Tabla.setRowHeight(20);
        JT_Tabla.setFont(new Font("Roboto Light", Font.PLAIN,15));
        JT_Tabla.setBackground(new Color(236,240,241));
        scroll = new JScrollPane(JT_Tabla);
        JP_Tabla.add(scroll);
        //datos de la tabla
        List<Usuario> usuarios = usuarioControlador.listarUsuario();
        cargarDatosTabla(usuarios);
    }//configuraciones inicales

    public void cargarDatosTabla(List<Usuario> usuarios){
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);
        //columnas
        modeloTabla.addColumn("No.");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Correo");
        modeloTabla.addColumn("Tipo");
        //filas
        JT_Tabla.setRowHeight(27);
        mapa = new HashMap<>();
        int contador = 1;
        for(Usuario usuario : usuarios){
            Object [] fila = {contador++,usuario.getNombre(),usuario.getCorreo(),usuario.getTipo()};
            mapa.put(usuario.getCorreo(),usuario.getIdUsuario());//para eliminar usuario
            modeloTabla.addRow(fila);
        }

        //centrar las filas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < JT_Tabla.getColumnCount(); i++) {
            JT_Tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    public void inicalizarEventos(){

        JB_Desactivar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_Desactivar.setForeground(new Color(0,0,0,220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_Desactivar.setForeground(Color.WHITE);
            }
        });
        JB_Desactivar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seleccionado = JT_Tabla.getSelectedRow();
                if(seleccionado!=-1){
                    usuarioControlador.desactivarUsuario(mapa.get(JT_Tabla.getValueAt(seleccionado,2)));
                    cargarDatosTabla(usuarioControlador.listarUsuario());
                }

            }
        });

        JTF_Buscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {//al dar click en buscar se quita el texto de apoyo
                if (JTF_Buscar.getText().equals("Buscar usuarios")) {
                    JTF_Buscar.setText("");
                    JTF_Buscar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {//cuando buscar está vacio se agrega este texto
                if (JTF_Buscar.getText().isEmpty()) {
                    JTF_Buscar.setText("Buscar usuarios");
                    JTF_Buscar.setForeground(Color.GRAY);
                }
            }
        });

        JTF_Buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//para quitar el foco en caso de que se de ESC
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_Buscar.setFocusable(false);
                    JTF_Buscar.setFocusable(true);
                }
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
        });//JT_Tabla

        JTF_Buscar.addKeyListener(new KeyAdapter() {//para filtrar las busquedas
            @Override
            public void keyReleased(KeyEvent e) {
                String termino = JTF_Buscar.getText();
                List<Usuario> resultados = usuarioControlador.buscarUsuarios(termino);
                cargarDatosTabla(resultados);
            }
        });

        JB_Modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seleccionado = JT_Tabla.getSelectedRow();
                if(seleccionado !=-1){
                    DialogUsuariosAgregar_Modificar modificarAgregar = new DialogUsuariosAgregar_Modificar(mapa.get(JT_Tabla.getValueAt(seleccionado,2)));
                    //se desactivan algunos componentes para mejorar la visualización de la pantalla a mostrar
                    JB_Modificar.setVisible(false);
                    JB_Desactivar.setVisible(false);
                    JL_Titulo.setText("Usuarios");
                    modificarAgregar.setVisible(true);//se abre la interfaz
                    //una vez cerrando la interfaz se vuelve a mostrar lo anteriormente ocultado
                    JB_Modificar.setVisible(true);
                    JB_Desactivar.setVisible(true);
                    JL_Titulo.setText("Gestión de cuentas");
                    cargarDatosTabla(usuarioControlador.listarUsuario());//se actualiza la tabla

                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un usuario para modifiar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }//if seleccionado
            }
        });
    }//eventos
}
