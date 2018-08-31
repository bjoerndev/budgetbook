package de.bjrn.budgetbook.model;

import java.awt.Color;
import java.awt.Paint;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.javalite.activejdbc.Model;

public class Category extends Model {

	public final static String PROP_NAME = "name";
	public final static String PROP_DESCRIPTION = "description";
	public final static String PROP_PARENT = "parent";
	public final static String PROP_CATEGORY_AUTO = "categoryAuto";
	public final static String PROP_CATEGORY_MANUAL = "categoryManual";
	public final static String PROP_IGNORE = "ignore";
	
	public String getName() {
		return getString(PROP_NAME);
	}
	
	public void setName(String value) {
		setString(PROP_NAME, value);
	}
	
	public String getDescription() {
		return getString(PROP_DESCRIPTION);
	}
	
	public void setDescription(String value) {
		setString(PROP_DESCRIPTION, value);
	}
	
	public Boolean isIgnore() {
		return getBoolean(PROP_IGNORE);
	}
	
	public void setIgnore(Boolean value) {
		setBoolean(PROP_IGNORE, value);
	}
	
	public List<Rule> getRules() {
		List<Rule> rules = getAll(Rule.class);
		Collections.sort(rules, new Comparator<Rule>() {
			@Override
			public int compare(Rule o1, Rule o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return rules;
	}
	
	public Long getParentId() {
		return getLong(PROP_PARENT);
	}

	@Override
	public String toString() {
		return getName();
	}

	public Paint getColor() {
		int val = getName().hashCode();
		return Color.decode(Integer.toString(val));
	}
}
