package com.eigsacompras.interfaz;

import com.eigsacompras.controlador.ProductoControlador;
import com.eigsacompras.modelo.Producto;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class DialogAgregarProducto extends JDialog {
    private JPanel contentPane;
    private JButton JB_Aceptar, JB_Cancelar;
    private JComboBox JCB_Descripcion;
    private JTextField JTF_Cantidad, JTF_Precio;
    private int cantidad;
    private Double precio;
    private String descripcion;
    private Map<String,Integer> mapaProducto = new HashMap<>();

    public DialogAgregarProducto() {
        setUndecorated(true);
        setResizable(true);
        setSize(420, 270);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(28, 33, 115), 4));//borde azul del Dialog

        configuracionInicial();
        inicializarEventos();

        setLocationRelativeTo(null);//centrado
        setContentPane(contentPane);
        setModal(true);
        SwingUtilities.invokeLater(() -> contentPane.requestFocusInWindow());//elimina la seleccion al iniciar

    }//constructor

    public void configuracionInicial(){
        //botones
        JB_Aceptar.setBorderPainted(false);
        JB_Aceptar.setFocusPainted(false);
        JB_Aceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //textField
        JTF_Cantidad.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        JTF_Precio.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        //comboBox
        JCB_Descripcion.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");//reemplaza el boton de comboBox
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setBorder(BorderFactory.createEmptyBorder());//elimina los bordes
                return button;
            }
        });//necesario para quitar el borde y modificar el ComboBox
        //insercción de datos al ComboBox
        JCB_Descripcion.addItem("\n                                Seleccionar");//para que aparezca centrado

        for(Producto producto: new ProductoControlador().listarProducto()){
            mapaProducto.put(producto.getDescripcion(),producto.getIdProducto());//para acceder al id en DialogComprasAgregar
            JCB_Descripcion.addItem(producto.getDescripcion());
        }

    }//configuracion iniciar

    public void inicializarEventos(){
        JB_Cancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_Cancelar.setForeground(new Color(0,0,0,220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_Cancelar.setForeground(Color.WHITE);
            }
        });//cambia el color del texto al boton Eliminar al ser presionado

        JB_Cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });//boton de cancelar

        JCB_Descripcion.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JTextArea textArea = new JTextArea();
                textArea.setText(value != null ? value.toString() : "");//agregan los datos al textarea(combobox)
                textArea.setBorder(BorderFactory.createEmptyBorder(5,10,3,0));
                textArea.setLineWrap(true);//multilinea
                textArea.setWrapStyleWord(true);//para mostrar palabras completas
                textArea.setOpaque(true);

                if (isSelected) {//para cuando se selecciona o no
                    textArea.setBackground(list.getSelectionBackground());
                    textArea.setForeground(list.getSelectionForeground());
                } else {
                    textArea.setBackground(list.getBackground());
                    textArea.setForeground(list.getForeground());
                }
                textArea.setPreferredSize(new Dimension(JCB_Descripcion.getWidth(), 70));//mantiene el tamaño del comboBox
                return textArea;
            }
        });//conversion del ComboBox a texArea para ajustar el texto mostrado

        JB_Aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validar()){
                    descripcion =(String) JCB_Descripcion.getSelectedItem();
                    cantidad = Integer.parseInt(JTF_Cantidad.getText());
                    precio = Double.valueOf(JTF_Precio.getText());
                    dispose();
                }//validar
            }//boton de aceptar
        });

        JTF_Cantidad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(!Character.isDigit(c)){
                    e.consume();
                }
            }
        });//para que solo acepte números el text field Cantidad

    }//inicializar eventos

    public boolean validar(){
        if(JCB_Descripcion.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null,"Selecciona un producto","No seleccionado",JOptionPane.ERROR_MESSAGE);
            return false;
        }//if descripcion
        if(JTF_Cantidad.getText().isEmpty() || JTF_Precio.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"No pueden haber campos vacios","Datos vacios",JOptionPane.ERROR_MESSAGE);
            return false;
        }//if cantidad
        try{
            Double.valueOf(JTF_Precio.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Solo números","Datos invalidos",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    //getter para acceder a los datos de esta interfaz
    public Double getPrecio() {
        return precio;
    }
    public int getCantidad() {
        return cantidad;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public Map<String, Integer> getMapaProducto() {
        return mapaProducto;
    }
}
