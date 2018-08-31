package de.bjrn.budgetbook.view.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import de.bjrn.budgetbook.logic.BBLogic;

public class ShutdownListener extends WindowAdapter {

	BBLogic logic;

	public ShutdownListener(BBLogic logic) {
		this.logic = logic;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// close backend
		if (logic != null) {
			logic.close();
			logic = null;
		}
		
		// close UI
		e.getWindow().dispose();
		
		// Exit java process
		System.exit(0);
	}

}
