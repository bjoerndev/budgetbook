package de.bjrn.budgetbook.model;

import org.javalite.activejdbc.Model;

public class Rule extends Model {

	public final static String PROP_MIN_AMOUNT = "minAmount";
	public final static String PROP_MAX_AMOUNT = "maxAmount";
	public final static String PROP_NAME = "name";
	public final static String PROP_IBAN = "iban";
	public final static String PROP_BIC = "bic";
	public final static String PROP_CUSTOMERREF = "customerref";
	public final static String PROP_INSTREF = "instref";
	public final static String PROP_TEXT = "text";
	public final static String PROP_USAGE = "usage";
	public static final String[] PROPS4VIEW = new String[] {Rule.PROP_NAME, Rule.PROP_TEXT, Rule.PROP_USAGE};
	
	public Category getCategory() {
		return parent(Category.class, true);
	}
	
	public int getMinAmount() {
		return getInteger(PROP_MIN_AMOUNT);
	}

	public void setMinAmount(int value) {
		setInteger(PROP_MIN_AMOUNT, value);
	}
	
	public int getMaxAmount() {
		return getInteger(PROP_MAX_AMOUNT);
	}

	public void setMaxAmount(int value) {
		setInteger(PROP_MAX_AMOUNT, value);
	}
	
	public String getName() {
		return getString(PROP_NAME);
	}

	public void setName(String value) {
		setString(PROP_NAME, value);
	}

}
