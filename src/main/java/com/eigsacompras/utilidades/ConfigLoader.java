package com.eigsacompras.utilidades;
//para recorger los datos en el archivo de propiedades

import java.util.ResourceBundle;

public class ConfigLoader {
    public static String getPropiedad(String clave) {
        try {
            ResourceBundle recursoBundle = ResourceBundle.getBundle("Config");
            return recursoBundle.getString(clave);
        } catch (Exception e) {
            System.out.println("Error al obtener la propiedad: " + e.getMessage());
            return null;
        }
    }
}

