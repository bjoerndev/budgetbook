package de.bjrn.budgetbook;

import javax.swing.JOptionPane;

import de.bjrn.budgetbook.logic.BBConfig;
import de.bjrn.budgetbook.logic.BBLogic;
import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.model.transaction.TX;
import de.bjrn.budgetbook.view.swing.BBView;
import de.bjrn.budgetbook.view.swing.open.OpenDialog;

/**
 * Launcher to initialize and startup BB as Swing application
 * @author bschaedlich
 */
public class BudgetBookSwing {
	
	public static void main(String[] args) {
		new BudgetBookSwing().startup();
	}
	
	private void startup() {
		if (BBConfig.INSTANCE().isAvailable()) {
			startupApp();
		} else {
			new OpenDialog();
		}
	}

	private void startupApp() {
		BBModel model = BBModel.INSTANCE();
		TX.start();
		try {
			BBLogic logic = new BBLogic(model);
			BBView ui = new BBView(logic);
			ui.show();
		} catch (Exception e) {
			e.printStackTrace();
			TX.abort();
			JOptionPane.showMessageDialog(null, "Error on startup: " + e.getLocalizedMessage());
		} finally {
			TX.commit();
		}
	}
	
}
