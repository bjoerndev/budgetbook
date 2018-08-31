package de.bjrn.budgetbook.view.i18;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18 {

	private static I18 instance = new I18();
	
    Locale currentLocale;
    ResourceBundle messages;

	public static I18 getInstance() {
		return instance;
	}
	
	private I18() {
		try {
			initLocale(Locale.getDefault());
		} catch (Exception e) {
			initLocale(new Locale("de", "DE"));
		}
	}
	
	public void initLocale(Locale locale) {
        messages = ResourceBundle.getBundle("MessagesBundle", locale);
	}
	
	public String getString(String key) {
		if (!messages.containsKey(key)) {
			System.out.println("[WARNING] Unknown message key:" + key);
			return key;
		}
		return messages.getString(key);
	}
	
	public static String t(String key) {
		return getInstance().getString(key);
	}

	public static String tProperty(String key) {
		return t("property." + key);
	}

	public static String tLabel(String key) {
		return t("label." + key);
	}
	
}
