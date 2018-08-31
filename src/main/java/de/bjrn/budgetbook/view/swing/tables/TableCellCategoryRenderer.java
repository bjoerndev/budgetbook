package de.bjrn.budgetbook.view.swing.tables;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.model.Category;

public class TableCellCategoryRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private Map<Long, Category> categories = new HashMap<Long, Category>();

	public TableCellCategoryRenderer(BBModel model) {
		for (Category cat : model.getCategories()) {
			categories.put(cat.getLongId(), cat);
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value instanceof Integer) {
			value = new Long((Integer) value);
		}
		if (value instanceof Long) {
			Category cat = categories.get((Long) value);
			if (cat != null) {
				value = cat.getName();
			}
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
