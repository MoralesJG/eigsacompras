package com.eigsacompras.controlador;

import com.eigsacompras.dao.RecuperacionPasswordDAO;
import com.eigsacompras.modelo.RecuperacionPassword;
import com.eigsacompras.utilidades.Email;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.Random;

public class RecuperacionControlador {
    private RecuperacionPasswordDAO recuperacionPasswordDAO;

    public RecuperacionControlador() {
        this.recuperacionPasswordDAO = new RecuperacionPasswordDAO();
    }

    public void agregarRecuperacionPassword(String email){
        int idUsuario = recuperacionPasswordDAO.idUsuarioPorEmail(email);
        String codigo = generarCodigoRecuperacion();
        Timestamp fecha = generarFechaExpiracion();

        RecuperacionPassword rs = new RecuperacionPassword(idUsuario,codigo,fecha);
        boolean exito = recuperacionPasswordDAO.agregarCodigoRecuperacion(rs);
        //se envia por correo
        if(exito)
            Email.enviarEmail(email,codigo);
    }//agregar

    public void validarRecuperacionPassword(String codigo, int idUsuario){
        if(recuperacionPasswordDAO.validarCodigoRecuperacion(codigo,idUsuario)){
            recuperacionPasswordDAO.eliminarRecuperacionPassword(codigo);//se elimina de la base de datos
            JOptionPane.showMessageDialog(null, "Código válidado correctamente", "Validar", JOptionPane.INFORMATION_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null, "Código no válido. Verificar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//validar

    public String generarCodigoRecuperacion() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);//número de 6 digitos
        return String.valueOf(codigo);
    }//generar Codigo

    public Timestamp generarFechaExpiracion() {
        long tiempoActual = System.currentTimeMillis();
        return new Timestamp(tiempoActual + (1000 * 60 * 60));//tiempo actual más una hora
    }
}
