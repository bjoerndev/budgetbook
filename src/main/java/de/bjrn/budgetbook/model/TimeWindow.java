package de.bjrn.budgetbook.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

public class TimeWindow implements Comparable<TimeWindow>{

	LocalDate start;
	LocalDate end;
	
	String title;
	
	public TimeWindow(String title, LocalDate start, LocalDate end) {
		this.title = title;
		this.start = start;
		this.end = end;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}

	@Override
	public int compareTo(TimeWindow other) {
		int c = start.compareTo(other.getStart());
		return c == 0 ? end.compareTo(other.getEnd()) : c;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof TimeWindow) {
			TimeWindow twOther = (TimeWindow) other;
			return ObjectUtils.equals(start, twOther.getStart()) && ObjectUtils.equals(end, twOther.getEnd());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(start, end);
	}

	/**
	 * @return null (if start or end is null), or number of days between start and end
	 */
	public Long getDays() {
		if (start == null || end == null) return null;
		return ChronoUnit.DAYS.between(start, end);
	}

	/**
	 * @param txs
	 * @return {@link #getDays()} if != null, otherwise days between date min and max in txs, otherwise (if min=null or max=null) returns null.
	 */
	public Long getDays(AccountTransactionList txs) {
		Long days = getDays();
		if (days != null) {
			return days;
		}
		LocalDate min = txs.getDateMin(), max = txs.getDateMax();
		if (min == null || max == null) {
			return null;
		}
		return ChronoUnit.DAYS.between(min, max);
	}
	
}
