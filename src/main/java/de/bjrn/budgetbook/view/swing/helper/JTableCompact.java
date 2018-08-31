package de.bjrn.budgetbook.view.swing.helper;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JTableCompact extends JTable {
	private static final long serialVersionUID = 1L;

	public JTableCompact(TableModel tableModel) {
		super(tableModel);
	}

	@Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
	
}
