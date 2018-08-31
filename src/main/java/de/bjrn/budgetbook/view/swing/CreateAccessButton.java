package de.bjrn.budgetbook.view.swing;

import java.awt.Color;

import org.kordamp.ikonli.websymbols.Websymbols;

import de.bjrn.budgetbook.view.swing.helper.JBBButton;

public class CreateAccessButton extends JBBButton {
	private static final long serialVersionUID = 1L;

	BBViewMain view;
	
	public CreateAccessButton(BBViewMain view) {
		super("Ergänzen", Websymbols.ADDUSER, Color.BLACK);
		this.view = view;
		setToolTipText("Neuen Zugang ergänzen");
		addActionListener( e-> showCreateAccessForm());
	}

	protected void showCreateAccessForm() {
		view.setContext(new BBViewEditAccess(view));
	}
}
