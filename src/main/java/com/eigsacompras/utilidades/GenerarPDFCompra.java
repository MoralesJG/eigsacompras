package com.eigsacompras.utilidades;

import com.eigsacompras.enums.TipoCompra;
import com.eigsacompras.modelo.Compra;
import com.eigsacompras.modelo.CompraProducto;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GenerarPDFCompra {

    public void generarReporte(List<Compra> compras, String path) {
        try {

            //crear documento y escritor
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            //logo de la empresa y textos a los lados
            Image logo = Image.getInstance(getClass().getResource("/imagenes/LogoEigsaReporte.png"));
            agregarLogoConTextos(document,logo);

            //fuente del titulo del pdf
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC, new BaseColor(28, 33, 115));
            Paragraph titulo = new Paragraph("EQUIPOS INDUSTRIALES DEL GOLFO, S.A. DE C.V.", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            //fuentes para las tablas
            Font fuenteCabecero = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(28, 33, 115));
            Font fuenteContenido = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

           //generar las tablas para cada compra
            int contador = 1;
            for (Compra compra : compras) {
                //para ver donde inicia cada compra
                Paragraph separador = new Paragraph("Compra "+contador++,new Font(Font.FontFamily.HELVETICA,13,Font.BOLD));
                separador.setAlignment(Element.ALIGN_CENTER);
                separador.setSpacingAfter(20);
                document.add(separador);
                //tabla 1:Proveedor, Orden de compra, Orden de trabajo, Fecha de emisión, Condiciones, Fecha entrega, observaciones, Agente Proveedor
                PdfPTable tabla1 = new PdfPTable(4);
                tabla1.setWidthPercentage(100);
                tabla1.setSpacingBefore(10);
                tabla1.addCell(crearCelda("Proveedor", fuenteCabecero));
                tabla1.addCell(crearCelda("Orden de Compra", fuenteCabecero));
                tabla1.addCell(crearCelda("Orden de Trabajo", fuenteCabecero));
                tabla1.addCell(crearCelda("Fecha de Emisión", fuenteCabecero));
                tabla1.addCell(crearCelda(compra.getProveedorNombre(), fuenteContenido));
                tabla1.addCell(crearCelda(compra.getOrdenCompra(), fuenteContenido));
                tabla1.addCell(crearCelda(compra.getOrdenTrabajo(), fuenteContenido));
                tabla1.addCell(crearCelda(compra.getFechaEmision().toString(), fuenteContenido));
                //simular una segunda tabla
                tabla1.addCell(crearCelda("Condiciones", fuenteCabecero));
                tabla1.addCell(crearCelda("Fecha de Entrega", fuenteCabecero));
                tabla1.addCell(crearCelda("Observaciones", fuenteCabecero));
                tabla1.addCell(crearCelda("Agente Proveedor", fuenteCabecero));
                tabla1.addCell(crearCelda(compra.getCondiciones(), fuenteContenido));
                if(compra.getTipo().equals(TipoCompra.COMPRA)){
                    tabla1.addCell(crearCelda(compra.getFechaEntrega().toString(), fuenteContenido));
                }else{
                    tabla1.addCell(crearCelda("", fuenteContenido));
                }
                tabla1.addCell(crearCelda(compra.getCondiciones(), fuenteContenido));
                tabla1.addCell(crearCelda(compra.getAgenteProveedor(), fuenteContenido));
                document.add(tabla1);

                //tabla 2:Productos asociados a las compras
                PdfPTable tabla2 = new PdfPTable(5);
                tabla2.setWidthPercentage(100);
                tabla2.setSpacingBefore(10);
                tabla2.addCell(crearCelda("Partida", fuenteCabecero));
                tabla2.addCell(crearCelda("Cantidad", fuenteCabecero));
                tabla2.addCell(crearCelda("Descripción", fuenteCabecero));
                tabla2.addCell(crearCelda("Precio Unitario", fuenteCabecero));
                tabla2.addCell(crearCelda("Total", fuenteCabecero));
                for (CompraProducto producto : compra.getProductos()) {
                    tabla2.addCell(crearCelda(String.valueOf(producto.getPartida()), fuenteContenido));
                    tabla2.addCell(crearCelda(producto.getCantidad(), fuenteContenido));
                    tabla2.addCell(crearCelda(producto.getDescripcionProducto(), fuenteContenido));
                    tabla2.addCell(crearCelda("$"+producto.getPrecioUnitario(), fuenteContenido));
                    tabla2.addCell(crearCelda("$"+producto.getTotal(), fuenteContenido));
                }
                document.add(tabla2);

                //notas generales
                Paragraph notas = new Paragraph("Notas Generales: " + compra.getNotasGenerales(), fuenteContenido);
                notas.setSpacingBefore(10);
                notas.setSpacingAfter(10);
                document.add(notas);

                //tabla 3:Comprador, Revisado Por, Aprobado Por y si aplica la fehca inicio renta y fecha fin renta
                PdfPTable tabla3;
                if(compra.getTipo().equals(TipoCompra.RENTA)){
                    tabla3 = new PdfPTable(5);//si es tipo renta se agregarán 5 columnas
                }else{
                    tabla3 = new PdfPTable(3);
                }
                tabla3.setWidthPercentage(100);
                tabla3.setSpacingBefore(10);
                tabla3.addCell(crearCelda("Comprador", fuenteCabecero));
                tabla3.addCell(crearCelda("Revisado Por", fuenteCabecero));
                tabla3.addCell(crearCelda("Aprobado Por", fuenteCabecero));
                if(compra.getTipo().equals(TipoCompra.RENTA)) {
                    tabla3.addCell(crearCelda("Fecha Inicio Renta", fuenteCabecero));
                    tabla3.addCell(crearCelda("Fecha Fin Renta", fuenteCabecero));
                }
                tabla3.addCell(crearCelda(compra.getNombreComprador(), fuenteContenido));
                tabla3.addCell(crearCelda(compra.getRevisadoPor(), fuenteContenido));
                tabla3.addCell(crearCelda(compra.getAprobadoPor(), fuenteContenido));
                if(compra.getTipo().equals(TipoCompra.RENTA)){
                    tabla3.addCell(crearCelda(compra.getFechaInicioRenta().toString(),fuenteContenido));
                    tabla3.addCell(crearCelda(compra.getFechaFinRenta().toString(),fuenteContenido));
                }
                document.add(tabla3);

                //espacio entre las compras
                document.add(Chunk.NEWLINE);
            }

            document.add(new Paragraph("Reporte generado el "+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy")),new Font(Font.FontFamily.HELVETICA,12,Font.BOLD)));
            document.close();
            if(path.contains("Documents")) {//para que el mensaje se imprima solo cuando es para generar y no para imprimir
                JOptionPane.showMessageDialog(null, "Reporte generado correctamente en: " + path);
            }//if temp
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
        }
    }

    private PdfPCell crearCelda(String contenido, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(contenido, font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        if(font.getSize()==11){
            celda.setBackgroundColor(new BaseColor(215,216,218));//fondo de las columnas
        }
        celda.setBorderWidth(1.2F);//tamaño del borde de la tabla
        return celda;
    }//centrado de celdas


    private void agregarLogoConTextos(Document document, Image logo) throws DocumentException {
        PdfPTable tablaEncabezado = new PdfPTable(3);//tabla con 3 columnas
        tablaEncabezado.setWidthPercentage(100);
        tablaEncabezado.setWidths(new float[]{2, 1, 2});//proporcion de las columnas

        Font textoFuente = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, new BaseColor(28, 33, 115));

        //texto izquierda
        PdfPCell textoIzquierda = new PdfPCell(new Phrase("PLANTA Y OFICINAS:\n" +
                "MÉXICO Calle 4 a Sur No.3, Col. Independencia\n" +
                "Tultitlan, Edo. de México C.P. 54915\n" +
                "Tel.: 55 58 99 53 00\n" +
                "Fax.: 55 58 99 53 01\n" +
                "e-mail: contacto@eigsamexico.com", textoFuente));
        textoIzquierda.setHorizontalAlignment(Element.ALIGN_LEFT);
        textoIzquierda.setBorder(Rectangle.NO_BORDER);
        tablaEncabezado.addCell(textoIzquierda);

        //logo
        logo.scaleToFit(130, 80);//tamaño del logo
        PdfPCell celdaLogo = new PdfPCell(logo);
        celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        tablaEncabezado.addCell(celdaLogo);

        //texto derecha
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
