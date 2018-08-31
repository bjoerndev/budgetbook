package de.bjrn.budgetbook.view.swing.evaluations;

import java.awt.Component;

import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;

public class EvaluationsViewFactory {

	public static Component create(BBViewEvaluations view, AccountTransactionList txs,
			EvaluationConfig config) {
		switch(config.getChartType()) {
		case categoryPie: return new EvaluationsViewCategoryPie(view, txs, config);
		case categoryMonth: return new EvaluationsViewCategoryMonthLine(view, txs, config);
		case categoryMonthBar: return new EvaluationsViewCategoryMonthBar(view, txs, config);
		case sumMonth: return new EvaluationsViewSumMonth(view, txs, config);
		case extrema: return new EvaluationsViewExtrema(view, txs, config);
		}
		throw new RuntimeException("Unsupported chart type: " + config.getChartType().name());
	}

}
