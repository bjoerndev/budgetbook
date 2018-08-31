package de.bjrn.budgetbook.view.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import de.bjrn.budgetbook.view.i18.I18;

public class Splash extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private final static String SPLASH = "de/bjrn/budgetbook/images/splash.jpg";

	public Splash() {
		super(I18.tLabel("Splash.Title"));
		setIcon(new ImageIcon(Splash.class.getClassLoader().getResource(SPLASH)));
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.CENTER);
		setFont(new Font("Serif", Font.BOLD, 30));
		setForeground(Color.WHITE);
	}
}
