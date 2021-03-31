package de.bjrn.budgetbook.view.swing.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Timestamp;

public class SimpleTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (value instanceof Timestamp) {
            value = ((Timestamp)value).toLocalDateTime().toLocalDate();
        }
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected && !hasFocus) {
            if (row % 2 == 0) {
                component.setBackground(Color.WHITE);
            } else {
                component.setBackground(Color.LIGHT_GRAY);
            }
        }
        return component;
    }
}
