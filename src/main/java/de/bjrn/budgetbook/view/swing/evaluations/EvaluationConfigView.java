package de.bjrn.budgetbook.view.swing.evaluations;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bjrn.budgetbook.view.i18.I18;

public class EvaluationConfigView extends JPanel {
	private static final long serialVersionUID = 1L;

	TimewindowSelector ts;
	JComboBox<ChartType> cbType;
	JCheckBox cbDetails;
	JCheckBox cbIncomings;
	ActionListener updateCallback;
	
	public EvaluationConfigView(ActionListener updateCallback) {
		this.updateCallback = updateCallback;
		initUI();
	}
	
	private void initUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(new JLabel(I18.tLabel("Timeslice")));
		ts = new TimewindowSelector();
		ts.addActionListener(e -> refreshUI());
		add(ts);
		
		add(new JLabel(I18.tLabel("Type")));
		cbType = new JComboBox<ChartType>(ChartType.values());
		cbType.addActionListener(e -> refreshUI());
		add(cbType);
		
		cbDetails = new JCheckBox(I18.tLabel("Details"));
		cbDetails.setToolTipText(I18.tLabel("Details.Desc"));
		cbDetails.addActionListener(e -> refreshUI());
		add(cbDetails);

		cbIncomings = new JCheckBox(I18.tLabel("Income"));
		cbIncomings.setToolTipText(I18.tLabel("Income.Desc"));
		cbIncomings.addActionListener(e -> refreshUI());
		add(cbIncomings);
	}
	
	private void refreshUI() {
		updateCallback.actionPerformed(null);
		ChartType type = getSelectedChartType();
		cbDetails.setVisible(type != null && type.hasOptionDetails());
		cbIncomings.setVisible(type != null && type.hasOptionIncome());
	}
	
	private ChartType getSelectedChartType() {
		return cbType.getItemAt(cbType.getSelectedIndex());
	}

	public EvaluationConfig getConfig() {
		EvaluationConfig cfg = new EvaluationConfig();
		cfg.setChartType(getSelectedChartType());
		cfg.setDetails(cbDetails.isSelected());
		cfg.setTimeWindow(ts.getSelected());
		cfg.setOutgoings(!cbIncomings.isSelected());
		return cfg;
	}
}
