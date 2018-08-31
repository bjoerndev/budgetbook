package de.bjrn.budgetbook.logic;

import java.io.File;

public class BBConfig {

	private static BBConfig instance;
	
	File file;
	char[] pwd;
	
	private BBConfig() {
	}

	public static synchronized BBConfig INSTANCE() {
		if (instance == null) {
			instance = new BBConfig();
		}
		return instance;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public char[] getPwd() {
		return pwd;
	}

	public void setPwd(char[] pwd) {
		this.pwd = pwd;
	}
	
	public boolean isAvailable() {
		return file != null;
	}
	
}
