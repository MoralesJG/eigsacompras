package com.eigsacompras.utilidades;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Properties;

public class Email {
    public static boolean enviarEmail(String destinatario, String codigo) {
        //Se recuperan datos de propiedades
        String remitente = ConfigLoader.getPropiedad("email.remitente");
        String password = ConfigLoader.getPropiedad("email.password");
        String smtpHost = ConfigLoader.getPropiedad("email.smtp.host");
        String smtpPort = ConfigLoader.getPropiedad("email.smtp.port");

        String asunto = "Recuperación de contraseña - Código de verificación";
        String cuerpo = "Hola,\n"
                + "Hemos recibido una solicitud para restablecer la contraseña de su cuenta. "
                + "A continuación, encontrará su código de verificación:\n\n"
                + "Código de verificación: " + codigo + "\n\n"
                + "Ingrese este código en el sistema para restablecer su contraseña. "
                + "Este código expirará en 1 horas.\n\n"
                + "Si no solicitó este cambio, por favor ignore este correo.";
        //se configura para gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        //autentica el remitente
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            // Crear el mensaje
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); //A quien se envia
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            //Enviar el mensaje
            Transport.send(mensaje);
            JOptionPane.showMessageDialog(null, "El correo ha sido enviado con éxito. Por favor, revisa tu bandeja de entrada.", "Envío de Token", JOptionPane.INFORMATION_MESSAGE);
            return true;


        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(null, "El correo no pudo ser enviado. Por favor, inténtalo nuevamente.\nDetalles del error: " + e.getMessage(), "Error en envío de Token", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
