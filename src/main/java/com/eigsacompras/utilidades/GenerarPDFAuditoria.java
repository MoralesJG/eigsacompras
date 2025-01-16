package com.eigsacompras.utilidades;

import com.eigsacompras.controlador.UsuarioControlador;
import com.eigsacompras.modelo.Auditoria;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GenerarPDFAuditoria {
    private UsuarioControlador usuarioControlador;

    public void generarReporte(List<Auditoria> auditorias, String path) {
        try {
            //documento
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            //logo de la empresa
            Image logo = Image.getInstance(getClass().getResource("/imagenes/LogoEigsaReporte.png"));
            agregarLogoConTextos(document, logo);

            //fuente del titulo
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC, new BaseColor(28, 33, 115));
            Paragraph titulo = new Paragraph("EQUIPOS INDUSTRIALES DEL GOLFO, S.A. DE C.V.", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            //fuente de las tablas
            Font fuenteCabecero = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(28, 33, 115));
            Font fuenteContenido = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);


            Paragraph subtitulo = new Paragraph("Reporte de Auditorías", fuenteTitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            document.add(subtitulo);

            //tabla de la auditoria
            PdfPTable tablaAuditorias = new PdfPTable(6);
            tablaAuditorias.setWidthPercentage(100);
            tablaAuditorias.setSpacingBefore(10);
            tablaAuditorias.setWidths(new float[]{1, 2, 2, 3, 2, 2});

            //columnas
            tablaAuditorias.addCell(crearCelda("ID", fuenteCabecero));
            tablaAuditorias.addCell(crearCelda("Tabla Afectada", fuenteCabecero));
            tablaAuditorias.addCell(crearCelda("Acción", fuenteCabecero));
            tablaAuditorias.addCell(crearCelda("Descripción del Cambio", fuenteCabecero));
            tablaAuditorias.addCell(crearCelda("Usuario", fuenteCabecero));
            tablaAuditorias.addCell(crearCelda("Fecha y Hora", fuenteCabecero));

            //filas
            usuarioControlador = new UsuarioControlador();
            int id = 1;
            for (Auditoria auditoria : auditorias) {
                tablaAuditorias.addCell(crearCelda(String.valueOf(id++), fuenteContenido));
                tablaAuditorias.addCell(crearCelda(auditoria.getTablaAfectada(), fuenteContenido));
                tablaAuditorias.addCell(crearCelda(auditoria.getAccion().toString(), fuenteContenido));
                tablaAuditorias.addCell(crearCelda(auditoria.getDescripcion(), fuenteContenido));
                tablaAuditorias.addCell(crearCelda(usuarioControlador.buscarUsuarioPorId(auditoria.getIdUsuario()).getNombre(), fuenteContenido));
                tablaAuditorias.addCell(crearCelda(auditoria.getFechaCambio().toString(), fuenteContenido));
            }

            document.add(tablaAuditorias);

            //pie de pagina
            document.add(new Paragraph("Reporte generado el " +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy")),
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            document.close();
            JOptionPane.showMessageDialog(null, "Reporte generado correctamente en: " + path);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
        }
    }

    private PdfPCell crearCelda(String contenido, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(contenido, font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        if (font.getSize() == 11) {
            celda.setBackgroundColor(new BaseColor(215, 216, 218));
        }
        celda.setBorderWidth(1.2F);//tamaño del borde
        return celda;
    }

    private void agregarLogoConTextos(Document document, Image logo) throws DocumentException {
        PdfPTable tablaEncabezado = new PdfPTable(3);
        tablaEncabezado.setWidthPercentage(100);
        tablaEncabezado.setWidths(new float[]{2, 1, 2});

        Font textoFuente = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, new BaseColor(28, 33, 115));

        //texto de la izquierda
        PdfPCell textoIzquierda = new PdfPCell(new Phrase("PLANTA Y OFICINAS:\n" +
                "MÉXICO Calle 4 a Sur No.3, Col. Independencia\n" +
                "Tultitlan, Edo. de México C.P. 54915\n" +
                "Tel.: 55 58 99 53 00\n" +
                "Fax.: 55 58 99 53 01\n" +
                "e-mail: contacto@eigsamexico.com", textoFuente));
        textoIzquierda.setHorizontalAlignment(Element.ALIGN_LEFT);
        textoIzquierda.setBorder(Rectangle.NO_BORDER);
        tablaEncabezado.addCell(textoIzquierda);

        //logo central
        logo.scaleToFit(130, 80); // Tamaño del logo
        PdfPCell celdaLogo = new PdfPCell(logo);
        celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        tablaEncabezado.addCell(celdaLogo);

        //texto de la derecha
        PdfPCell textoDerecha = new PdfPCell(new Phrase("                          TABASCO: \n" +
                "                          Carretera Circuito del Golfo\n" +
                "                          km 123 + 100, No. 141, Cárdenas, Tab.\n" +
                "                          Tel.: 937 37-212-56\n" +
                "                                  937 37-299-44\n" +
                "                                  937 37-288-33\n" +
                "                          Fax.: 937 37-243-31\n" +
                "                          e-mail: cardenas@eigsamexico.com", textoFuente));
        textoDerecha.setHorizontalAlignment(Element.ALIGN_LEFT);
        textoDerecha.setBorder(Rectangle.NO_BORDER);
        tablaEncabezado.addCell(textoDerecha);

        document.add(tablaEncabezado);
    }
}
