package de.bjrn.budgetbook.model;

import de.bjrn.budgetbook.logic.Utils;

import java.time.LocalDate;
import java.util.*;

;

public class AccountTransactionList extends Vector<AccountTransaction> {
	private static final long serialVersionUID = 1L;
	
	public AccountTransactionList() {
	}
	
	/**
	 * Constructor only uses items between start (included) and end (excluded)
	 * @param sourceList
	 * @param start null (unlimited), or first accepted date (included)
	 * @param end null (unlimited), or last accepted date (excluded)
	 */
	public AccountTransactionList(List<AccountTransaction> sourceList, LocalDate start, LocalDate end) {
		for (AccountTransaction tx : sourceList) {
			LocalDate dateTx = Utils.getLocalDate(tx.getBDate());
			if ((start == null || start.isBefore(dateTx) || start.equals(dateTx)) 
					&& (end == null || end.isAfter(dateTx))) {
				add(tx);
			}
		}
	}

	public AccountTransactionList(Collection<AccountTransaction> txs) {
		super(txs);
	}

	public LocalDate getDateMin() {
		LocalDate min = null;
		for (AccountTransaction tx : this) {
			LocalDate curr = Utils.getLocalDate(tx.getBDate());
			if (min == null || min.isAfter(curr)) {
				min = curr;
			}
		}
		return min;
	}
	
	public LocalDate getDateMax() {
		LocalDate max = null;
		for (AccountTransaction tx : this) {
			LocalDate curr = Utils.getLocalDate(tx.getBDate());
			if (max == null || max.isBefore(curr)) {
				max = curr;
			}
		}
		return max;
	}

	public long getAmount() {
		long amount = 0;
		for (AccountTransaction tx : this) {
			Long curr = tx.getValueAmount();
			if (curr != null) {
				amount += curr;
			}
		}
		return amount;
	}

	/**
	 * @param outgoings true: sum of all outgoings, false: sum of all incomings
	 * @return absolute (positive) sum
	 */
	public long getAmount(boolean outgoings) {
		long amount = 0;
		for (AccountTransaction tx : this) {
			Long curr = tx.getValueAmount();
			if (curr != null 
					&& ((curr < 0 && outgoings) || (curr > 0 && !outgoings))
					) {
				amount += curr;
			}
		}
		return Math.abs(amount);
	}

	public void sortByDate(boolean ascending) {
		Comparator<AccountTransaction> comp = Comparator.comparing(AccountTransaction::getSaldoDate)
				.thenComparing(AccountTransaction::getLongId);
		this.sort(ascending ? comp : comp.reversed());
	}

	/**
	 * 
	 * @param category
	 * @param recursive Incluse sub-categories?
	 * @param model Can be null if recursive is false
	 * @return
	 */
	public AccountTransactionList subList(Category category, boolean recursive, BBModel model) {
		AccountTransactionList l = new AccountTransactionList();
		for (AccountTransaction tx : this) {
			if (tx.getCategory().equals(category.getLongId())) {
				l.add(tx);
			}
		}
		if (recursive) {
			for (Category sub : model.getCategories(category)) {
				l.addAll(subList(sub, recursive, model));
			}
		}
		return l;
	}
	
	public AccountTransactionList subList(LocalDate start, LocalDate end) {
		return new AccountTransactionList(this, start, end);
	}

	public AccountTransactionList subListNotIgnored(BBModel model) {
		AccountTransactionList l = new AccountTransactionList();
		for (AccountTransaction tx : this) {
			Category cat = model.getCategory(tx.getCategory());
			if (!cat.isIgnore()) {
				l.add(tx);
			}
		}
		return l;
	}

	public AccountTransactionList subList(BBModel model, Access access) {
		return subList(model.getAccounts(access));
	}

	public AccountTransactionList subList(List<Account> accounts) {
		List<String> accountIds = new ArrayList<>();
		for (Account account : accounts) {
			accountIds.add(account.getIdentifier());
		}
		AccountTransactionList l = new AccountTransactionList();
		for (AccountTransaction tx : this) {
			if (accountIds.contains(tx.getAccountID())) {
				l.add(tx);
			}
		}
		return l;
	}
}
