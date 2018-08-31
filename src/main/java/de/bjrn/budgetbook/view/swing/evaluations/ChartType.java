package de.bjrn.budgetbook.view.swing.evaluations;

import de.bjrn.budgetbook.view.i18.I18;

public enum ChartType {

	categoryPie(true, true), categoryMonthBar(true, true), categoryMonth(true, true), sumMonth(false, false), extrema(true, false);
	
	ChartType(boolean optionDetails, boolean optionIncome) {
		this.optionDetails = optionDetails;
		this.optionIncome = optionIncome;
	}
	
	boolean optionDetails, optionIncome;
	
	@Override
	public String toString() {
		return I18.t("charttype." + name());
	}

	public boolean hasOptionDetails() {
		return optionDetails;
	}

	public boolean hasOptionIncome() {
		return optionIncome;
	}
}
