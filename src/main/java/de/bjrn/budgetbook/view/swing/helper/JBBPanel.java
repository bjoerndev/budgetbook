package de.bjrn.budgetbook.view.swing.helper;

import javax.swing.JPanel;

import de.bjrn.budgetbook.logic.BBLogic;
import de.bjrn.budgetbook.view.swing.BBViewMain;

public class JBBPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	BBViewMain view;
	
	public JBBPanel(BBViewMain view) {
		this.view = view;
	}
	
	public BBViewMain getView() {
		return view;
	}
	
	public BBLogic getLogic() {
		return view.getLogic();
	}

}
