package de.bjrn.budgetbook.view.swing.evaluations;

import java.awt.Font;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;

public class EvaluationsViewCategoryPie extends EvaluationsViewChart {
	private static final long serialVersionUID = 1L;

	public EvaluationsViewCategoryPie(BBViewEvaluations view, List<AccountTransaction> txs, EvaluationConfig config) {
		super(view, txs, config);
	}

	@Override
	protected PieDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Category cat : getCategories()) {
			Double val = getValue(cat, true);
			if (val != null && val != 0 && !cat.equals(parentCategory)) {
				dataset.setValue(getName(cat), val);
			}
		}
		if (!config.isDetails() && parentCategory != null && !parentCategory.isIgnore()) {
			Double val = getValue(parentCategory, false);
			if (val != 0) {
				dataset.setValue("<" + getName(parentCategory) + ">", val);
			}
		}
		return dataset;
	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createPieChart(null, // chart title
				createDataset(), // data
				true, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		plot.setNoDataMessage(I18.tLabel("NoDataAvailable"));
		plot.setCircular(false);
		plot.setLabelGap(0.02);
		
		for (Category cat : getCategories()) {
			plot.setSectionPaint(getName(cat), cat.getColor());
		}
		if (!config.isDetails() && parentCategory != null && !parentCategory.isIgnore()) {
			Double val = getValue(parentCategory, false);
			if (val != 0) {
				plot.setSectionPaint("<" + getName(parentCategory) + ">", parentCategory.getColor());
			}
		}
		
		return chart;
	}

}
