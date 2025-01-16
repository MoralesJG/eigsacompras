package com.eigsacompras.interfaz.productos;

import com.eigsacompras.controlador.ProveedorControlador;
import javax.swing.plaf.basic.BasicComboBoxUI;
import com.eigsacompras.modelo.Proveedor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class DialogAgregarProveedor extends JDialog {
    private JButton JB_Aceptar,JB_Cancelar;
    private JComboBox JCB_Proveedor;
    private JTextField JTF_Precio;
    private JPanel contentPane;
    private Map<String,Integer> mapaProveedor = new HashMap<>();
    private String proveedor;
    private Double precio;

    public DialogAgregarProveedor() {
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
    }

    public void configuracionInicial(){
        //botones
        JB_Aceptar.setBorderPainted(false);
        JB_Aceptar.setFocusPainted(false);
        JB_Aceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //textField
        JTF_Precio.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        //comboBox
        JCB_Proveedor.setUI(new BasicComboBoxUI() {
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
        JCB_Proveedor.addItem("\n                                Seleccionar");//para que aparezca centrado

        for(Proveedor proveedor: new ProveedorControlador().listarProveedor()){
            mapaProveedor.put(proveedor.getNombre(),proveedor.getIdProveedor());//para acceder al id en DialogProductosAgregar
            JCB_Proveedor.addItem(proveedor.getNombre());
        }

    }//configuracion inicial

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

        JCB_Proveedor.setRenderer(new DefaultListCellRenderer() {
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
                textArea.setPreferredSize(new Dimension(JCB_Proveedor.getWidth(), 45));//mantiene el tamaño del comboBox
                return textArea;
            }
        });//conversion del ComboBox a texArea para ajustar el texto mostrado

        JB_Aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validar()){
                    proveedor =(String) JCB_Proveedor.getSelectedItem();
                    precio = Double.valueOf(JTF_Precio.getText());
                    dispose();
                }//validar
            }//boton de aceptar
        });

    }//inicializar eventos

    public boolean validar(){
        if(JCB_Proveedor.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null,"Selecciona un producto","No seleccionado",JOptionPane.ERROR_MESSAGE);
            return false;
        }//if descripcion
        try{
            Double.valueOf(JTF_Precio.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Solo números","Datos invalidos",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }//validar

    //getter para acceder a los datos de esta interfaz
    public Double getPrecio() {
        return precio;
    }

    public String getProveedor(){
        return proveedor;
    }

    public Map<String, Integer> getMapaProveedor() {
        return mapaProveedor;
    }
}
