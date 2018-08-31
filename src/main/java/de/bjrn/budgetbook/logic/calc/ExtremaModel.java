package de.bjrn.budgetbook.logic.calc;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.model.TimeWindow;

public class ExtremaModel {
	
	List<ExtremaVo> extremas;

	public ExtremaModel(TimeWindow tw, AccountTransactionList txs, List<Category> categories, boolean isDetails, BBModel model) {
		if (tw.getStart() == null) {
			tw.setStart(txs.getDateMin());
		}
		if (tw.getEnd() == null) {
			tw.setEnd(txs.getDateMax());
		}

		extremas = new Vector<ExtremaVo>();
		for (Category category : categories) {
			init(tw, txs.subList(category, !isDetails, model), category);
		}
		sort();
	}

	private void sort() {
		Collections.sort(extremas, new Comparator<ExtremaVo>() {
			@Override
			public int compare(ExtremaVo o1, ExtremaVo o2) {
				return Long.compare(o1.getDiff(), o2.getDiff());
			}
		});
	}

	private void init(TimeWindow tw, AccountTransactionList txs, Category category) {
		LocalDate start = tw.getStart();
		LocalDate end = tw.getEnd();
		LocalDate month = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
		
		long sum = 0;
		for (LocalDate curr = month; curr.isBefore(end); curr = curr.plus(1, ChronoUnit.MONTHS)) {
			sum += txs.subList(curr, curr.plus(1, ChronoUnit.MONTHS)).getAmount();
		}
		long average = sum / ChronoUnit.MONTHS.between(start, end);
		for (LocalDate curr = month; curr.isBefore(end); curr = curr.plus(1, ChronoUnit.MONTHS)) {
			long amount = txs.subList(curr, curr.plus(1, ChronoUnit.MONTHS)).getAmount();
			if (amount != 0 || average != 0) {
				extremas.add(new ExtremaVo(curr, curr.plus(1, ChronoUnit.MONTHS), amount, average, category));
			}
		}
	}
	
	public List<ExtremaVo> getExtremas() {
		return extremas;
	}
	
}
