package de.bjrn.budgetbook.view.swing.evaluations;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.bjrn.budgetbook.logic.calc.ExtremaModel;
import de.bjrn.budgetbook.logic.calc.ExtremaVo;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.AccountTransactionList;
import de.bjrn.budgetbook.model.TimeWindow;
import de.bjrn.budgetbook.model.transaction.TX;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.BBViewEvaluations;
import de.bjrn.budgetbook.view.swing.tables.JTableFilter;

public class EvaluationsViewExtrema extends EvaluationsView {
	private static final long serialVersionUID = 1L;
	
	final static DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
	
	List<ExtremaVo> exs;

	public EvaluationsViewExtrema(BBViewEvaluations view, List<AccountTransaction> txs, EvaluationConfig config) {
		super(view, txs, config);
	}
	
	@Override
	protected void initUI() {
		removeAll();
		add(createTable(), BorderLayout.CENTER);
		updateUI();
	}

	private Component createTable() {
		JTable table = new JTable(createDataModel());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        int row = table.getSelectedRow();
		        if (mouseEvent.getClickCount() == 2 && row != -1) {
		        	showRow(table.convertRowIndexToModel(row));
		        }
		    }
		});
		return new JScrollPane(new JTableFilter<TableModel>(table));
	}

	protected void showRow(int row) {
		TX.start();
		try {
	    	ExtremaVo ex = exs.get(row);
	    	String title = monthFormatter.format(ex.getStart());
	        showTable(ex.getCategory(), new TimeWindow(title, ex.getStart(), ex.getEnd()), true);
		} finally {
			TX.abort();
		}
	}

	private TableModel createDataModel() {
		String[] columnNames = new String[] {"Diff", I18.tLabel("Month"), I18.tLabel("Category"), I18.tLabel("Amount"), I18.tLabel("Average")};
		ExtremaModel em = new ExtremaModel(config.getTimeWindow(), new AccountTransactionList(txs), getCategories(), config.isDetails(), getModel());
		exs = em.getExtremas();
		Object[][] data = new Object[exs.size()][columnNames.length];
		for (int row=0; row<exs.size(); row++) {
			ExtremaVo ex = exs.get(row);
			data[row][0] = ex.getDiff() / 100.0;
			data[row][1] = ex.getStart().format(monthFormatter);
			data[row][2] = getName(ex.getCategory());
			data[row][3] = ex.getAmount() / 100.0;
			data[row][4] = ex.getAverage() / 100.0;
		}
		return new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 1L;
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
			@Override
			public Class<?> getColumnClass(int col) {
				switch(col) {
				case 0:
				case 3:
				case 4:
					return Double.class;
				case 1:
				case 2:
					return String.class;
				default:
					return super.getColumnClass(col);
				}
			}
		};
	}

}
