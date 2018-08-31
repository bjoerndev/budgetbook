package de.bjrn.budgetbook.view.swing.tables;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.model.Category;

public class TableCellCategoryEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;
	
	private JComboBox<Category> jComboBox = new JComboBox<Category>();
	Vector<Category> cats = new Vector<Category>();
	boolean cellEditingStopped = false;

	public TableCellCategoryEditor(BBModel model) {
		cats.add(null);
		for (Category cat : model.getCategories()) {
			cats.add(cat);
		}
	}
	
	@Override
	public Object getCellEditorValue() {
		Object item = jComboBox.getSelectedItem();
    	if (item != null) {
    		return ((Category) item).getLongId();
    	}
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        jComboBox = new JComboBox<Category>(cats);
        jComboBox.setSelectedItem(value);

        jComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    fireEditingStopped();
                }
            }
        });
        jComboBox.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                cellEditingStopped = false;
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                cellEditingStopped = true;
                fireEditingCanceled();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        return jComboBox;
	}
	
    @Override
    public boolean stopCellEditing() {
        return cellEditingStopped;
    }

}
