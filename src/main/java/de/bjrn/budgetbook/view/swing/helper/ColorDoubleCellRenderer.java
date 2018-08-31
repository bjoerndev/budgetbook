package de.bjrn.budgetbook.view.swing.helper;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorDoubleCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private static final DecimalFormat formatter = new DecimalFormat( "#.00" );

	Color positive, negative, zero;
	
	public ColorDoubleCellRenderer(Color positive, Color negative, Color zero) {
		this.positive = positive;
		this.negative = negative;
		this.zero = zero;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Double val = value instanceof Double ? (Double) value : null;
		if (val != null) {
			value = formatter.format((Number)value);
		}
		//Cells are by default rendered as a JLabel.
	    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	    //Get the status for the current row.
	    if (val != null) {
	    	if (val < 0) {
	    		l.setForeground(negative);
	    	} else if (val > 0) {
	    		l.setForeground(positive);
	    	} else {
	    		l.setForeground(zero);
	    	}
	    	l.setHorizontalAlignment(JLabel.RIGHT);  
	    }
	    return l;
	}

}
