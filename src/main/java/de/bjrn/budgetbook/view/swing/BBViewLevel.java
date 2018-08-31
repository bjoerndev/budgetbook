package de.bjrn.budgetbook.view.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import de.bjrn.budgetbook.model.Account;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.DemoMode;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.i18.I18;

public class BBViewLevel extends BBViewAbstract {
	private static final long serialVersionUID = 1L;
	
	JCheckBox cbSum;
	Map<Account, JCheckBox> mapAccountCb;
	
	public BBViewLevel(BBViewMain main) {
		super(main);
		mapAccountCb = new HashMap<Account, JCheckBox>();
		cbSum = new JCheckBox(I18.tLabel("Sum"), true);
		cbSum.addActionListener(new TxActionListener(e -> showUI()));
	}

	@Override
	public void showUI() {
		removeAll();
		setLayout(new BorderLayout());
		add(createConfig(), BorderLayout.NORTH);
		add(createChart(), BorderLayout.CENTER);
		updateUI();
	}
	
	private Component createConfig() {
		JPanel p = new JPanel();
		p.add(cbSum);
		for (Account account : getModel().getAccounts()) {
			JCheckBox cb = mapAccountCb.get(account);
			if (cb == null) {
				cb = mapAccountCb.get(account) == null ? new JCheckBox(account.toStringShort(), true) : mapAccountCb.get(account);
				cb.setToolTipText(account.toString());
				cb.addActionListener(new TxActionListener(e -> showUI()));
				mapAccountCb.put(account, cb);
			}
			p.add(cb);
		}
		return p;
	}

	private Component createChart() {
		String title = null;
		JFreeChart chart = 
				ChartFactory.createTimeSeriesChart(title, I18.tLabel("Time"), I18.tLabel("Amount"), createDataset(), true, true, false);
		ValueAxis axisX = chart.getXYPlot().getDomainAxis();
		if (axisX instanceof DateAxis) {
			((DateAxis) axisX).setDateFormatOverride(getModel().getDateFormat());
		}
		if (DemoMode.on) {
			chart.getXYPlot().getRangeAxis().setVisible(false);
		}
		ChartPanel cp = new ChartPanel(chart);
		return cp;
	}

	private XYDataset createDataset() {
		Map<Account, List<AccountTransaction>> txMap = getModel().getAccountTransactionByAccount();
		prepareData(txMap);
		Date currentDate = new Date();
		DefaultXYDataset ds = new DefaultXYDataset();
		for (Entry<Account, List<AccountTransaction>> entry : txMap.entrySet()) {
			Account account = entry.getKey();
	        String label = account.toString();
	        ds.addSeries(label, createDatasetData(entry.getValue(), currentDate));
		}
        return ds;
	}

	private double[][] createDatasetData(List<AccountTransaction> txs, Date currentDate) {
		Collections.sort(txs, new Comparator<AccountTransaction>() {
			@Override
			public int compare(AccountTransaction o1, AccountTransaction o2) {
				return getDate(o1).compareTo(getDate(o2));
			}
		});
		double[][] data = new double[2][txs.size() * 2];
		for (int iTx=0; iTx < txs.size(); iTx++) {
			AccountTransaction tx = txs.get(iTx);
			// Current
			double saldo = ((double)getAmount(tx))/100.0;
			data[0][iTx*2] = getDate(tx).getTime();
			data[1][iTx*2] = saldo;
			// Until next
			Date nextDate = iTx == txs.size()-1 ? currentDate : getDate(txs.get(iTx + 1));
			data[0][iTx*2 + 1] = nextDate.getTime();
			data[1][iTx*2 + 1] = saldo;
		}
		return data;
	}

	private void prepareData(Map<Account, List<AccountTransaction>> txMap) {
		filterAccounts(txMap);
		enrichDataWithStart(txMap);
		if (cbSum.isSelected() && txMap.keySet().size() > 1) {
			enrichDataWithSum(txMap);
		}
	}

	private void filterAccounts(Map<Account, List<AccountTransaction>> txMap) {
		for (Entry<Account, JCheckBox> entry : mapAccountCb.entrySet()) {
			if (!entry.getValue().isSelected()) {
				txMap.remove(entry.getKey());
			}
		}
	}

	private void enrichDataWithSum(Map<Account, List<AccountTransaction>> txMap) {
		Map<Account, Map<Date, Long>> mapAccountDateAmount = new HashMap<Account, Map<Date, Long>>();
		for (Entry<Account, List<AccountTransaction>> entry : txMap.entrySet()) {
			Account account = entry.getKey();
			Map<Date, Long> mapDateAmount = new HashMap<Date, Long>();
			for (AccountTransaction tx : entry.getValue()) {
				mapDateAmount.put(getDate(tx), getAmount(tx));
			}
			mapAccountDateAmount.put(account, mapDateAmount);
		}
		
		List<AccountTransaction> txs = new Vector<AccountTransaction>();
		Map<Account, Long> mapAccountAmount = new HashMap<Account, Long>();
		List<Date> dates = getDates(txMap);
		for (Date date : dates) {
			AccountTransaction tx = new AccountTransaction();
			setDate(tx, date);
			long sum = 0;
			for (Account account : txMap.keySet()) {
				Map<Date,Long> mapDateAmount = mapAccountDateAmount.get(account);
				Long curr = mapDateAmount.get(date);
				if (curr != null) {
					mapAccountAmount.put(account, curr);
				}
				sum += mapAccountAmount.get(account);
			}
			tx.setSaldoAmount(sum);
			txs.add(tx);
		}
		Account accountSum = new Account();
		accountSum.setString(Account.PROP_TYPE, I18.tLabel("Sum"));
		accountSum.setString(Account.PROP_NAME, "("+I18.tLabel("Calculated")+")");
		accountSum.setString(Account.PROP_IBAN, "");
		txMap.put(accountSum, txs);
	}

	private Long getAmount(AccountTransaction tx) {
		return tx.getSaldoAmount();
	}

	private List<Date> getDates(Map<Account, List<AccountTransaction>> txMap) {
		List<Date> dates = new Vector<Date>();
		for (List<AccountTransaction> txs : txMap.values()) {
			for (AccountTransaction tx : txs) {
				dates.add(getDate(tx));
			}
		}
		Collections.sort(dates);
		return dates;
	}

	private void enrichDataWithStart(Map<Account, List<AccountTransaction>> txMap) {
		Date start = getFirstDate(txMap);
		if (start == null) {
			return;
		}
		for (List<AccountTransaction> txs : txMap.values()) {
			if (!txs.isEmpty()) {
				Date currStart = getStart(txs);
				if (start.compareTo(currStart) < 0) {
					AccountTransaction first = new AccountTransaction();
					first.copyFrom(txs.get(0));
					setDate(first, start);
					txs.add(0, first);
				}
			}
		}
	}

	private Date getStart(List<AccountTransaction> txs) {
		Date date = null;
		for (AccountTransaction tx : txs) {
			Date curr = getDate(tx);
			if (date == null || curr.compareTo(date) < 0) {
				date = curr;
			}
		}
		return date;
	}

	private void setDate(AccountTransaction tx, Date date) {
		tx.setBDate(date);
	}
	
	private Date getDate(AccountTransaction tx) {
		return tx.getBDate();
	}

	private Date getFirstDate(Map<Account, List<AccountTransaction>> txMap) {
		List<Date> starts = new Vector<Date>();
		for (List<AccountTransaction> txs : txMap.values()) {
			starts.add(getStart(txs));
		}
		if (starts.isEmpty()) {
			return null;
		}
		Collections.sort(starts);
		return starts.get(0);
	}
	
	
}
