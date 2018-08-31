package de.bjrn.budgetbook.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import org.kordamp.ikonli.websymbols.Websymbols;

import de.bjrn.budgetbook.model.Access;
import de.bjrn.budgetbook.model.Account;
import de.bjrn.budgetbook.model.transaction.TxActionListener;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.helper.JBBButton;
import de.bjrn.budgetbook.view.swing.helper.JTableCompact;
import de.bjrn.budgetbook.view.swing.tables.ModelTableModel;

public class BBViewAccesses extends BBViewAbstract {
	private static final long serialVersionUID = 1L;
	
	public BBViewAccesses(BBViewMain main) {
		super(main);
	}

	private JPanel createAccessActions(Access access) {
		JPanel p = new JPanel(new GridLayout(0, 1));
		p.add(createEditAccessButton(access));
		p.add(createRemoveAccessButton(access));
		p.add(createCheckAccessButton(access));
		JPanel main = new JPanel(new BorderLayout());
		main.add(p, BorderLayout.EAST);
		return main;
	}

	private JButton createCheckAccessButton(final Access access) {
		JButton b = new JBBButton(I18.tLabel("Check"), Websymbols.LINK, Color.BLACK);
		b.setToolTipText(I18.tLabel("CheckAccount.Desc"));
		b.addActionListener(new TxActionListener(e -> checkAccess(access)));
		return b;
	}
	
	private JButton createEditAccessButton(final Access access) {
		JButton b = new JBBButton(I18.tLabel("Edit"), Websymbols.EDIT, Color.BLACK);
		b.setToolTipText(I18.tLabel("EditAccount.Desc"));
		b.addActionListener(new TxActionListener(e -> editAccess(access)));
		return b;
	}
	
	private void editAccess(Access access) {
		setContext(new BBViewEditAccess(getView(), access));
	}

	private JButton createRemoveAccessButton(final Access access) {
		JButton b = new JBBButton(I18.tLabel("Remove"), Websymbols.STOP, Color.BLACK);
		b.setToolTipText(I18.tLabel("RemoveAccount.Desc"));
		b.addActionListener(new TxActionListener(e -> removeAccess(access)));
		return b;
	}
	
	private void removeAccess(Access selection) {
		selection.delete();
		showUI();
	}

	private void checkAccess(Access access) {
		try {
			int num = getLogic().sync(access);
			showInfo("Datenabruf erfolgreich", num+" neue/aktualisierte Umsätze");
		} catch (Exception e) {
			e.printStackTrace();
			showWarning("Fehler beim Verbindungsaufbau", e.getMessage());
		}
	}

	@Override
	public void showUI() {
		removeAll();
		setLayout(new BorderLayout());
		add(createContent(), BorderLayout.NORTH);
	}
	
	private Component createContent() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		List<Access> accesses = getModel().getAccesses();
		boolean first = true;
		for (Access access : accesses) {
			if (first) {
				first = false;
			} else {
				JSeparator separator = new JSeparator();
				separator.setBorder(new EmptyBorder(15, 10, 15, 10));
				p.add(separator);
			}

			p.add(createAccessPanel(access));
		}
		p.add(createButtonsGeneral());
		return p;
	}
	
	private Component createAccessPanel(Access access) {
		JPanel pa = new JPanel();
		pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
		pa.add(new JLabel(I18.tLabel("Access")));
		pa.add(new JScrollPane(new JTableCompact(createTableModel(access))));
		pa.add(new JLabel(I18.tLabel("Accounts")));
		pa.add(new JScrollPane(new JTableCompact(createTableModel(getModel().getAccounts(access)))));
		
		int space = 20;
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new EmptyBorder(space, 0, space, 0));
		p.add(pa, BorderLayout.CENTER);
		p.add(createAccessActions(access), BorderLayout.EAST);
		return p;
	}

	private Component createButtonsGeneral() {
		JPanel p = new JPanel(new GridLayout(1, 0));
		p.add(new CreateAccessButton(getView()));
		p.add(createSyncButton());
		return p;
	}

	private Component createSyncButton() {
		JBBButton b = new JBBButton(I18.tLabel("Sync"), Websymbols.SYNCHRONISE, Color.BLUE);
		b.setToolTipText(I18.tLabel("Sync.Desc"));
		b.addActionListener(new TxActionListener(e -> sync()));
		return b;
	}

	private void sync() {
		try {
			int num = getLogic().sync();
			showInfo("Datenabruf erfolgreich", num+" Umsätze neu/aktualisiert");
		} catch (Exception e) {
			e.printStackTrace(); 
			showWarning("Fehler beim Verbindungsaufbau", e.getMessage());
		}
	}

	private TableModel createTableModel(List<Account> accounts) {
		return new ModelTableModel<Account>(accounts, Account.PROPS4VIEW);
	}

	private TableModel createTableModel(Access access) {
		List<Access> accs = new Vector<Access>();
		accs.add(access);
		ModelTableModel<Access> tm = new ModelTableModel<Access>(accs, Access.PROPS4VIEW);
		tm.setEditable(Access.PROP_DESCRIPTION);
		return tm;
	}
	
}
