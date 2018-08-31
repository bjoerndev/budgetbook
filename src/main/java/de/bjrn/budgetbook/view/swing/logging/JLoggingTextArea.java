package de.bjrn.budgetbook.view.swing.logging;

import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.JTextArea;

public class JLoggingTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;

	Logger logger;
	Handler handler;

	public JLoggingTextArea(Logger logger) {
		this.logger = logger;
		handler = new TextComponentHandler(this);
		logger.addHandler(handler);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		for (Handler hh : logger.getHandlers())
			if (hh == handler)
				return;
		logger.addHandler(handler);
	}

	@Override
	public void removeNotify() {
		super.removeNotify();
		logger.removeHandler(handler);
	}
}
