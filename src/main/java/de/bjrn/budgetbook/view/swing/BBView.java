package de.bjrn.budgetbook.view.swing;

import de.bjrn.budgetbook.logic.BBLogic;

public class BBView {
	
	private BBLogic logic;

	public BBView(BBLogic logic) {
		this.logic = logic;
	}

	public void show() {
        // Init and show main frame
		BBViewMain frame = new BBViewMain(logic);
		frame.showMain();
	}

}
