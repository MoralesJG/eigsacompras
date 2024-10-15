package com.eigsacompras.utilidades;

import org.mindrot.jbcrypt.BCrypt;

//este metodo de encriptaión es sencillo pero funciona correctamente. No se necesia una seguridad extrema ya que
//el software no se centra en lo que son usuarios
public class EncryptedPassword {
    //Encriptar contraseñas
    public static String encriptar(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    //verificar contrseñas
    public static boolean verificar(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

}
