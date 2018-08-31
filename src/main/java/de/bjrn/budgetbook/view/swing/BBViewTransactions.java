package de.bjrn.budgetbook.view.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;

import de.bjrn.budgetbook.model.Account;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.swing.tables.AccountTransactionTableModel;
import de.bjrn.budgetbook.view.swing.tables.JTableFilter;
import de.bjrn.budgetbook.view.swing.tables.JTransactionTable;

public class BBViewTransactions extends BBViewAbstract {
	private static final long serialVersionUID = 1L;
	
	List<JButton> actions;
	JTable table;
	Map<Account, JCheckBox> mapAccountCb;
	
	public BBViewTransactions(BBViewMain main) {
		super(main);
		mapAccountCb = new HashMap<Account, JCheckBox>();
		actions = createAccessActions();
	}

	private List<JButton> createAccessActions() {
		List<JButton> buttons = new Vector<JButton>();
		return buttons;
	}
	
	private Component createConfig() {
		JPanel p = new JPanel();
		for (Account account : getModel().getAccounts()) {
			JCheckBox cb = mapAccountCb.get(account);
			if (cb == null) {
				cb = mapAccountCb.get(account) == null ? new JCheckBox(account.toStringShort(), true) : mapAccountCb.get(account);
				cb.setToolTipText(account.toString());
				cb.addActionListener(new TxActionListener(e -> showUI()));
				mapAccountCb.put(account, cb);
			}
			p.add(cb);
		}
		return p;
	}

	@Override
	public void showUI() {
		removeAll();
		setLayout(new BorderLayout());
		add(createConfig(), BorderLayout.NORTH);
		add(createTable(), BorderLayout.CENTER);
		JPanel buttons = new JPanel(new GridLayout(1, 0));
//		buttons.add(createClearButton());
		for (JButton action : actions) {
			buttons.add(action);
		}
		add(buttons, BorderLayout.SOUTH);
		updateUI();
	}
	
	private Component createTable() {
		List<String> accounts = new Vector<String>();
		for (Entry<Account, JCheckBox> entry : mapAccountCb.entrySet()) {
			if (entry.getValue().isSelected()) {
				accounts.add(entry.getKey().getIdentifier());
			}
		}
		AccountTransactionList txs = new AccountTransactionList();
		for (AccountTransaction tx : getModel().getAccountTransactions()) {
			if (accounts.contains(tx.getAccountID())) {
				txs.add(tx);
			}
		}
		table = new JTransactionTable(txs, getModel());
		return new JTableFilter<AccountTransactionTableModel>(table);
	}

}
