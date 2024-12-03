package com.eigsacompras.interfaz.notificaciones;

import com.eigsacompras.controlador.NotificacionControlador;
import com.eigsacompras.modelo.Notificacion;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionListener;
import java.time.format.FormatStyle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class NotificacionesPopup extends JPopupMenu {
    private JPanel panelContenido;
    private JTextField buscador;
    private JButton botonSalir;
    private NotificacionControlador noticacionControlador;


    public NotificacionesPopup() {
        noticacionControlador = new NotificacionControlador();
        inicializarComponentes();
        cargarNotificaciones(noticacionControlador.listarNotificacion());
        inicializarEventos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 500));
        setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        //PANEL de busqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.setBackground(new Color(236, 240, 241));
        panelBusqueda.setBorder(new MatteBorder(1, 0, 1, 0, new Color(199, 200, 202)));//borde superior e inferior
        buscador = new JTextField();
        buscador.setText("Buscar...");
        buscador.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        buscador.setBackground(new Color(236, 240, 241));
        buscador.setFont(new Font("Roboto Light", Font.PLAIN, 14));
        JLabel iconoBuscar = new JLabel(new ImageIcon(getClass().getResource("/imagenes/search.png")));//se agrega un icono a lado izquierdo de buscar
        iconoBuscar.setBorder(new EmptyBorder(0, 10, 0, 5));
        botonSalir = new JButton(new ImageIcon(getClass().getResource("/imagenes/close.png")));//se agrega un boton a lado derecho de buscar
        botonSalir.setBorder(new EmptyBorder(0, 5, 0, 13));
        botonSalir.setFocusPainted(false);
        botonSalir.setBorderPainted(false);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBusqueda.add(iconoBuscar, BorderLayout.WEST);
        panelBusqueda.add(botonSalir, BorderLayout.EAST);
        panelBusqueda.add(buscador, BorderLayout.CENTER);

        //PANEL de notificaciones
        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(new Color(236, 240, 241));

        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);//se elimina el scroll visible
        scrollPane.setBorder(null);

        //se agregan los componentes
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }//inicializar componentes

    private void cargarNotificaciones(List<Notificacion> notificaciones) {
        panelContenido.removeAll(); //limpia todo el panel
        LocalDate hoy = LocalDate.now();//se extrae siempre la fecha de hoy
        if (!notificaciones.isEmpty()) {//verifica si está vacío o no las notificaciones
            for (Notificacion notificacion : notificaciones) {
                long diasFaltantes = ChronoUnit.DAYS.between(hoy, notificacion.getFecha());//días faltantes entre la fecha de entrega y la fecha de hoy
                String fecha = notificacion.getFecha().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));//se le da formato a la fecha
                String mensaje = notificacion.getFecha().isEqual(hoy) || notificacion.getFecha().isBefore(hoy)//mensaje de cuantos días faltan o está atrasado
                        ? " atrasada por " + (-diasFaltantes) + "días"
                        : " se entrega en " + diasFaltantes + " días";
                agregarNotificacion(notificacion, notificacion.getMensaje() + mensaje, fecha);
            }//for
        } else {
            agregarNotificacion(null, null, null);
        }
        panelContenido.revalidate();
        panelContenido.repaint();
    }//cargar

    private void agregarNotificacion(Notificacion notificacion, String mensaje, String fecha) {
        JPanel notificacionPanel = new JPanel(new BorderLayout());//panel que contendrá cada notificación
        notificacionPanel.setBorder(BorderFactory.createLineBorder(new Color(199, 200, 202)));
        notificacionPanel.setBackground(new Color(236, 240, 241));

        if (notificacion != null) {//para validar si hay notificaciones o no
            JLabel textoNotificacion = new JLabel("<html><b>" + mensaje + "</b><br>" + fecha + "</html>");//el label que contendrá el texto de notificacion
            textoNotificacion.setFont(new Font("Roboto Light", Font.PLAIN, 12));
            textoNotificacion.setBorder(new EmptyBorder(5, 10, 5, 5));
            textoNotificacion.setForeground(notificacion.getFecha().isEqual(LocalDate.now()) || notificacion.getFecha().isBefore(LocalDate.now())
                    ? new Color(156, 21, 10)
                    : Color.BLACK);

            JButton botonEliminar = new JButton();
            botonEliminar.setIcon(new ImageIcon(getClass().getResource("/imagenes/delete.png")));
            botonEliminar.setPreferredSize(new Dimension(40, 40));
            botonEliminar.setFocusPainted(false);
            botonEliminar.setBorderPainted(false);
            botonEliminar.setContentAreaFilled(false);
            botonEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            botonEliminar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (noticacionControlador.eliminarNotificacion(notificacion.getIdNotificacion())) {
                        panelContenido.remove(notificacionPanel);//elimina de la vista la notificación
                        panelContenido.revalidate();
                        panelContenido.repaint();
                        mensajeFlotante("Notificación eliminada");
                    } else {
                        mensajeFlotante("Error al eliminar la notificación");
                    }
                }
            });

            notificacionPanel.setPreferredSize(new Dimension(300, 70));
            notificacionPanel.add(textoNotificacion, BorderLayout.CENTER);
            notificacionPanel.add(botonEliminar, BorderLayout.EAST);
        } else {
            JLabel textoNotificacion = new JLabel("No hay notificaciones para mostrar");//el label que contendrá el texto de no hay notificaciones
            textoNotificacion.setFont(new Font("Roboto Light", Font.BOLD, 16));
            textoNotificacion.setHorizontalAlignment(SwingConstants.CENTER);
            textoNotificacion.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            notificacionPanel.add(textoNotificacion, BorderLayout.CENTER);
        }//cierre if
        panelContenido.add(notificacionPanel);
    }//agregar notificacion

    private void inicializarEventos() {
        buscador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarNotificaciones(noticacionControlador.buscarNotificaciones(buscador.getText()));
            }
        });

        buscador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (buscador.getText().equals("Buscar...")) {
                    buscador.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (buscador.getText().isEmpty()) {
                    buscador.setText("Buscar...");
                }
            }
        });

        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscador.setText("Buscar...");
                cargarNotificaciones(noticacionControlador.listarNotificacion());
            }
        });
    }//inicializar eventos

    private void mensajeFlotante(String mensaje) {
        //este metodo es para mostrar un mensaje de confirmación a la hora de eliminar una notificación
        JLabel mensajeFlotante = new JLabel(mensaje);//label del mensaje
        mensajeFlotante.setOpaque(true);
        mensajeFlotante.setBackground(new Color(0, 0, 0, 180));
        mensajeFlotante.setForeground(Color.WHITE);
        mensajeFlotante.setFont(new Font("Roboto Light", Font.BOLD, 14));
        mensajeFlotante.setHorizontalAlignment(SwingConstants.CENTER);
        mensajeFlotante.setBorder(new EmptyBorder(10, 10, 10, 10));

        JWindow ventanaFlotante = new JWindow();//este es un contenedor para mostrarse en cualquier lugar de la pantalla
        ventanaFlotante.add(mensajeFlotante);
        ventanaFlotante.setSize(200, 50);
        ventanaFlotante.setLocationRelativeTo(null);
        ventanaFlotante.setVisible(true);

        Timer timer = new Timer(2000, e -> ventanaFlotante.dispose());//tiempo qur dura el mensaje
        timer.setRepeats(false);
        timer.start();
    }//mensaje flotante
}
