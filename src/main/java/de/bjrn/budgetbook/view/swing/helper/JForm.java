package de.bjrn.budgetbook.view.swing.helper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class JForm extends JPanel {
	private static final long serialVersionUID = 1L;

	public JForm(String[] labels, Component[] fields, JButton[] buttons) {
		setLayout(new BorderLayout());
		add(createField(labels, fields), BorderLayout.CENTER);
		add(createButtons(buttons), BorderLayout.SOUTH);
	}

	private Component createButtons(JButton[] buttons) {
		JPanel p = new JPanel(new GridLayout(0, 1));
		for (JButton b : buttons) {
			p.add(b);
		}
		return p;
	}

	private Component createField(String[] labels, Component[] fields) {
		int numPairs = labels.length;

		// Create and populate the panel.
		JPanel p = new JPanel(new SpringLayout());
		for (int i = 0; i < numPairs; i++) {
			Component field = fields[i];
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			l.setLabelFor(field);
			p.add(field);
		}

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(p, numPairs, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad
		return p;
	}
}
