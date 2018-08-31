package de.bjrn.budgetbook.model.transaction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TxActionListener implements ActionListener {

	ActionListener action;
	
	public TxActionListener(ActionListener action) {
		this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TX.start();
		try {
			action.actionPerformed(e);
			TX.commit();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			TX.abort();
			throw ex;
		}	
	}

}
