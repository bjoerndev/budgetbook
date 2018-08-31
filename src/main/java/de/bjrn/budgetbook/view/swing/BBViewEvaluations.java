package de.bjrn.budgetbook.view.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.evaluations.EvaluationConfig;
import de.bjrn.budgetbook.view.swing.evaluations.EvaluationConfigView;
import de.bjrn.budgetbook.view.swing.evaluations.EvaluationsViewFactory;

public class BBViewEvaluations extends BBViewAbstract {
	private static final long serialVersionUID = 1L;
	
	List<AccountTransaction> txsNoCategory;
	EvaluationConfigView configView;

	public BBViewEvaluations(BBViewMain main) {
		super(main);
	}

	@Override
	public void showUI() {
		setLayout(new BorderLayout());
		txsNoCategory = getModel().getAccountTransactionsWithoutCategory();
		if (!txsNoCategory.isEmpty()) {
			// Try is re-using rules helps
			getModel().execRules();
			txsNoCategory = getModel().getAccountTransactionsWithoutCategory();
		}
		configView = new EvaluationConfigView(new TxActionListener(e -> refreshUI()));
		refreshUI();
	}
	
	private void refreshUI() {
		removeAll();
		if (txsNoCategory.isEmpty()) {
			// Show evaluations
			add(getConfigPanel(), BorderLayout.WEST);
			
			EvaluationConfig config = configView.getConfig();
			AccountTransactionList txs = new AccountTransactionList(getModel().getAccountTransactions(), config.getStart(), config.getEnd());
			add(EvaluationsViewFactory.create(this, txs, config), BorderLayout.CENTER);
		} else {
			// Create rules / tx-category-mappings
			add(new JLabel(I18.tLabel("TransactionWithoutCategory")+": " + txsNoCategory.size()), BorderLayout.NORTH);
			add(new AssignTxCategoryView(this, txsNoCategory.get(0)), BorderLayout.CENTER);
		}
		updateUI();
	}
	
	private Component getConfigPanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.add(configView, BorderLayout.NORTH);
		return main;
	}

}
