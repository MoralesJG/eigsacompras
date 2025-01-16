package com.eigsacompras.interfaz.inicioSesion;

import com.eigsacompras.controlador.RecuperacionControlador;
import com.eigsacompras.controlador.UsuarioControlador;
import com.eigsacompras.interfaz.usuarios.DialogCambiarPassword;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogPasswordOlvidada extends JDialog {
    private JPanel contentPane;
    private JButton JB_Verificar;
    private JButton JB_Cancelar;
    private JTextField JTF_Codigo;
    private JButton JB_Enviar;
    private JTextField JTF_Correo;
    private RecuperacionControlador recuperacionControlador;
    private UsuarioControlador usuarioControlador;
    private int idUsuario;

    public DialogPasswordOlvidada() {
        setUndecorated(true);
        setResizable(true);
        setSize(520, 370);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(28, 33, 115), 4));//borde azul del Dialog

        recuperacionControlador = new RecuperacionControlador();
        usuarioControlador = new UsuarioControlador();
        configuracionInicial();
        inicializarEventos();

        setLocationRelativeTo(null);//centrado
        setContentPane(contentPane);
        setModal(true);
        SwingUtilities.invokeLater(() -> contentPane.requestFocusInWindow());//elimina la seleccion al iniciar
    }

    public void configuracionInicial() {
        //botones
        JB_Verificar.setBorderPainted(false);
        JB_Verificar.setFocusPainted(false);
        JB_Verificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Verificar.setEnabled(false);//se desaactiva hasta que se envie el codigo al correo
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Enviar.setBorderPainted(false);
        JB_Enviar.setFocusPainted(false);
        JB_Enviar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //textField
        JTF_Codigo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTF_Correo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }//configuracion inicial

    public void inicializarEventos() {
        JB_Cancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JB_Cancelar.setForeground(new Color(0, 0, 0, 220));
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

        JB_Verificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (recuperacionControlador.validarRecuperacionPassword(JTF_Codigo.getText().trim(), idUsuario)) {
                    DialogCambiarPassword cambiarPassword = new DialogCambiarPassword(idUsuario);
                    dispose();
                    cambiarPassword.setVisible(true);
                }
            }
        });//boton de verificar

        JB_Enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idUsuario = recuperacionControlador.agregarRecuperacionPassword(JTF_Correo.getText().trim());
                if (idUsuario != -1) {
                    JB_Enviar.setEnabled(false);
                    JB_Verificar.setEnabled(true);
                }
            }
        });//boton de enviar

    }//inicializar eventos
}
