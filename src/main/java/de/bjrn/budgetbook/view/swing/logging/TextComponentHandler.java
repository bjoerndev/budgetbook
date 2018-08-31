package de.bjrn.budgetbook.view.swing.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.JTextArea;

final class TextComponentHandler extends Handler {
	private final JTextArea text;

	TextComponentHandler(JTextArea text) {
		this.text = text;
	}

	@Override
	public void publish(LogRecord record) {
		if (isLoggable(record)) {
			synchronized (text) {
				text.append(getFormatter().format(record));
			}
		}
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}
}