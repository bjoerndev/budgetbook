package de.bjrn.budgetbook.view.swing.evaluations;

import de.bjrn.budgetbook.logic.Utils;
import de.bjrn.budgetbook.model.*;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;
import de.bjrn.budgetbook.view.swing.tables.AccountTransactionTableModel;
import de.bjrn.budgetbook.view.swing.tables.JTableFilter;
import de.bjrn.budgetbook.view.swing.tables.JTransactionTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class EvaluationsView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	BBViewEvaluations view;
	List<AccountTransaction> txs;
	Category parentCategory;
	Map<Long, Set<Long>> category2childs;
	EvaluationConfig config;
	
	public EvaluationsView(BBViewEvaluations view, List<AccountTransaction> txs, EvaluationConfig config) {
		this.view = view;
		this.txs = txs;
		this.config = config;
		parentCategory = null;
		initCategoryMap();
		setLayout(new BorderLayout());
		initUI();
	}

	protected abstract void initUI();

	protected void showTable(Category cat, TimeWindow tw, boolean recursive) {
		JFrame frame = new JFrame(getName(cat) + " - " + tw.toString());
		frame.setContentPane(getTable(cat, tw, recursive));
		frame.pack();
		frame.setVisible(true);
	}

	protected JComponent getTable(Category cat, TimeWindow tw, boolean recursive) {
		AccountTransactionList txs = new AccountTransactionList(getModel().getAccountTransactionsByCategory(cat, recursive), tw.getStart(), tw.getEnd());
		txs.sortByDate(false);
		return getTable(txs);
	}
	
	protected JComponent getInfo(AccountTransactionList txs, TimeWindow tw) {
		txs = txs.subListNotIgnored(getModel());
		JPanel p = new JPanel();
		Long days = tw.getDays(txs);
		if (days == null) {
			return p;
		}
		double exactDay = txs.getAmount(config.isOutgoings()) / 100.0 / (double) days;
		double  eachDay   = Utils.round(exactDay, 2);
		double  eachMonth = Utils.round(exactDay * 30, 2);
		boolean demo = DemoMode.on;
		p.add(new JLabel(
				I18.tLabel("XXXeachMonth").replace("XXX", demo ? "?" : Double.toString(eachMonth)) 
				+ ", " 
				+ I18.tLabel("XXXeachDay").replace("XXX", demo ? "?" : Double.toString(eachDay))
				));
		return p;
	}

	private JComponent getTable(AccountTransactionList txs) {
		return new JTableFilter<AccountTransactionTableModel>(new JTransactionTable(new AccountTransactionTableModel(txs, AccountTransaction.PROPS4VIEW_NO_SALDO), getModel()));
	}

	protected Category getCategoryByKey(String key) {
		for (Category cat : getCategories()) {
			if (key.equals(getName(cat))) {
				return cat;
			}
		}
		return null;
	}

	private void initCategoryMap() {
		category2childs = new HashMap<Long, Set<Long>>();
		for (Category cat : getModel().getCategories()) {
			if (!cat.isIgnore()) {
				if (config.isDetails()) {
					// Flat
					Set<Long> set = new HashSet<Long>();
					set.add(cat.getLongId());
					category2childs.put(cat.getLongId(), set);
				} else {
					// Hierarchical
					category2childs.put(cat.getLongId(), getChildsRecursive(cat));
				}
			}
		}
	}

	protected String getName(Category cat) {
		return getName(cat, config.isDetails());
	}
	
	protected String getName(Category cat, boolean fullPath) {
		String name = cat.getName();
		if (fullPath) {
			Category parent = getModel().getCategory(cat.getParentId());
			while (parent != null) {
				name = parent.getName() + " / " + name;
				// Next
				parent = getModel().getCategory(parent.getParentId());
			}
		}
		return name;
	}

	private Set<Long> getChildsRecursive(Category cat) {
		Set<Long> set = new HashSet<Long>();
		if (!cat.isIgnore()) {
			set.add(cat.getLongId());
			for (Category sub : getModel().getCategories(cat)) {
				set.addAll(getChildsRecursive(sub));
			}
		}
		return set;
	}
	
	protected Set<Long> getCategoriesRecursive(Category cat) {
		return category2childs.get(cat.getLongId());
	}

	protected BBModel getModel() {
		return view.getModel();
	}
	
	protected List<Category> getCategories() {
		List<Category> cats = new ArrayList<>();
		for (Category cat : config.isDetails() ? getModel().getCategories() : getModel().getCategories(parentCategory)) {
			if (!cat.isIgnore()) {
				cats.add(cat);
			}
		}
		Collections.sort(cats, (Category o1, Category o2) -> getName(o1).compareTo(getName(o2)));
		return cats;
	}

	protected Double getValue(Category cat, boolean recursive) {
		Set<Long> cats;
		if (recursive) {
			cats = getCategoriesRecursive(cat);
		} else {
			cats = new HashSet<Long>();
			cats.add(cat.getLongId());
		}
		long v = 0;
		for (AccountTransaction tx : txs) {
			Long curr = tx.getCategoryManual();
			if (curr == null) {
				curr = tx.getCategoryAuto();
			}
			if (curr != null) {
				if (cats.contains(curr)) {
					v += tx.getValueAmount();
				}
			}
		}
		if (config.isOutgoings()) {
			if (v < 0) {
				v = -v;
			} else {
				v = 0;
			}
		} else {
			if (v < 0) v = 0;
		}
		return Utils.round(v / 100.0, 2);
	}

}
