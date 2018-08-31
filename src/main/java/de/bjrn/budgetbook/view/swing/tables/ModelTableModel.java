package de.bjrn.budgetbook.view.swing.tables;

import java.util.Arrays;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.javalite.activejdbc.Model;

import de.bjrn.budgetbook.model.transaction.TX;
import de.bjrn.budgetbook.view.i18.I18;

public class ModelTableModel<BEAN extends Model> implements TableModel {
	
	List<BEAN> beans;
	String[] properties;
	Class<?>[] colClass;
	int beansSize;
	String[] editable;
	
	public ModelTableModel(List<BEAN> beans, String[] properties) {
		this.beans = beans;
		beansSize = beans.size();
		this.properties = properties;
		colClass = new Class<?>[properties.length];
	}
	
	public void setEditable(String... properties) {
		editable = properties;
	}

	@Override
	public int getRowCount() {
		return beansSize;
	}

	@Override
	public int getColumnCount() {
		return properties.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		String name = properties[columnIndex];
		String msg = I18.tProperty(name);
		return msg == null ? name : msg;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (colClass[columnIndex] == null) {
			Class<?> c = String.class;
			if (beansSize != 0) {
				String prop = properties[columnIndex];
				for (BEAN bean : beans) {
					Object value = bean.get(prop);
					if (value != null) {
						c = value.getClass();
						break;
					}
				}
			}
			colClass[columnIndex] = c;
		}
		return colClass[columnIndex];
	}

	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return editable == null ? false : Arrays.asList(editable).contains(properties[columnIndex]);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return beans.get(rowIndex).get(properties[columnIndex]);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		TX.start();
		try {
			BEAN bean = beans.get(rowIndex);
			bean.set(properties[columnIndex], aValue);
			bean.save();
			TX.commit();
		} catch (Exception e) {
			TX.abort();
			throw e;
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

}
