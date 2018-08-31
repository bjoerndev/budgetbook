package de.bjrn.budgetbook.view.swing.helper;

import java.awt.Color;

import javax.swing.JButton;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.swing.FontIcon;

public class JBBButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	final static int BUTTON_ICON_SIZE = 16;
	
	public JBBButton(String text, Ikon icon, Color color) {
		super(text, FontIcon.of(icon, BUTTON_ICON_SIZE, color));
	}

}
