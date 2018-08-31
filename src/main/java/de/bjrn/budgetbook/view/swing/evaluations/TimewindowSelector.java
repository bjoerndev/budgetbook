package de.bjrn.budgetbook.view.swing.evaluations;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.bjrn.budgetbook.model.TimeWindow;
import de.bjrn.budgetbook.view.i18.I18;

public class TimewindowSelector extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
	JComboBox<TimeWindow> cbType;
	JPanel context;
	
	public TimewindowSelector() {
		cbType = new JComboBox<TimeWindow>(getTimeTypes());
		cbType.setSelectedIndex(5);
		add(cbType);
		context = new JPanel();
		add(context);
	}
	
	public void addActionListener(ActionListener listener) {
		cbType.addActionListener(listener);
	}

	private Vector<TimeWindow> getTimeTypes() {
		Vector<TimeWindow> types = new Vector<TimeWindow>();
		// All
		types.add(new TimeWindow("*", null, null));
		// Years
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		for (int y = year; y > year - 3; y--) {
			types.add(new TimeWindow(Integer.toString(y), LocalDate.of(y, 1, 1), LocalDate.of(y+1, 1, 1)));
		}
		// Last x Month
		for (int num : new int[] {3, 6, 12}) {
			LocalDate end = LocalDate.of(year, now.getMonthValue(), 1);
			LocalDate start = end.minus(num, ChronoUnit.MONTHS);
			String title = I18.tLabel("LastXXXMonth").replace("XXX", Integer.toString(num));
			types.add(new TimeWindow(title, start, end));
		}
		
		// Months
		DateTimeFormatter mothFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
		for (int m = 0; m < 5; m++) {
			LocalDate month = LocalDate.of(year, now.getMonthValue(), 1);
			month = month.minus(m, ChronoUnit.MONTHS);
			types.add(new TimeWindow(month.format(mothFormatter), month, month.plus(1, ChronoUnit.MONTHS)));
		}
		// Optional: Add more types
		return types;
	}
	
	public TimeWindow getSelected() {
		return (TimeWindow) cbType.getSelectedItem();
	}
	
	public LocalDate getStart() {
		TimeWindow tw = getSelected();
		return tw == null ? null : tw.getStart();
	}

	public LocalDate getEnd() {
		TimeWindow tw = getSelected();
		return tw == null ? null : tw.getEnd();
	}

}
