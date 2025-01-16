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

    public int agregarRecuperacionPassword(String email){
        int idUsuario = recuperacionPasswordDAO.idUsuarioPorEmail(email);
        if(idUsuario!=-1) {
            String codigo = generarCodigoRecuperacion();
            Timestamp fecha = generarFechaExpiracion();

            RecuperacionPassword rs = new RecuperacionPassword(idUsuario, codigo, fecha);
            boolean exito = recuperacionPasswordDAO.agregarCodigoRecuperacion(rs);
            //se envia por correo
            if (exito)
                Email.enviarEmail(email, codigo);

        }else{
            JOptionPane.showMessageDialog(null,"No existe usuario con ese correo ","Error",JOptionPane.ERROR_MESSAGE);
        }
        return idUsuario;
    }//agregar

    public boolean validarRecuperacionPassword(String codigo, int idUsuario){
        if(recuperacionPasswordDAO.validarCodigoRecuperacion(codigo,idUsuario)){
            recuperacionPasswordDAO.eliminarRecuperacionPassword(idUsuario);//se elimina de la base de datos
            JOptionPane.showMessageDialog(null, "Código correcto", "Validar", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }else {
            JOptionPane.showMessageDialog(null, "Código no válido. Verificar", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }//validar


    private String generarCodigoRecuperacion() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);//número de 6 digitos
        return String.valueOf(codigo);
    }//generar Codigo

    private Timestamp generarFechaExpiracion() {
        long tiempoActual = System.currentTimeMillis();
        return new Timestamp(tiempoActual + (1000 * 60 * 60));//tiempo actual más una hora
    }
}
