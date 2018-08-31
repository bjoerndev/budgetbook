package de.bjrn.budgetbook.view.swing;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.bjrn.budgetbook.logic.BBLogic;
import de.bjrn.budgetbook.model.BBModel;

public abstract class BBViewAbstract extends JPanel {
	private static final long serialVersionUID = 1L;
	
	BBViewMain main;

	public BBViewAbstract(BBViewMain main) {
		this.main = main;
	}
	
	public BBLogic getLogic() {
		return main.getLogic();
	}
	
	public BBModel getModel() {
		return main.getLogic().getModel();
	}
	
	public BBViewMain getView() {
		return main;
	}
	
	public void setContext(JComponent ctx) {
		main.setContext(ctx);
	}
	
	protected void showInfo(String title, String msg) {
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void showWarning(String title, String msg) {
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
	}
	
	protected void showError(String title, String msg) {
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
	}
	
	public abstract void showUI();

}
