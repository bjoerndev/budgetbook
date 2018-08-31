package de.bjrn.budgetbook.view.swing.tables;

import java.util.List;

import de.bjrn.budgetbook.model.Rule;

public class RuleTableModel extends ModelTableModel<Rule> {

	public RuleTableModel(List<Rule> rules) {
		super(rules, Rule.PROPS4VIEW);
		setEditable(Rule.PROP_NAME, Rule.PROP_TEXT, Rule.PROP_USAGE);
	}
}
