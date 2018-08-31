package de.bjrn.budgetbook.view.swing.tables;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.bjrn.budgetbook.model.AccountTransaction;

public class AccountTransactionTableModel extends ModelTableModel<AccountTransaction> {
	
	public AccountTransactionTableModel(List<AccountTransaction> txs, String[] properties) {
		super(txs, properties);
		setEditable(AccountTransaction.PROP_DESCRIPTION, AccountTransaction.PROP_CATEGORY_MANUAL);
	}
	
	public List<AccountTransaction> getTxs() {
		return beans;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return isAmount(columnIndex) ? Double.class : super.getColumnClass(columnIndex);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = super.getValueAt(rowIndex, columnIndex);
		if (isAmount(columnIndex)) {
			return getAmount((Long) value);
		}
		if (value instanceof String) {
			value = StringUtils.replace(value.toString(), "\n", "; ");
		}
		return value;
	}
	
	private Double getAmount(Long amount) {
		return ((double)amount)/100.0;
	}
	
	private boolean isAmount(int columnIndex) {
		return properties[columnIndex].toLowerCase().endsWith("amount");
	}

}
