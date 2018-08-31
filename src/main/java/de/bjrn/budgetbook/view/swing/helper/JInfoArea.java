package de.bjrn.budgetbook.view.swing.helper;

import javax.swing.JTextArea;

public class JInfoArea extends JTextArea {
	private static final long serialVersionUID = 1L;

	public JInfoArea(String message) {
		super(message);
		setLineWrap(true);
		setWrapStyleWord(true);
		setEditable(false);
	}
}
