package de.bjrn.budgetbook.view.swing.tables;

import de.bjrn.budgetbook.model.AccountTransaction;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
			if (AccountTransaction.PROP_OTHER_NAME.equals(properties[columnIndex])) {
				String otherName2 = beans.get(rowIndex).getString(AccountTransaction.PROP_OTHER_NAME2);
				if (!StringUtils.isBlank(otherName2)) {
					value = value.toString() + otherName2;
				}
			}
			value = StringUtils.replace(value.toString(), "\n", "; ").trim();
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
