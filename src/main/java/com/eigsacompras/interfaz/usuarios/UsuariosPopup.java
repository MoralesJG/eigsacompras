package com.eigsacompras.interfaz.usuarios;

import com.eigsacompras.interfaz.InterfazPrincipal;
import com.eigsacompras.interfaz.inicioSesion.InicioSesion;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class UsuariosPopup extends JPopupMenu {
    private JLabel iconoAdministrar, iconoAgregar, iconoSalir;
    private JButton administrar, agregar, salir;
    private JLabel fechaContenedor, bienvenida;
    private JPanel principal;

    public UsuariosPopup() {
        inicializarComponentes();
        this.principal = principal;
        inicializarEventos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 270));
        setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        setBorder(new LineBorder(Color.GRAY));

        // PANEL de bienvenida y fecha
        JPanel panelBienvenida = new JPanel(new BorderLayout());
        panelBienvenida.setBackground(new Color(236, 240, 241));

        fechaContenedor = new JLabel();
        fechaContenedor.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))); //fecha actual
        fechaContenedor.setHorizontalAlignment(SwingConstants.CENTER);
        fechaContenedor.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        fechaContenedor.setFont(new Font("Roboto Light", Font.BOLD, 16));

        bienvenida = new JLabel("¡Bienvenido Gabriel!");//Modificar por el nombre de USUARIO
        bienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        bienvenida.setBorder(BorderFactory.createEmptyBorder(5, 0, 25, 0));
        bienvenida.setFont(new Font("Roboto Light", Font.BOLD, 21));

        panelBienvenida.add(fechaContenedor, BorderLayout.NORTH);
        panelBienvenida.add(bienvenida, BorderLayout.CENTER);

        //PANEL de botones
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));//se usa un box layout
        panelContenido.setBackground(new Color(236, 240, 241));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));

        //botón Administrar
        iconoAdministrar = new JLabel(new ImageIcon(getClass().getResource("/imagenes/archive.png")));
        administrar = new JButton("Administrar cuentas");
        JPanel subPanel1 = crearSubPanel(iconoAdministrar,administrar);

        //botón Agregar
        iconoAgregar = new JLabel(new ImageIcon(getClass().getResource("/imagenes/plus.png")));
        agregar = new JButton("Agregar usuarios");
        JPanel subPanel2 = crearSubPanel(iconoAgregar,agregar);

        // botón Salir
        iconoSalir = new JLabel(new ImageIcon(getClass().getResource("/imagenes/logout.png")));
        salir = new JButton("Salir");
        JPanel subPanel3 = crearSubPanel(iconoSalir,salir);

        panelContenido.add(subPanel1);
        panelContenido.add(Box.createVerticalStrut(10));//separación entre paneles
        panelContenido.add(subPanel2);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(subPanel3);

        add(panelBienvenida, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);
    }//inicializar componentes

    private JPanel crearSubPanel(JLabel icono, JButton boton) {
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));//se usa box layout horizontal
        subPanel.setBackground(new Color(217, 219, 220));
        subPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        subPanel.setMaximumSize(new Dimension(300, 40));//dimensiones de cada panel

        configurarBoton(boton);

        subPanel.add(icono);
        subPanel.add(boton);
        return subPanel;
    }//crear sub paneles

    private void configurarBoton(JButton boton) {
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFont(new Font("Roboto Light", Font.PLAIN, 15));
        boton.setPreferredSize(new Dimension(300, 30));
    }//configurar boton

    public void inicializarEventos(){
        administrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogAdministrarUsuarios administrar = new DialogAdministrarUsuarios();
                administrar.setVisible(true);
            }
        });//administrar

        agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogUsuariosAgregar_Modificar agregarUsuario = new DialogUsuariosAgregar_Modificar(null);
                agregarUsuario.setVisible(true);
            }
        });//agregar

        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(UsuariosPopup.this);//se obtiene la ventana principal
                if (parentFrame != null) {
                    parentFrame.dispose(); //cierra la ventana principal
                    InicioSesion login = new InicioSesion();
                    login.setVisible(true);//abre el login
                }
            }
        });//salir
    }
}
