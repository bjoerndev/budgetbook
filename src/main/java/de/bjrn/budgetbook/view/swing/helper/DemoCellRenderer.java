package de.bjrn.budgetbook.view.swing.helper;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

public class DemoCellRenderer extends ColorDoubleCellRenderer {
	private static final long serialVersionUID = 1L;

	public DemoCellRenderer(Color positive, Color negative, Color zero) {
		super(positive, negative, zero);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component ui = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (ui instanceof JLabel) {
			((JLabel) ui).setText("<-Demo-Mode->");
		}
		return ui;
	}

}
