package de.bjrn.budgetbook.view.swing.helper;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

public class JPanelNorth extends JPanel {
	private static final long serialVersionUID = 1L;

	public JPanelNorth(Component comp) {
		super(new BorderLayout());
		add(comp, BorderLayout.NORTH);
	}
}
