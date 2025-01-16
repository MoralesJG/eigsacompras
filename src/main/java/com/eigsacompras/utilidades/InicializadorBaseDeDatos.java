package com.eigsacompras.utilidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class InicializadorBaseDeDatos {
    public static void iniciarBD() {
        Properties properties = new Properties();

        try (InputStream input = InicializadorBaseDeDatos.class.getClassLoader().getResourceAsStream("Config.properties")) {
            if (input == null) {
                throw new RuntimeException("Archivo Config.properties no encontrado.");
            }
            properties.load(input);

            //se lee el properties
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            //primero intenta conectarse con el .properties
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Conexión exitosa con las credenciales configuradas.");
                return;
            } catch (Exception e) {
                System.out.println("Fallo al conectar con las credenciales configuradas. Intentando con root...");
            }

            //Se conecta con root si el .properties no funciona lo que significa que no se ha iniciado nada
            String rootUrl = "jdbc:mysql://localhost:3306";
            String rootUser = "root";
            String rootPassword = ""; //cambiar si root tiene contraseña (IMPORTANTE)

            try (Connection rootConnection = DriverManager.getConnection(rootUrl, rootUser, rootPassword)) {
                System.out.println("Conexión exitosa como root.");
                try (Statement statement = rootConnection.createStatement()) {
                    //Se crea el usuario y la contraseña para acceder ahora directo en el .properties
                    statement.executeUpdate("CREATE USER IF NOT EXISTS 'eigsa_usuario'@'localhost' IDENTIFIED BY '@E20.c24=US.mx';");
                    statement.executeUpdate("GRANT ALL PRIVILEGES ON *.* TO 'eigsa_usuario'@'localhost';");
                    statement.executeUpdate("FLUSH PRIVILEGES;");
                    System.out.println("Usuario 'eigsa_usuario' creado y permisos asignados.");

                    //se ejecuta el script del sql que tiene la base de datos y las tablas
                    String sqlFilePath = "src/main/resources/compraseigsa.sql";
                    ejecutarScript(rootConnection, sqlFilePath);
                }
            }

            //se itenta nuevamente con los datos del archivo .properties
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Conexión exitosa con las credenciales configuradas después de la inicialización.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar la base de datos.");
        }
    }

    private static void ejecutarScript(Connection connection, String sqlFilePath) throws Exception {
        try (Statement statement = connection.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }
            String[] queries = sql.toString().split(";");
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    statement.execute(query);
                }
            }
            System.out.println("Script SQL ejecutado correctamente.");
        }//buffered
    }//ejecutar script
}