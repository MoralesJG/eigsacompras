package com.eigsacompras.interfaz.usuarios;

import com.eigsacompras.controlador.RecuperacionControlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogRecuperarPassword extends JDialog {
    private JButton JB_Verificar,JB_Cancelar,JB_Enviar;
    private JTextField JTF_Codigo;
    private JPanel contentPane;
    private RecuperacionControlador recuperacionControlador;
    private int idUsuario;
    private String correo;

    public DialogRecuperarPassword(String correo) {
        setUndecorated(true);
        setResizable(true);
        setSize(420, 270);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(28, 33, 115), 4));//borde azul del Dialog

        this.correo = correo;
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
        JB_Verificar.setBorderPainted(false);
        JB_Verificar.setFocusPainted(false);
        JB_Verificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Cancelar.setBorderPainted(false);
        JB_Cancelar.setFocusPainted(false);
        JB_Cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_Enviar.setBorderPainted(false);
        JB_Enviar.setFocusPainted(false);
        JB_Enviar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //textField
        JTF_Codigo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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

        JB_Verificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(recuperacionControlador.validarRecuperacionPassword(JTF_Codigo.getText(),idUsuario)){
                    DialogCambiarPassword cambiarPassword = new DialogCambiarPassword(idUsuario);
                    cambiarPassword.setVisible(true);
                    dispose();
                }
            }
        });//boton de verificar

        JB_Enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idUsuario = recuperacionControlador.agregarRecuperacionPassword(correo);
                if(idUsuario!=-1){
                    JB_Enviar.setEnabled(false);
                }
            }
        });//boton de enviar

    }//inicializar eventos

}
