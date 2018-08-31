package de.bjrn.budgetbook.view.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.kordamp.ikonli.websymbols.Websymbols;

import de.bjrn.budgetbook.model.Access;
import de.bjrn.budgetbook.model.AccessType;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.swing.helper.JBBButton;
import de.bjrn.budgetbook.view.swing.helper.JForm;

public class BBViewEditAccess extends BBViewAbstract {
	private static final long serialVersionUID = 1L;
	
	JTextField descField, blzField, usernameField;
	JPasswordField pinField;
	
	Access access;

	public BBViewEditAccess(BBViewMain main) {
		super(main);
		showUI();
	}
	
	public BBViewEditAccess(BBViewMain main, Access accessToEdit) {
		super(main);
		access = accessToEdit;
		showUI();
	}

	@Override
	public void showUI() {
		removeAll();
		initFields();
		String[] labels = new String[] {"Beschreibung (optional)", "Bankleitzahl", "Benutzername / Kontonummer", "Pin / Zugangscode"};
		Component[] fields = new Component[] {descField, blzField, usernameField, pinField};
		JButton[] buttons = new JButton[] {createOkButton()};
		add(new JForm(labels, fields, buttons));
	}

	private void initFields() {
		descField = new JTextField(10);
		descField.requestFocus();
		blzField = new JTextField(10);
		usernameField = new JTextField(10);
		pinField = new JPasswordField();
		if (access != null) {
			blzField.setText(access.getBlz());
			usernameField.setText(access.getUserName());
			pinField.setText(access.getPin());
		}
	}

	private JButton createOkButton() {
		JBBButton b = new JBBButton(access == null ? "Ergänzen" : "Übernehmen", Websymbols.ADDUSER, Color.BLUE);
		b.addActionListener(new TxActionListener(e -> saveAccess()));
		return b;
	}

	protected void saveAccess() {
		Access current = access == null ? new Access() : access;
		current.setDesc(descField.getText());
		current.setBlz(blzField.getText());
		current.setUserName(usernameField.getText());
		current.setPin(String.valueOf(pinField.getPassword()));
		current.setAccessType(AccessType.HbciPinTan);
		current.saveIt();
		getView().setContext(new BBViewAccesses(getView()));
	}

}
