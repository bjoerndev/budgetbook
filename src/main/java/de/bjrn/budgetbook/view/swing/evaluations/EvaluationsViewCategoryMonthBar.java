package de.bjrn.budgetbook.view.swing.evaluations;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;

public class EvaluationsViewCategoryMonthBar extends EvaluationsViewCategoryMonth {

	public EvaluationsViewCategoryMonthBar(BBViewEvaluations view, List<AccountTransaction> txs,
			EvaluationConfig config) {
		super(view, txs, config);
	}

	@Override
	protected JFreeChart createChart(String title, String domainAxisLabel, String rangeAxisLabel,
			CategoryDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
		return ChartFactory.createStackedBarChart(title, domainAxisLabel, rangeAxisLabel, dataset, orientation, legend, tooltips, urls);
	}

}
