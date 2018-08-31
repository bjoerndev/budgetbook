package de.bjrn.budgetbook.view.swing.helper;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JButton;

import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.swing.BBViewAbstract;

public class JNaviButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	ContextUpdater uiCallback;
	BBViewAbstract targetUI;

	public JNaviButton(String text, Icon icon, ContextUpdater uiCallback, BBViewAbstract targetUI) {
		super(text, icon);
		this.uiCallback = uiCallback;
		this.targetUI = targetUI;
		addActionListener(new TxActionListener(e -> exec()));
	}
	
	private void exec() {
		targetUI.showUI();
		uiCallback.setContext(targetUI);
		select();
	}

	public void select() {
		setBackground(Color.YELLOW);
	}
	
	public void unselect() {
		setBackground(null);
	}
	
}
