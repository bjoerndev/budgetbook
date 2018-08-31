package de.bjrn.budgetbook.view.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;

import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.model.MapPropAccountTransaction2Rule;
import de.bjrn.budgetbook.model.Rule;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.helper.JInfoArea;
import de.bjrn.budgetbook.view.swing.helper.JPanelNorth;
import de.bjrn.budgetbook.view.swing.helper.JTableCompact;
import de.bjrn.budgetbook.view.swing.tables.AccountTransactionTableModel;

public class AssignTxCategoryView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final static String[] TX_STRING_PROPS_FOR_RULE = new String[] {
			AccountTransaction.PROP_OTHER_NAME, 
			AccountTransaction.PROP_TEXT,
			AccountTransaction.PROP_USAGE
			};
	
	BBViewAbstract view;
	AccountTransaction tx;
	JTree categoryTree;
	Map<String, JTextComponent> mapProp2TextComponent;
	
	public AssignTxCategoryView(BBViewAbstract view, AccountTransaction tx) {
		this.view = view;
		this.tx = tx;
		mapProp2TextComponent = new HashMap<String, JTextComponent>();
		init();
	}

	private void init() {
		removeAll();
		setLayout(new BorderLayout());
		add(createTransactionArea(), BorderLayout.NORTH);
		add(createAssignArea(), BorderLayout.CENTER);
	}

	private Component createTransactionArea() {
		List<AccountTransaction> txs = new Vector<AccountTransaction>();
		txs.add(tx);
		return new JScrollPane(new JTableCompact(new AccountTransactionTableModel(txs, AccountTransaction.PROPS4VIEW)));
	}

	private Component createAssignArea() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		categoryTree = new JTree(new CategoryTreeModel(view.getModel()));
		for (int i = 0; i < categoryTree.getRowCount(); i++) {
			categoryTree.expandRow(i);
		}
		p.add(new JScrollPane(categoryTree));
		p.add(createTabs());
		return p;
	}

	private Component createTabs() {
		JTabbedPane tab = new JTabbedPane();
		tab.addTab(I18.tLabel("CreateRule"), createCreateRule());
		tab.addTab(I18.tLabel("AssignCategory"), createAssignCategory());
		return tab;
	}

	private Component createAssignCategory() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(new JInfoArea(I18.tLabel("AssignManualCategoryDesc")));
		JButton b = new JButton(I18.tLabel("AssignCategory"));
		b.addActionListener(new TxActionListener(e -> assignCategory()));
		p.add(b);
		return new JPanelNorth(p);
	}

	private void assignCategory() {
		Category sel = getSelectedCategory();
		if (sel == null) {
			JOptionPane.showMessageDialog(this, I18.tLabel("FirstSelectCategory"), I18.tLabel("AssignCategory"), JOptionPane.WARNING_MESSAGE);
			return;
		}
		tx.setCategoryManual(sel.getLongId());
		tx.save();
		// Refresh
		view.showUI();
	}

	private Category getSelectedCategory() {
		TreePath path = categoryTree.getSelectionPath();
		if (path == null) {
			return null;
		}
		Object o = path.getLastPathComponent();
		return o instanceof Category ? (Category) o : null;
	}

	private Component createCreateRule() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(new JInfoArea(I18.tLabel("CreateRuleDesc")));
		p.add(createRuleEditor());
		JButton b = new JButton(I18.tLabel("CreateRule"));
		b.addActionListener(new TxActionListener(e -> createRule()));
		p.add(b);
		return new JPanelNorth(p);
	}

	private Component createRuleEditor() {
		JPanel pMain = new JPanel();
		pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
		MapPropAccountTransaction2Rule mapPropAccountTransaction2Rule = new MapPropAccountTransaction2Rule();
		for (String propTx : TX_STRING_PROPS_FOR_RULE) {
			JPanel p = new JPanel(new GridLayout(0, 2));
			String propRule = mapPropAccountTransaction2Rule.get(propTx);
			p.add(new JLabel(I18.tProperty(propRule)));
			JTextComponent tf = propTx.equals(AccountTransaction.PROP_USAGE) ? new JTextArea() : new JTextField();
			tf.setText(tx.getString(propTx));
			p.add(tf);
			mapProp2TextComponent.put(propRule, tf);
			pMain.add(p);
		}
		return pMain;
	}

	private void createRule() {
		Category sel = getSelectedCategory();
		if (sel == null) {
			JOptionPane.showMessageDialog(this, I18.tLabel("FirstSelectCategory"), I18.tLabel("AssignCategory"), JOptionPane.WARNING_MESSAGE);
			return;
		}
		Rule rule = new Rule();
		for (Entry<String, JTextComponent> entry : mapProp2TextComponent.entrySet()) {
			rule.setString(entry.getKey(), entry.getValue().getText().trim());
		}
		//
		view.getModel().addRule(sel, rule);
		// Refresh
		view.showUI();
	}

}
