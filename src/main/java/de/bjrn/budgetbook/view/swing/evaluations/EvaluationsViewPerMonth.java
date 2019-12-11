package de.bjrn.budgetbook.view.swing.evaluations;

import de.bjrn.budgetbook.logic.Utils;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public abstract class EvaluationsViewPerMonth extends EvaluationsViewChart {
	private static final long serialVersionUID = 1L;
	
	private LocalDate min, max, base;
	private int months;
	private boolean initialized = false;

	public EvaluationsViewPerMonth(BBViewEvaluations view, List<AccountTransaction> txs, EvaluationConfig config) {
		super(view, txs, config);
		initData();
	}

	private void initData() {
		initialized = true;
		AccountTransactionList list = new AccountTransactionList(txs);
		min = list.getDateMin();
		max = list.getDateMax();
		base = min == null ? null : min.withDayOfMonth(1);
		months = base == null ? 0 : (int) ChronoUnit.MONTHS.between(base, max) + 1;
	}
	
	protected LocalDate getMin() {
		if (!initialized) {
			initData();
		}
		return min;
	}
	
	protected LocalDate getMax() {
		if (!initialized) {
			initData();
		}
		return max;
	}
	
	protected LocalDate getBase() {
		if (!initialized) {
			initData();
		}
		return base;
	}
	
	protected int getMonths() {
		if (!initialized) {
			initData();
		}
		return months;
	}

	@Override
	protected List<Category> getCategories() {
		List<Category> cats = super.getCategories();
		Map<Category, Double> variability = new HashMap<>();
		for (Category cat : cats) {
			variability.put(cat, getVariability(cat, false));
		}
		Collections.sort(cats, Comparator.comparingDouble(variability::get));
		return cats;
	}

	private double getVariability(Category cat, boolean relative) {
		AccountTransactionList txsFiltered = new AccountTransactionList();
		Set<Long> catsRecursive = cat.equals(parentCategory) ? Set.of(cat.getLongId()) : getCategoriesRecursive(cat);
		for (AccountTransaction tx : txs) {
			if (catsRecursive.contains(tx.getCategory())) {
				txsFiltered.add(tx);
			}
		}
		if (txsFiltered.isEmpty()) {
			return 0;
		}
		double sum = 0;
		List<Double> values = new ArrayList<>();
		for (int m = 0; m < getMonths(); m++) {
			LocalDate start = getBase().plusMonths(m);
			LocalDate end = start.plusMonths(1);
			long amount = new AccountTransactionList(txsFiltered, start, end).getAmount(config.isOutgoings());
			double val = Utils.round(amount / 100.0, 2);
			sum += val;
			values.add(val);
		}
		if (sum == 0) {
			return 0;
		}
		double variability = 0;
		double average = sum / values.size();
		for (Double value : values) {
			variability += Math.abs(value - average) / (relative ? average : 1.0);
		}
		return variability;
	}

}
