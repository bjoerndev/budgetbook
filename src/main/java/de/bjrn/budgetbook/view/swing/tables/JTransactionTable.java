package de.bjrn.budgetbook.view.swing.tables;

import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.model.DemoMode;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.helper.DemoCellRenderer;
import de.bjrn.budgetbook.view.swing.helper.JTableCompact;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class JTransactionTable extends JTableCompact {
	private static final long serialVersionUID = 1L;
	
	List<AccountTransaction> txs;
	
	public JTransactionTable(List<AccountTransaction> txs, BBModel bbModel) {
		this(createTableModel(txs), bbModel);
	}
	
	public JTransactionTable(AccountTransactionTableModel model, BBModel bbModel) {
		super(model);
		this.txs = model.getTxs();
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		for (String prop : AccountTransaction.PROPS4VIEW) {
			setRenderer(prop, new SimpleTableCellRenderer());
		}
		if (DemoMode.on) {
			setRenderer(AccountTransaction.PROP_VALUE_AMOUNT, new DemoCellRenderer(Color.GREEN.darker(), Color.RED.darker(), Color.BLACK));
			setRenderer(AccountTransaction.PROP_SALDO_AMOUNT, new DemoCellRenderer(null, Color.RED.darker(), null));
		} else {
			setRenderer(AccountTransaction.PROP_VALUE_AMOUNT, new ColorDoubleCellRenderer(Color.GREEN.darker(), Color.RED.darker(), Color.BLACK));
			setRenderer(AccountTransaction.PROP_SALDO_AMOUNT, new ColorDoubleCellRenderer(null, Color.RED.darker(), null));
		}
		
		TableCellRenderer categoryRenderer = new TableCellCategoryRenderer(bbModel);
		setRenderer(AccountTransaction.PROP_CATEGORY_AUTO, categoryRenderer);
		setRenderer(AccountTransaction.PROP_CATEGORY_MANUAL, categoryRenderer);
		setEditor(AccountTransaction.PROP_CATEGORY_MANUAL, new TableCellCategoryEditor(bbModel));
	}

	private void setRenderer(String property, TableCellRenderer renderer) {
		TableColumn col = getCol(property);
		if (col != null) {
			col.setCellRenderer(renderer);
		}
	}
	
	private void setEditor(String property, TableCellEditor editor) {
		TableColumn col = getCol(property);
		if (col != null) {
			col.setCellEditor(editor);
		}
	}

	private TableColumn getCol(String key) {
		String val = I18.tProperty(key);
		for (TableColumn col : Collections.list(getColumnModel().getColumns())) {
			if (val.equals(col.getHeaderValue().toString())) {
				return col;
			}
		}
		return null;
	}

	private static AccountTransactionTableModel createTableModel(List<AccountTransaction> txs) {
		return new AccountTransactionTableModel(txs, AccountTransaction.PROPS4VIEW);
	}
	
	public AccountTransaction getSelection() {
		int row = getSelectedRow();
		return row < 0 ? null : txs.get(convertRowIndexToModel(row));
	}

}
