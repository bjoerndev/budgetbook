package de.bjrn.budgetbook.view.swing.tables;

import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.model.Category;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TableCellCategoryRenderer extends SimpleTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private Map<Long, Category> categories = new HashMap<>();

	public TableCellCategoryRenderer(BBModel model) {
		for (Category cat : model.getCategories()) {
			categories.put(cat.getLongId(), cat);
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value instanceof Integer) {
			value = Long.valueOf((Integer) value);
		}
		if (value instanceof Long) {
			Category cat = categories.get(value);
			if (cat != null) {
				value = cat.getName();
			}
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
