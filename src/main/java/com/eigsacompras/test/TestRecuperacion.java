package com.eigsacompras.test;

import com.eigsacompras.dao.RecuperacionPasswordDAO;
import com.eigsacompras.modelo.RecuperacionPassword;

import java.sql.Timestamp;

public class TestRecuperacion {
    public static void main(String[] args) {
        long tiempoActual = System.currentTimeMillis();
        Timestamp tiempo=  new Timestamp(tiempoActual + (1000 * 60 * 60 * 1));  // Tiempo actual más X horas

        RecuperacionPasswordDAO recuperacionPasswordDAO = new RecuperacionPasswordDAO();
        RecuperacionPassword rp = new RecuperacionPassword(1,"123456",tiempo);

        recuperacionPasswordDAO.eliminarRecuperacionPassword("123456");
    }
}
