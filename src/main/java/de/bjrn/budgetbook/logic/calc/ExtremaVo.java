package de.bjrn.budgetbook.logic.calc;

import java.time.LocalDate;

import de.bjrn.budgetbook.model.Category;

public class ExtremaVo {
	
	LocalDate start, end;
	long amount;
	long average;
	Category category;

	public ExtremaVo(LocalDate start, LocalDate end, long amount, long average, Category category) {
		this.start = start;
		this.end = end;
		this.amount = amount;
		this.average = average;
		this.category = category;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getAverage() {
		return average;
	}

	public void setAverage(long average) {
		this.average = average;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public long getDiff() {
		return amount - average;
	}

}
