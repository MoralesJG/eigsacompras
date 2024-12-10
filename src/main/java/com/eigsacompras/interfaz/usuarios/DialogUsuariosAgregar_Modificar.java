package com.eigsacompras.interfaz.usuarios;

import com.eigsacompras.controlador.UsuarioControlador;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.modelo.Usuario;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;

public class DialogUsuariosAgregar_Modificar extends JDialog {
    private JButton JB_Guardar,JB_Cancelar,JB_CambiarPassword;
    private JLabel JTF_Titulo,JL_Password, JL_Confirmar;
    private JPasswordField JTF_Password, JTF_Confirmar;
    private JPanel contentPane, JP_DatosPrincipales;
    private JTextField JTF_Nombre,JTF_Correo;
    private JComboBox JCB_Tipo;
    private Integer idUsuario;
    private Usuario usuario;
    private UsuarioControlador usuarioControlador;

    public DialogUsuariosAgregar_Modificar(Integer idUsuario) {
        this.idUsuario=idUsuario;
        this.usuarioControlador = new UsuarioControlador();
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxHeight = (int) (screenSize.height * 0.75);//usa el 75% de la altura total de la pantalla
        setSize(700, maxHeight);
        setLocationRelativeTo(null);//centrado
        setContentPane(contentPane);
        setModal(true);
        SwingUtilities.invokeLater(() -> contentPane.requestFocusInWindow());//elimina la seleccion al iniciar

        inicializarComponentes();
        inicializarEventos();
        if(idUsuario!=null)
            mostrarDatosModificar();//si hay un número en idProveedor se convierte en modificar la interfaz
    }

    public void inicializarComponentes(){
        //botones
        JB_CambiarPassword.setFocusPainted(false);
        JB_CambiarPassword.setBorderPainted(false);
        JB_CambiarPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_CambiarPassword.setContentAreaFilled(false);
        JB_CambiarPassword.setText("<html><u>Cambiar contraseña</u></html>");//para el subrayado
        JB_CambiarPassword.setVisible(false);
        JB_Guardar.setFocusPainted(false);
        JB_Guardar.setBorderPainted(false);
        JB_Guardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //text field's
        JTF_Nombre.setBorder(BorderFactory.createEmptyBorder(4,5,4,5));
        JTF_Correo.setBorder(BorderFactory.createEmptyBorder(4,5,4,5));
        JTF_Password.setBorder(BorderFactory.createEmptyBorder(4,5,4,5));
        JTF_Confirmar.setBorder(BorderFactory.createEmptyBorder(4,5,4,5));

        //JComboBox
        JCB_Tipo.addItem(TipoAcceso.ADMINISTRADOR);
        JCB_Tipo.addItem(TipoAcceso.EMPLEADO);
        JCB_Tipo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                return button;
            }
        });//necesario para quitar el borde del ComboBox
        JCB_Tipo.setBorder(BorderFactory.createEmptyBorder(4,5,4,5));
        JCB_Tipo.setFont(new Font("Roboto Light",Font.PLAIN,13));

        //para el placeholder de JTF_Password
        JTF_Password.setText("La contraseña debe contener caracteres y números");
        JTF_Password.setForeground(Color.GRAY);
        JTF_Password.setEchoChar('\0');//desactiva la censura
    }//Inicializar componentes

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
        });
        JB_Cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JB_Guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idUsuario==null){//si no hay id se agrega un nuevo usuario
                        if (new UsuarioControlador().agregarUsuario(JTF_Nombre.getText(), JTF_Correo.getText(), (TipoAcceso) JCB_Tipo.getSelectedItem(), JTF_Password.getText(),JTF_Confirmar.getText())) {//este if para que limpie la interfaz solo cuando se agrega un usuario
                            limpiarInterfaz();//esto hace el if si todo se agrega correctamente
                        }//if usuarioControlador
                }else{
                    if(new UsuarioControlador().actualizarUsuario(JTF_Nombre.getText(),JTF_Correo.getText(), (TipoAcceso) JCB_Tipo.getSelectedItem(),idUsuario)){
                        dispose();//si se actualiza correctamente se cierra la ventana
                    }//if actualizar
                }//if idUsuario
            }
        });//boton guardar compra

        JTF_Confirmar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {//para validar las contraseñas
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validarPassword();

            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validarPassword();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validarPassword();
            }
        });
        JTF_Password.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validarPassword();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validarPassword();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validarPassword();
            }
        });

        JTF_Password.addFocusListener(new FocusAdapter() {//placeholder (texto de apoyo )
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(JTF_Password.getPassword()).equals("La contraseña debe contener caracteres y números")) {
                    JTF_Password.setText("");
                    JTF_Password.setForeground(Color.BLACK);
                    JTF_Password.setEchoChar('\u2022');//se vuelve a mostrar el campo en forma de contraseña
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(JTF_Password.getPassword()).isEmpty()) {
                    JTF_Password.setText("La contraseña debe contener caracteres y números");
                    JTF_Password.setForeground(Color.GRAY);
                    JTF_Password.setEchoChar('\0');//mostrar el texto normal
                }
            }
        });

        JB_CambiarPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogRecuperarPassword recuperar = new DialogRecuperarPassword(usuario.getCorreo());
                recuperar.setVisible(true);
            }
        });//boton guardar compra

    }//eventos

    private void validarPassword() {
        String password = new String(JTF_Password.getPassword());
        String confirmar = new String(JTF_Confirmar.getPassword());

        if (password.equals("La contraseña debe contener caracteres y números")) {//valida si el texto es placeholder
            JTF_Password.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            return;//no se ejecuta el resto del codigo de este metodo
        }

        Border margen = BorderFactory.createEmptyBorder(4,5,4,5);//margenes internos

        if (usuarioControlador.validarPassword(password)) {//valida si la contraseña cumple con los requisitos
            JTF_Password.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), margen));
        } else {
            JTF_Password.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), margen));
        }

        //se valida si ambas contraseñas coinciden
        if (!confirmar.isEmpty() && password.equals(confirmar)) {
            JTF_Confirmar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), margen));
        } else if (!confirmar.isEmpty()) {
            JTF_Confirmar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), margen));
        } else {
            JTF_Confirmar.setBorder(margen);
        }
    }//validar contraseña

    private void mostrarDatosModificar(){
        JB_Guardar.setText("Modificar");
        JTF_Titulo.setText("Modificar usuario");
        usuario = new UsuarioControlador().buscarUsuarioPorId(idUsuario);
        JTF_Nombre.setText(usuario.getNombre());
        JTF_Correo.setText(usuario.getCorreo());
        JCB_Tipo.setSelectedItem(usuario.getTipo());
        JB_CambiarPassword.setVisible(true);
        //se ocultan los componenetes que no se usan par modificar
        JTF_Password.setVisible(false);
        JTF_Confirmar.setVisible(false);
        JL_Confirmar.setVisible(false);
        JL_Password.setVisible(false);

        //se cambia el tamaño de la interfaz
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxHeight = (int) (screenSize.height * 0.68);//usa el 68% de la altura total de la pantalla
        setSize(620, maxHeight);
        setLocationRelativeTo(null);//centrado
    }//mostrarDatos

    private void limpiarInterfaz(){
        JTF_Nombre.setText("");
        JTF_Correo.setText("");
        JTF_Confirmar.setText("");
        JCB_Tipo.setSelectedIndex(0);
        JTF_Password.setText("La contraseña debe contener caracteres y números");
        JTF_Password.setForeground(Color.GRAY);
        JTF_Password.setEchoChar('\0');//desactiva la censura
    }//limpiar interfaz

}
