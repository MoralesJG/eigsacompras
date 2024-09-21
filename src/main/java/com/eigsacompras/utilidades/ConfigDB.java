package com.eigsacompras.utilidades;
//para recorger los datos de la base de datos en el archivo de propiedades
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ConfigDB {
    public static String getPropiedad(String clave) {
        try {
            ResourceBundle recursoBundle = ResourceBundle.getBundle("db");
            return recursoBundle.getString(clave);
        } catch (Exception e) {
            System.out.println("Error al obtener la propiedad: " + e.getMessage());
            return null;
        }
    }
}

