package de.bjrn.budgetbook.view.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import de.bjrn.budgetbook.logic.Utils;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.model.DemoMode;
import de.bjrn.budgetbook.model.Rule;
import de.bjrn.budgetbook.model.transaction.TX;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.helper.JPanelEast;
import de.bjrn.budgetbook.view.swing.helper.JPanelWest;
import de.bjrn.budgetbook.view.swing.helper.JTableCompact;
import de.bjrn.budgetbook.view.swing.tables.AccountTransactionTableModel;
import de.bjrn.budgetbook.view.swing.tables.JTableFilter;
import de.bjrn.budgetbook.view.swing.tables.JTransactionTable;
import de.bjrn.budgetbook.view.swing.tables.RuleTableModel;

public class BBViewCategories extends BBViewAbstract {
	private static final long serialVersionUID = 1L;
	
	private JPanel context;
	
	public BBViewCategories(BBViewMain main) {
		super(main);
	}

	@Override
	public void showUI() {
		removeAll();
		setLayout(new BorderLayout());
		context = new JPanel(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTree(), context);
		add(split, BorderLayout.CENTER);
		select(I18.tLabel("Categories"));
		updateUI();
	}
	
	private Component createTree() {
		JTree tree = new JTree(new CategoryTreeModel(getModel()));
		for (int i = 0; i < tree.getRowCount(); i++) {
		    tree.expandRow(i);
		}
		tree.addTreeSelectionListener(e -> seleted(e));
		return new JScrollPane(tree);
	}
	
	private void setNodeContext(Component ui) {
		context.removeAll();
		context.add(ui, BorderLayout.CENTER);
		updateUI();
	}

	private void seleted(TreeSelectionEvent e) {
		Object o = e.getPath().getLastPathComponent();
		if (o instanceof DefaultMutableTreeNode) {
			o = ((DefaultMutableTreeNode) o).getUserObject();
		}
		TX.start();
		try {
			select(o);
			TX.commit();
		} catch (RuntimeException ex) {
			TX.abort();
			throw ex;
		}
	}

	private void select(Object o) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(createButtons(o));
		Component context = createContext(o);
		if (context != null) {
			p.add(context);
		}
		JPanel main = new JPanel(new BorderLayout());
		main.add(p, BorderLayout.NORTH);
		setNodeContext(main);
	}
	
	private Component createContext(Object o) {
		if (o instanceof Category) {
			final Category cat = (Category) o;
			List<Rule> rules = cat.getRules();
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			// Ignore
			JCheckBox cbIgnore = new JCheckBox(I18.tProperty(Category.PROP_IGNORE), Boolean.TRUE.equals(cat.isIgnore()));
			cbIgnore.addActionListener(new TxActionListener(e -> setIgnore(cat, cbIgnore.isSelected())));
			p.add(new JPanelWest(cbIgnore));
			// Rules
			if (!rules.isEmpty()) {
				p.add(new JLabel(I18.tLabel("Rules")));
				JTable table = new JTableCompact(new RuleTableModel(rules));
				p.add(new JScrollPane(table));
				p.add(new JPanelEast(createRuleButtons(table, cat, rules)));
			}
			// Transactions
			AccountTransactionList txs = getModel().getAccountTransactionsByCategory(cat, false);
			addTxsTable(p, txs, I18.tLabel("Transactions"));
			
			// Child-Transactions
			txs = new AccountTransactionList();
			for (Category catSub : getModel().getCategories(cat)) {
				txs.addAll(getModel().getAccountTransactionsByCategory(catSub, true));
			}
			addTxsTable(p, txs, I18.tLabel("ChildCategoryTransactions"));
			return p;
		}
		return null;
	}

	private void addTxsTable(JPanel p, AccountTransactionList txs, String title) {
		if (!txs.isEmpty()) {
			txs.sortByDate(false);
			long amount = 0;
			for (AccountTransaction tx : txs) {
				amount += tx.getValueAmount();
			}
			p.add(new JLabel(txs.size() + " " + title + ": " + (DemoMode.on ? "?" : Utils.round(amount/100.0, 2))));
//			JTable t = new JTableCompact(new AccountTransactionTableModel(txs, AccountTransaction.PROPS4VIEW_NO_SALDO));
			JTable t = new JTransactionTable(new AccountTransactionTableModel(txs, AccountTransaction.PROPS4VIEW_NO_SALDO), getModel());
			p.add(new JTableFilter<AccountTransactionTableModel>(t));
		}
	}

	private void setIgnore(Category cat, boolean ignore) {
		cat.setIgnore(ignore);
		cat.save();
	}

	private Component createRuleButtons(final JTable table, Category cat, List<Rule> rules) {
		JPanel p = new JPanel();
		JButton b = new JButton(I18.tLabel("Delete"));
		b.addActionListener(new TxActionListener(e -> deleteRules(getRule(table, rules), cat)));
		p.add(b);
		return p;
	}

	private void deleteRules(List<Rule> rules, Category cat) {
		if (rules.isEmpty()) {
			showWarning(I18.tLabel("Delete"), I18.tLabel("FirstSelectRule"));
			return;
		}
		for (Rule rule : rules) {
			rule.delete();
		}
		// Refresh
		select(cat);
	}

	private List<Rule> getRule(JTable table, List<Rule> rules) {
		List<Rule> selected = new Vector<Rule>();
		for (int row : table.getSelectedRows()) {
			selected.add(rules.get(row));
		}
		return selected;
	}

	private Component createButtons(final Object o) {
		List<JButton> buttons = new Vector<JButton>();
		if (o instanceof String) {
			// Main
			JButton bAddCat = new JButton(I18.tLabel("AddCategory"));
			bAddCat.addActionListener(e -> addCategory(null));
			buttons.add(bAddCat);
			JButton bRedoRules = new JButton(I18.tLabel("RedoRules"));
			bRedoRules.addActionListener(new TxActionListener(e -> redoRules()));
			bRedoRules.setToolTipText(I18.tLabel("RedoRules.Desc"));
			buttons.add(bRedoRules);
		} else if (o instanceof Category) {
			JButton bAddCat = new JButton(I18.tLabel("AddSubCategory"));
			bAddCat.addActionListener(e -> addCategory((Category) o));
			buttons.add(bAddCat);
			JButton bDelCat = new JButton(I18.tLabel("DelCategory"));
			bDelCat.addActionListener(new TxActionListener(e -> delCategory((Category) o)));
			buttons.add(bDelCat);
			JButton bRenameCat = new JButton(I18.tLabel("RenameCategory"));
			bRenameCat.addActionListener(e -> renameCategory((Category) o));
			buttons.add(bRenameCat);
		}
		JPanel p = new JPanel(new GridLayout(1, 0));
		for (JButton button : buttons) {
			p.add(button);
		}
		return p;
	}

	private void redoRules() {
		getModel().clearCategoriesAuto();
		showInfo(I18.tLabel("RenameCategory"), I18.tLabel("RedoRules.Done"));
	}

	private void delCategory(Category cat) {
		getModel().delete(cat);
		showUI();
	}
	
	private void renameCategory(Category cat) {
		String name = JOptionPane.showInputDialog(this, I18.tProperty("name"), cat.getName());
		TX.start();
		try {
			cat.setName(name);
			cat.save();
			showUI();
			select(cat);
			TX.commit();
		} catch (RuntimeException e) {
			TX.abort();
			throw e;
		}
	}

	private void addCategory(Category base) {
		String name = JOptionPane.showInputDialog(this, I18.tProperty("name"));
		TX.start();
		try {
			if (getModel().getCategory(base, name) != null) {
				showWarning(I18.tLabel("AddCategory"), I18.tLabel("CategoryAlreadyExists"));
				TX.abort();
				return;
			}
			Category catNew = new Category();
			catNew.setName(name);
			getModel().addCategory(base, catNew);
			showUI();
			select(catNew);
			TX.commit();
		} catch (RuntimeException e) {
			TX.abort();
			throw e;
		}
	}
	
}
