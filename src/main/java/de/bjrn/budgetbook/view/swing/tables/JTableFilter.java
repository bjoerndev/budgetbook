package de.bjrn.budgetbook.view.swing.tables;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.bjrn.budgetbook.view.i18.I18;

public class JTableFilter<TABLE_MODEL extends TableModel> extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JTable table;
	JTextField tfFilter;
	private TableRowSorter<TABLE_MODEL> sorter;
	
	@SuppressWarnings("unchecked")
	public JTableFilter(JTable table) {
		this.table = table;
		sorter = new TableRowSorter<TABLE_MODEL>((TABLE_MODEL)table.getModel());
		table.setRowSorter(sorter);
		setLayout(new BorderLayout());
		add(getSearch(), BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private Component getSearch() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(I18.tLabel("Filter")+":"), BorderLayout.WEST);
		tfFilter = new JTextField(20);
		tfFilter.requestFocus();
		listen(tfFilter);
		p.add(tfFilter, BorderLayout.CENTER);
		return p;
	}

	private void listen(JTextField tf) {
		tf.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				newFilter();
			}
			public void insertUpdate(DocumentEvent e) {
				newFilter();
			}
			public void removeUpdate(DocumentEvent e) {
				newFilter();
			}
		});
	}

    private void newFilter() {
        RowFilter<TABLE_MODEL, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)" + tfFilter.getText());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

}
