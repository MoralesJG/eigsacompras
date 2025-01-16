package com.eigsacompras.interfaz.usuarios;

import com.eigsacompras.controlador.RecuperacionControlador;
import com.eigsacompras.controlador.UsuarioControlador;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class DialogCambiarPassword extends JDialog {
    private JPasswordField JTF_Password,JTF_ConfirmarPassword;
    private JButton JB_Cambiar,JB_Cancelar;
    private JPanel contentPane;
    private RecuperacionControlador recuperacionControlador;
    private UsuarioControlador usuarioControlador;
    private int idUsuario;

    public DialogCambiarPassword(int idUsuario) {
        setUndecorated(true);
        setResizable(true);
        setSize(420, 270);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(28, 33, 115), 4));//borde azul del Dialog

        this.idUsuario = idUsuario;
        usuarioControlador = new UsuarioControlador();
        recuperacionControlador = new RecuperacionControlador();
        configuracionInicial();
        inicializarEventos();

        setLocationRelativeTo(null);//centrado
        setContentPane(contentPane);
        setModal(true);
        SwingUtilities.invokeLater(() -> contentPane.requestFocusInWindow());//elimina la seleccion al iniciar
    }

    public void configuracionInicial(){
        //botones
        JB_Cambiar.setBorderPainted(false);
        JB_Cambiar.setFocusPainted(false);
        JB_Cambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //text field
        JTF_Password.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JTF_ConfirmarPassword.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        //para el placeholder de JTF_Password
        JTF_Password.setText("La contraseña debe contener caracteres y números");
        JTF_Password.setForeground(Color.GRAY);
        JTF_Password.setEchoChar('\0');//desactiva la censura
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
        });//cambia el color del texto al boton cancelar al ser presionado

        JB_Cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });//boton de cancelar

        JB_Cambiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(JTF_Password.getPassword()).trim();//trim quita los espacios en blanco
                String confirmarPassword = new String(JTF_ConfirmarPassword.getPassword()).trim();
                if(usuarioControlador.cambiarPassword(idUsuario,password,confirmarPassword)){
                    dispose();
                }
            }
        });//boton de verificar

        JTF_ConfirmarPassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {//para validar las contraseñas
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
    }//inicializar eventos

    private void validarPassword() {
        String password = new String(JTF_Password.getPassword());
        String confirmar = new String(JTF_ConfirmarPassword.getPassword());

        if (password.equals("La contraseña debe contener caracteres y números")) {//valida si el texto es placeholder
            JTF_Password.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            return;//no se ejecuta el resto del codigo de este metodo
        }

        Border margen = BorderFactory.createEmptyBorder(5, 5, 5, 5);//margenes internos

        if (usuarioControlador.validarPassword(password)) {//valida si la contraseña cumple con los requisitos
            JTF_Password.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), margen));
        } else {
            JTF_Password.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), margen));
        }

        //se valida si ambas contraseñas coinciden
        if (!confirmar.isEmpty() && password.equals(confirmar)) {
            JTF_ConfirmarPassword.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), margen));
        } else if (!confirmar.isEmpty()) {
            JTF_ConfirmarPassword.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), margen));
        } else {
            JTF_ConfirmarPassword.setBorder(margen);
        }
    }//validar contraseña
}
