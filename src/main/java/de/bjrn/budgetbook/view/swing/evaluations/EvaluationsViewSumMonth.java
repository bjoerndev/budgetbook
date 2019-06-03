package de.bjrn.budgetbook.view.swing.evaluations;

import de.bjrn.budgetbook.logic.Utils;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EvaluationsViewSumMonth extends EvaluationsViewPerMonth {
	private static final long serialVersionUID = 1L;
	
	enum TYPE {
		Null(Color.BLACK),
		Sum(Color.BLUE), 
		Income(Color.GREEN), 
		Outgoings(Color.RED);
		
		Color color;
		
		TYPE(Color color) {
			this.color = color;
		}
		Color getColor() {
			return color;
		}
	};
	
	AccountTransactionList txsClean;
	
	public EvaluationsViewSumMonth(BBViewEvaluations view, List<AccountTransaction> txs, EvaluationConfig config) {
		super(view, txs, config);
	}

	private AccountTransactionList getTxsClean() {
		if (txsClean == null) {
			txsClean = new AccountTransactionList();
			for (AccountTransaction tx : txs) {
				Category cat = getModel().getCategory(tx.getCategory());
				if (cat == null || !cat.isIgnore()) {
					txsClean.add(tx);
				}
			}
		}
		return txsClean;
	}

	@Override
	protected DefaultXYDataset createDataset() {
		DefaultXYDataset ds = new DefaultXYDataset();
		for (TYPE type : TYPE.values()) {
			double[][] data = createDatasetData(type);
			if (data != null) {
		        ds.addSeries(I18.tLabel(type.name()), data);
			}
		}
        return ds;
	}

	@Override
	protected JFreeChart createChart() {
		JFreeChart chart = 
				ChartFactory.createTimeSeriesChart(null, I18.tLabel("Time"), I18.tLabel("Sum"), createDataset(), true, true, false);
		ValueAxis axisX = chart.getXYPlot().getDomainAxis();
		if (axisX instanceof DateAxis) {
			((DateAxis) axisX).setDateFormatOverride(getModel().getDateFormat());
		}
		
		XYItemRenderer renderer = chart.getXYPlot().getRenderer();
		for (int i = 0; i < TYPE.values().length; i++) {
			TYPE type = TYPE.values()[i];
			renderer.setSeriesPaint(i, type.getColor());
		}
		
		return chart;
	}

	private double[][] createDatasetData(TYPE type) {
		if (TYPE.Null.equals(type)) {
			return createDatasetDataZero();
		}
		if (getTxsClean().isEmpty()) {
			return null;
		}
		double[][] data = new double[2][getMonths()*2];
		boolean content = false;
		for (int m = 0; m < getMonths(); m++) {
			LocalDate start = getBase().plusMonths(m);
			LocalDate end = start.plusMonths(1);
			data[0][m*2] = Utils.getMillis(start);
			data[0][m*2+1] = Utils.getMillis(end);
			AccountTransactionList txsFiltered = new AccountTransactionList(getTxsClean(), start, end);
			long amount = 0;
			switch(type) {
			case Sum: amount = txsFiltered.getAmount(); break;
			case Income: amount = txsFiltered.getAmount(false); break;
			case Outgoings: amount = -txsFiltered.getAmount(true); break;
			case Null: throw new RuntimeException("Should be already handled");
			}
			content |= amount != 0;
			double val = Utils.round(amount / 100.0, 2);
			data[1][m*2] = val;
			data[1][m*2+1] = val;
		}
		return content ? data : null;
	}

	private double[][] createDatasetDataZero() {
		double[][] data = new double[2][2];
		for (int i=0; i<2; i++) {
			data[0][i] = Utils.getMillis(i == 0 ? getBase() : getBase().plusMonths(getMonths()));
			data[1][i] = 0;
		}
		return data;
	}

}
