package de.bjrn.budgetbook.view.swing.evaluations;

import java.time.LocalDate;

import de.bjrn.budgetbook.model.TimeWindow;

public class EvaluationConfig {

	boolean outgoings = true;
	ChartType chartType;
	boolean details = false;
	TimeWindow timeWindow;
	
	public boolean isOutgoings() {
		return outgoings;
	}
	public void setOutgoings(boolean outgoings) {
		this.outgoings = outgoings;
	}
	public ChartType getChartType() {
		return chartType;
	}
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}
	public boolean isDetails() {
		return details;
	}
	public void setDetails(boolean details) {
		this.details = details;
	}
	public LocalDate getStart() {
		return timeWindow == null ? null : timeWindow.getStart();
	}

	public LocalDate getEnd() {
		return timeWindow == null ? null : timeWindow.getEnd();
	}

	public void setTimeWindow(TimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}
	
	public TimeWindow getTimeWindow() {
		return timeWindow;
	}
	
}
