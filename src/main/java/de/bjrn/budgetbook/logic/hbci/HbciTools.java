package de.bjrn.budgetbook.logic.hbci;

import java.util.List;

import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.structures.Konto;

import de.bjrn.budgetbook.model.Account;
import de.bjrn.budgetbook.model.AccountTransaction;

public class HbciTools {

	/**
	 * @param account
	 * @return Unique id for account. E.g. iban
	 */
	public final static String getID(Konto account) {
		return getAccount(account).getIdentifier();
	}
	
	private static String sum(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String line : list) {
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static AccountTransaction getAccountTransaction(UmsLine tx, Konto account) {
		AccountTransaction t = new AccountTransaction();
		t.setAccountID(HbciTools.getID(account));
		//
		t.setAdditional(tx.additional);
		t.setBDate(tx.bdate);
		if (tx.charge_value != null) {
			t.setChargeValueAmount(tx.charge_value.getLongValue());
			t.setChargeValueCurrency(tx.charge_value.getCurr());
		}
		t.setCustomerRef(tx.customerref);
		t.setGVCode(tx.gvcode);
		if (!"999".equals(tx.gvcode)) {
			t.setText(tx.text);
			t.setPrimanota(tx.primanota);
			t.setUsage(sum(tx.usage));
//			t.setOther(tx.other == null ? null : getAccount(tx.other).getIdentifier());
			if (tx.other != null) {
				t.setOtherName(tx.other.name);
				t.setOtherName2(tx.other.name2);
				t.setOtherIBAN(tx.other.iban);
				t.setOtherBIC(tx.other.bic);
			}
			t.setAddKey(tx.addkey);
		}
		t.setInstRef(tx.instref);
		t.setSepa(tx.isSepa);
		t.setStorno(tx.isStorno);
		t.setOrigValueAmount(tx.orig_value == null ? null : tx.orig_value.getLongValue());
		t.setOrigValueCurrency(tx.orig_value == null ? null : tx.orig_value.getCurr());
		if (tx.saldo != null && tx.saldo.value != null) {
			t.setSaldoAmount(tx.saldo.value.getLongValue());
			t.setSaldoCurrency(tx.saldo.value.getCurr());
			t.setSaldoDate(tx.saldo.timestamp);
		}
		if (tx.value != null) {
			t.setValueAmount(tx.value.getLongValue());
			t.setValueCurrency(tx.value.getCurr());
		}
		t.setValuta(tx.valuta);
			
		return t;
	}

	public static Account getAccount(Konto konto) {
		if (konto == null) {
			return null;
		}
		Account acc = new Account();
		acc.setString(Account.PROP_ACCTYPE, konto.acctype);
		acc.setString(Account.PROP_BIC, konto.bic);
		acc.setString(Account.PROP_IBAN, konto.iban);
		acc.setString(Account.PROP_NAME, konto.name);
		acc.setString(Account.PROP_NAME2, konto.name2);
		acc.setString(Account.PROP_NUMBER, konto.number);
		acc.setString(Account.PROP_SUBNUMBER, konto.subnumber);
		acc.setString(Account.PROP_TYPE, konto.type);
		return acc;
	}
}
