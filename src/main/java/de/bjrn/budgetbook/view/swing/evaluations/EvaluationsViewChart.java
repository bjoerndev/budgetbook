package de.bjrn.budgetbook.view.swing.evaluations;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.data.general.Dataset;

import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.model.TimeWindow;
import de.bjrn.budgetbook.model.transaction.TX;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;

public abstract class EvaluationsViewChart extends EvaluationsView {
	private static final long serialVersionUID = 1L;
	
	private boolean recursive = true;

	public EvaluationsViewChart(BBViewEvaluations view, List<AccountTransaction> txs, EvaluationConfig config) {
		super(view, txs, config);
	}

	protected abstract Dataset createDataset();
	
	protected abstract JFreeChart createChart();

	@Override
	protected void initUI() {
		removeAll();
		add(decorateInfo(getUpPanel()), BorderLayout.NORTH);
		if (parentCategory != null && (
				!recursive
				|| getCategoriesRecursive(parentCategory).size() < 2
				)) {
			add(getTable(parentCategory, config.getTimeWindow(), recursive), BorderLayout.CENTER);
		} else {
			ChartPanel cp = new ChartPanel(createChart());
			cp.addChartMouseListener(getChartMouseListener());
			add(cp, BorderLayout.CENTER);
		}
		updateUI();
	}
	private JComponent decorateInfo(JComponent base) {
		AccountTransactionList txsLocal = new AccountTransactionList(txs);
		if (parentCategory != null) {
			txsLocal = txsLocal.subList(parentCategory, recursive, getModel());
		}
		JComponent info = getInfo(txsLocal, config.getTimeWindow());
		if (base == null) return info;
		JPanel p = new JPanel(new BorderLayout());
		p.add(base, BorderLayout.CENTER);
		p.add(info, BorderLayout.EAST);
		return p;
	}

	private JComponent getUpPanel() {
		if (parentCategory == null) {
			return null;
		}
		final Category cat = parentCategory;
		JButton b = new JButton(getName(parentCategory));
		b.addActionListener(new TxActionListener(e -> showParent(cat)));
		return b;
	}

	private void showParent(Category cat) {
		if (recursive) {
			parentCategory = getModel().getCategory(cat.getParentId());
		} else {
			recursive = true;
		}
		initUI();
	}
	
	private ChartMouseListener getChartMouseListener() {
		return new ChartMouseListener() {
			@Override
			public void chartMouseClicked(ChartMouseEvent event) {
				TX.start();
				try {
					ChartEntity e = event.getEntity();
					if (e instanceof PieSectionEntity) {
						PieSectionEntity entity = (PieSectionEntity) e;
						String key = entity.getSectionKey().toString();
						Category sub = getCategoryByKey(key);
						if (sub == null) {
							if (key.startsWith("<")) {
								String keyUndecorated = key.substring(1, key.length() - 1);
								if (parentCategory != null && parentCategory.getName().equals(keyUndecorated)) {
									showTableOverlay(parentCategory, config.getTimeWindow(), false);
								}
							}
						} else if (getCategoriesRecursive(sub).size() > 1) {
							parentCategory = sub;
							initUI();
						} else {
							showTableOverlay(sub, config.getTimeWindow(), true);
						}
					} else if (e instanceof CategoryItemEntity) {
						CategoryItemEntity entity = (CategoryItemEntity) e;
						Category sub = getCategoryByKey(entity.getRowKey().toString());
						showTableOverlay(sub, (TimeWindow) entity.getColumnKey(), true);
					}
					TX.commit();
				} catch (RuntimeException e) {
					TX.abort();
					throw e;
				}
			}
			@Override
			public void chartMouseMoved(ChartMouseEvent event) {
			}
		};
	}

	protected void showTableOverlay(Category cat, TimeWindow tw, boolean recursive) {
		parentCategory = cat;
		this.recursive = recursive;
		initUI();
	}
	
}
