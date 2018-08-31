package de.bjrn.budgetbook.view.swing.evaluations;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;

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
	
	protected int getMonth() {
		if (!initialized) {
			initData();
		}
		return months;
	}

}
