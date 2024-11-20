package com.eigsacompras.interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTreeTableCellRenderer extends DefaultTableCellRenderer {
    private Font customFont = new Font("Roboto Light", Font.PLAIN, 14); // Cambia la fuente y tamaño aquí

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Aplica la fuente personalizada
        c.setFont(customFont);

        // Configura el color de fondo y de texto si está seleccionado
        if (isSelected) {
            c.setBackground(new Color(184, 207, 229)); // Color de fondo cuando está seleccionado
            c.setForeground(Color.BLACK); // Color del texto cuando está seleccionado
        } else {
            c.setBackground(Color.WHITE); // Color de fondo cuando no está seleccionado
            c.setForeground(Color.BLACK); // Color del texto cuando no está seleccionado
        }

        return c;
    }
}
