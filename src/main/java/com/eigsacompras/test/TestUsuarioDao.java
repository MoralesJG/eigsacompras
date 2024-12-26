package com.eigsacompras.test;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.UsuarioDAO;
import com.eigsacompras.enums.TipoAcceso;
import com.eigsacompras.modelo.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

public class TestUsuarioDao {
    public static void main(String[] args) {
        try (Connection conexion = Conexion.getConexion()){
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = new Usuario(4);
            usuario.setNombre("luisa");
            usuario.setCorreo("montse.21@gmail.com");
            usuario.setTipo(TipoAcceso.EMPLEADO);
            usuario.setContrasena("lol1010");
           //usuario.setIdUsuario(4);

            if (usuarioDAO.agregarUsuario(usuario)!=-1)
                System.out.println("Usuario agregado correctamente");
            else
                System.out.println("Error al agregar");

            System.out.println("-----Listar------");
            for (Usuario c:usuarioDAO.listarUsuario()){
                System.out.println(c);
            }
        }catch (SQLException e){
            System.out.println("Error al inicar la conexion");
        }

    }
}
