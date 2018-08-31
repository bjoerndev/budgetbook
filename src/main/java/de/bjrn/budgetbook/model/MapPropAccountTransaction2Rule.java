package de.bjrn.budgetbook.model;

import java.util.HashMap;

public class MapPropAccountTransaction2Rule extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;

	public MapPropAccountTransaction2Rule() {
		put(AccountTransaction.PROP_OTHER_NAME, Rule.PROP_NAME);
		put(AccountTransaction.PROP_USAGE, Rule.PROP_USAGE);
		put(AccountTransaction.PROP_TEXT, Rule.PROP_TEXT);
	}
}
