package com.eigsacompras.test;

import com.eigsacompras.basededatos.Conexion;
import com.eigsacompras.dao.CompraDAO;
import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.enums.TipoEstatus;
import com.eigsacompras.modelo.Compra;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class TestCompraDao {

    public static void main(String[] args) {
        try (Connection conexion = Conexion.getConexion()) {
            CompraDAO compraDAO = new CompraDAO(conexion);

            Compra compra = new Compra();
            compra.setOrdenCompra("OC-12345");
            compra.setCondiciones("Condicion");
            compra.setFechaEmision(LocalDate.now());
            compra.setOrdenTrabajo("OT-54321");
            compra.setFechaEntrega(LocalDate.now().plusDays(7));
            compra.setAgenteProveedor("Proveedor Ejemplo");
            compra.setNombreComprador("Gabriel M10");
            compra.setRevisadoPor("Natalia");
            compra.setAprobadoPor("La Señora");
            compra.setEstatus(TipoEstatus.PENDIENTE);
            compra.setNotasGenerales("Notas generales de prueba");
            compra.setTipo(TipoCompra.COMPRA);
            compra.setIdUsuario(1);
            compra.setIdProveedor(1);

            //compra.setIdCompra(1);

            if (compraDAO.agregarCompra(compra)) {
                System.out.println("Compra agregada con éxito.");
            } else {
                System.err.println("Error al agregar la compra.");
            }

            // 5. Probar listar compras
            System.out.println("Lista de compras:");
            for (Compra c : compraDAO.listarCompras()) {
                System.out.println(c);
            }

            // Puedes seguir probando actualizar y eliminar compras con más llamadas a la DAO

        } catch (SQLException e) {
            System.err.println("Error en la conexión a la base de datos: " + e.getMessage());
        }
    }
}

