package com.eigsacompras.interfaz.inicioSesion;

import com.eigsacompras.controlador.RecuperacionControlador;
import com.eigsacompras.controlador.UsuarioControlador;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.interfaz.InterfazPrincipal;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class InicioSesion extends JFrame{
    private JPanel JP_Principal;
    private JPanel JP_Logo;
    private JPanel JP_Login;
    private JLabel JL_LogoIniciar;
    private JButton JB_IniciarSesionIniciar;
    private JButton JB_CrearCuentaIniciar;
    private JTextField JTF_UsuarioCorreoIniciar;
    private JPanel JP_UsuarioCorreo;
    private JPanel JP_Password;
    private JButton JB_PasswordOlvidada;
    private JPasswordField JTF_PasswordIniciar;
    private JLabel JL_IconoMailIniciar;
    private JLabel JL_IconoLockIniciar;
    private JPanel JP_CrearCuenta;
    private JPanel JP_IniciarSesion,JP_Contenedor;
    private JTextField JTF_UsuarioCrear;
    private JPasswordField JTF_PasswordCrear;
    private JTextField JTF_CorreoCrear;
    private JLabel JL_IconoUser;
    private JLabel JL_IconoMailCrear;
    private JLabel JL_IconoLockCrear;
    private JLabel JL_IconoLogo;
    private JButton JB_IniciarSesionCrear;
    private JPasswordField JTF_PasswordConfirmar;
    private JButton JB_RegistrarseCrear;
    private JLabel JL_IconoConfirmarPassword;
    private UsuarioControlador usuarioControlador;
    private RecuperacionControlador recuperacionControlador;
    private CardLayout cardLayout;

    public InicioSesion() {
        setTitle("Inicio de sesión - EIGSA");
        setSize(960, 540);
        setExtendedState(JFrame.MAXIMIZED_BOTH);//pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Panel contenedor para ajustar la interfaz
        JPanel JP_Contenedor = new JPanel(new GridBagLayout());
        JP_Contenedor.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;//para que no se expanda para llenar espacio
        gbc.gridx = 0;
        gbc.gridy = 0;

        //tamaño predefinido del panel principal
        JP_Principal.setPreferredSize(new Dimension(850, 450));//tamaño preferido
        JP_Principal.setMinimumSize(new Dimension(850, 450));//tamaño minimo

        JP_Contenedor.add(JP_Principal, gbc);
        setContentPane(JP_Contenedor);
        //card layout
        cardLayout = (CardLayout) JP_Principal.getLayout();
        cardLayout.show(JP_Principal, "IniciarSesion");

        this.usuarioControlador = new UsuarioControlador();
        //Iniciar sesion
        inicializarComponentesIniciar();
        inicializarEventosIniciar();
        //Crear cuenta
        inicializarComponentesCrear();
        inicializarEventosCrear();

        setVisible(true);
        SwingUtilities.invokeLater(() -> JP_Principal.requestFocusInWindow());//quita la seleccion al iniciar de los componentes
    }

    //PANEL INICIAR SESION

    public void inicializarComponentesIniciar(){
        //text fields
        inicializarTextFieldIniciar();

        //botones
        JB_IniciarSesionIniciar.setFocusPainted(false);
        JB_IniciarSesionIniciar.setBorderPainted(false);
        JB_IniciarSesionIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //botones que no tienes fondo
        JB_CrearCuentaIniciar.setFocusPainted(false);
        JB_CrearCuentaIniciar.setBorder(null);
        JB_CrearCuentaIniciar.setContentAreaFilled(false);
        JB_CrearCuentaIniciar.setBorder(new LineBorder(new Color(255, 255, 255, 0)));
        JB_CrearCuentaIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JB_PasswordOlvidada.setFocusPainted(false);
        JB_PasswordOlvidada.setBorder(null);
        JB_PasswordOlvidada.setContentAreaFilled(false);
        JB_PasswordOlvidada.setBorder(new LineBorder(new Color(255, 255, 255, 0)));
        JB_PasswordOlvidada.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //logos
        JL_LogoIniciar.setIcon(new ImageIcon(getClass().getResource("/imagenes/LogoEigsaInicioSesion.png")));
        JL_IconoLockIniciar.setIcon(new ImageIcon(getClass().getResource("/imagenes/lock.png")));
        JL_IconoMailIniciar.setIcon(new ImageIcon(getClass().getResource("/imagenes/mail.png")));
    }//inicializar componentes iniciar sesion

    public void inicializarTextFieldIniciar(){
        JTF_PasswordIniciar.setText("Contraseña");
        JTF_PasswordIniciar.setForeground(new Color(199,200,202));
        JTF_PasswordIniciar.setEchoChar('\0');//desactiva la censura
        JTF_PasswordIniciar.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0, new Color(199,200,202)),new EmptyBorder(0,5,2,5)));//margenes y borde inferior
        JTF_UsuarioCorreoIniciar.setText("Usuario o correo");
        JTF_UsuarioCorreoIniciar.setForeground(new Color(199,200,202));
        JTF_UsuarioCorreoIniciar.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0, new Color(199,200,202)),new EmptyBorder(0,5,2,5)));
    }//text field

    public void inicializarEventosIniciar() {
        JB_IniciarSesionIniciar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                JB_IniciarSesionIniciar.setForeground(new Color(0, 0, 0, 180));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_IniciarSesionIniciar.setForeground(new Color(199,200,202));
            }
        });


        JTF_UsuarioCorreoIniciar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (JTF_UsuarioCorreoIniciar.getText().equals("Usuario o correo")) {
                    JTF_UsuarioCorreoIniciar.setText("");
                    JTF_UsuarioCorreoIniciar.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (JTF_UsuarioCorreoIniciar.getText().isEmpty()) {
                    JTF_UsuarioCorreoIniciar.setText("Usuario o correo");
                    JTF_UsuarioCorreoIniciar.setForeground(new Color(199,200,202));
                }
            }
        });

        JTF_PasswordIniciar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (JTF_PasswordIniciar.getText().equals("Contraseña")) {
                    JTF_PasswordIniciar.setText("");
                    JTF_PasswordIniciar.setForeground(Color.WHITE);
                    JTF_PasswordIniciar.setEchoChar('\u2022');//se vuelve a mostrar el campo en forma de contraseña
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (JTF_PasswordIniciar.getText().isEmpty()) {
                    JTF_PasswordIniciar.setText("Contraseña");
                    JTF_PasswordIniciar.setForeground(new Color(199,200,202));
                    JTF_PasswordIniciar.setEchoChar('\0');//mostrar el texto normal
                }
            }
        });

        JTF_UsuarioCorreoIniciar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_UsuarioCorreoIniciar.setFocusable(false);
                    JTF_UsuarioCorreoIniciar.setFocusable(true);
                }
            }
        });

        JTF_PasswordIniciar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_PasswordIniciar.setFocusable(false);
                    JTF_PasswordIniciar.setFocusable(true);
                }
            }
        });

        JB_IniciarSesionIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correoUsuario = JTF_UsuarioCorreoIniciar.getText().trim();//el trimp para eliminar espacios en blanco
                String password = new String(JTF_PasswordIniciar.getPassword()).trim();
                if(usuarioControlador.validarUsuarioLogin(correoUsuario,password)){
                    int idUsuario = usuarioControlador.buscarUsuarioPorCorreoNombre(correoUsuario);
                    InterfazPrincipal inicio = new InterfazPrincipal(idUsuario);
                    dispose();
                    inicio.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Por favor, inténtelo de nuevo.", "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });//Boton de iniciar sesion

        JB_CrearCuentaIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_Principal,"CrearCuenta");
                inicializarTextFieldCrear();
            }
        });//Boton de agregar

        JB_PasswordOlvidada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogPasswordOlvidada passwordOlvidada = new DialogPasswordOlvidada();
                passwordOlvidada.setVisible(true);
            }
        });//Boton de agregar
    }//eventos iniciar sesion

    //PANEL CREAR CUENTA

    public void inicializarComponentesCrear(){
        //text fields
        inicializarTextFieldCrear();

        //botones
        JB_RegistrarseCrear.setFocusPainted(false);
        JB_RegistrarseCrear.setBorderPainted(false);
        JB_RegistrarseCrear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //botones que no tienes fondo
        JB_IniciarSesionCrear.setFocusPainted(false);
        JB_IniciarSesionCrear.setBorder(null);
        JB_IniciarSesionCrear.setContentAreaFilled(false);
        JB_IniciarSesionCrear.setBorder(new LineBorder(new Color(255, 255, 255, 0)));
        JB_IniciarSesionCrear.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //logos
        JL_IconoLogo.setIcon(new ImageIcon(getClass().getResource("/imagenes/LogoEigsaInicioSesion.png")));
        JL_IconoLockCrear.setIcon(new ImageIcon(getClass().getResource("/imagenes/lock.png")));
        JL_IconoConfirmarPassword.setIcon(new ImageIcon(getClass().getResource("/imagenes/lock.png")));
        JL_IconoMailCrear.setIcon(new ImageIcon(getClass().getResource("/imagenes/mail.png")));
        JL_IconoUser.setIcon(new ImageIcon(getClass().getResource("/imagenes/people.png")));
    }//inicializar componentes crear cuenta

    private void inicializarTextFieldCrear(){
        JTF_UsuarioCrear.setText("Nombre de usuario");
        JTF_UsuarioCrear.setForeground(new Color(199,200,202));
        JTF_UsuarioCrear.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0, new Color(199,200,202)),new EmptyBorder(0,5,2,5)));
        JTF_CorreoCrear.setText("Correo");
        JTF_CorreoCrear.setForeground(new Color(199,200,202));
        JTF_CorreoCrear.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0, new Color(199,200,202)),new EmptyBorder(0,5,2,5)));
        JTF_PasswordCrear.setText("Contraseña");
        JTF_PasswordCrear.setForeground(new Color(199,200,202));
        JTF_PasswordCrear.setEchoChar('\0');//desactiva la censura
        JTF_PasswordCrear.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0, new Color(199,200,202)),new EmptyBorder(0,5,2,5)));//margenes y borde inferior
        JTF_PasswordConfirmar.setText("Confirmar contraseña");
        JTF_PasswordConfirmar.setForeground(new Color(199,200,202));
        JTF_PasswordConfirmar.setEchoChar('\0');//desactiva la censura
        JTF_PasswordConfirmar.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0, new Color(199,200,202)),new EmptyBorder(0,5,2,5)));//margenes y borde inferior
    }//text field

    public void inicializarEventosCrear() {
        JB_RegistrarseCrear.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                JB_RegistrarseCrear.setForeground(new Color(0, 0, 0, 180));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JB_RegistrarseCrear.setForeground(new Color(199,200,202));
            }
        });

        JTF_UsuarioCrear.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (JTF_UsuarioCrear.getText().equals("Nombre de usuario")) {
                    JTF_UsuarioCrear.setText("");
                    JTF_UsuarioCrear.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (JTF_UsuarioCrear.getText().isEmpty()) {
                    JTF_UsuarioCrear.setText("Nombre de usuario");
                    JTF_UsuarioCrear.setForeground(new Color(199,200,202));
                }
            }
        });

        JTF_CorreoCrear.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (JTF_CorreoCrear.getText().equals("Correo")) {
                    JTF_CorreoCrear.setText("");
                    JTF_CorreoCrear.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (JTF_CorreoCrear.getText().isEmpty()) {
                    JTF_CorreoCrear.setText("Correo");
                    JTF_CorreoCrear.setForeground(new Color(199,200,202));
                }
            }
        });

        JTF_PasswordCrear.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (JTF_PasswordCrear.getText().equals("Contraseña")) {
                    JTF_PasswordCrear.setText("");
                    JTF_PasswordCrear.setForeground(Color.WHITE);
                    JTF_PasswordCrear.setEchoChar('\u2022');//se vuelve a mostrar el campo en forma de contraseña
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (JTF_PasswordCrear.getText().isEmpty()) {
                    JTF_PasswordCrear.setText("Contraseña");
                    JTF_PasswordCrear.setForeground(new Color(199,200,202));
                    JTF_PasswordCrear.setEchoChar('\0');//mostrar el texto normal
                }
            }
        });

        JTF_PasswordConfirmar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (JTF_PasswordConfirmar.getText().equals("Confirmar contraseña")) {
                    JTF_PasswordConfirmar.setText("");
                    JTF_PasswordConfirmar.setForeground(Color.WHITE);
                    JTF_PasswordConfirmar.setEchoChar('\u2022');//se vuelve a mostrar el campo en forma de contraseña
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (JTF_PasswordConfirmar.getText().isEmpty()) {
                    JTF_PasswordConfirmar.setText("Confirmar contraseña");
                    JTF_PasswordConfirmar.setForeground(new Color(199,200,202));
                    JTF_PasswordConfirmar.setEchoChar('\0');//mostrar el texto normal
                }
            }
        });

        JTF_UsuarioCrear.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_UsuarioCrear.setFocusable(false);
                    JTF_UsuarioCrear.setFocusable(true);
                }
            }
        });

        JTF_CorreoCrear.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_CorreoCrear.setFocusable(false);
                    JTF_CorreoCrear.setFocusable(true);
                }
            }
        });

        JTF_PasswordCrear.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_PasswordCrear.setFocusable(false);
                    JTF_PasswordCrear.setFocusable(true);
                }
            }
        });

        JTF_PasswordConfirmar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JTF_PasswordConfirmar.setFocusable(false);
                    JTF_PasswordConfirmar.setFocusable(true);
                }
            }
        });

        JTF_PasswordCrear.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {//para validar las contraseñas
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
        JTF_PasswordConfirmar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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

        JTF_CorreoCrear.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {//para el correo
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validarCorreo();

            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validarCorreo();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validarCorreo();
            }
        });

        JB_RegistrarseCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = !JTF_UsuarioCrear.getText().equals("Nobre de usuario") ? JTF_UsuarioCrear.getText() : "";
                String correo = !JTF_CorreoCrear.getText().equals("Correo") ? JTF_CorreoCrear.getText() : "";
                String password = !JTF_PasswordCrear.getText().equals("Contraseña") ? new String(JTF_PasswordCrear.getPassword()).trim() : "";
                String passwordConfirmar = !JTF_PasswordConfirmar.getText().equals("Confirmar contraseña") ? new String(JTF_PasswordConfirmar.getPassword()).trim() : "";//el trim para eliminar espacios en blanco
                if(usuarioControlador.agregarUsuario(usuario,correo,TipoAcceso.EMPLEADO,password,passwordConfirmar)){//se manda por defecto empleado
                    cardLayout.show(JP_Principal,"IniciarSesion");//se abre iniciar sesion
                    inicializarTextFieldIniciar();
                }
            }
        });//Boton de crear cuenta

        JB_IniciarSesionCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(JP_Principal,"IniciarSesion");
                inicializarTextFieldIniciar();
            }
        });//Boton de iniciar sesion
    }//eventos crear cuenta


    private void validarPassword() {
        String password = new String(JTF_PasswordCrear.getPassword());
        String confirmar = new String(JTF_PasswordConfirmar.getPassword());

        //bordes
        Border bordeNormal = new CompoundBorder(new MatteBorder(0, 0, 1, 0, new Color(199, 200, 202)), new EmptyBorder(0, 5, 2, 5));
        Border bordeRojo = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.RED), new EmptyBorder(0, 5, 2, 5));
        Border bordeVerde = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.GREEN), new EmptyBorder(0, 5, 2, 5));

        //se valida la contraseña
        if (password.equals("Contraseña") || password.isEmpty()) {
            JTF_PasswordCrear.setBorder(bordeNormal);
        } else if (usuarioControlador.validarPassword(password)) {//validar contraseña con el controlador
            JTF_PasswordCrear.setBorder(bordeVerde);
        } else {
            JTF_PasswordCrear.setBorder(bordeRojo);
        }

        //se valida el text field de confirmar contraseña
        if (confirmar.equals("Confirmar contraseña") || confirmar.isEmpty()) {
            JTF_PasswordConfirmar.setBorder(bordeNormal);
        } else if (!confirmar.isEmpty() && password.equals(confirmar)) {//si coinciden
            JTF_PasswordConfirmar.setBorder(bordeVerde);
        } else {
            JTF_PasswordConfirmar.setBorder(bordeRojo);
        }

    }//validar contraseña

    private void validarCorreo(){
        //bordes
        Border bordeNormal = new CompoundBorder(new MatteBorder(0, 0, 1, 0, new Color(199, 200, 202)), new EmptyBorder(0, 5, 2, 5));
        Border bordeRojo = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.RED), new EmptyBorder(0, 5, 2, 5));
        Border bordeVerde = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.GREEN), new EmptyBorder(0, 5, 2, 5));

        if(JTF_CorreoCrear.getText().equals("Correo") || JTF_CorreoCrear.getText().isEmpty()){
            JTF_CorreoCrear.setBorder(bordeNormal);
        }else if(usuarioControlador.validarCorreo(JTF_CorreoCrear.getText())){
            JTF_CorreoCrear.setBorder(bordeVerde);
        }else{
            JTF_CorreoCrear.setBorder(bordeRojo);
        }
    }//validar correo


    public static void main(String[] args) {
        new InicioSesion();
    }
}