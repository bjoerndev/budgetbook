package de.bjrn.budgetbook.logic.hbci;

import java.time.LocalDate;
import java.util.*;

import de.bjrn.budgetbook.logic.Utils;
import de.bjrn.budgetbook.model.AccountTransactionList;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.GV_Result.HBCIJobResult;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;

import de.bjrn.budgetbook.exceptions.BusinessException;
import de.bjrn.budgetbook.model.Access;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.BBModel;

public class HbciTransaction {

	private HbciCredential credentials;

	public HbciTransaction(HbciCredential credentials) {
		this.credentials = credentials;
	}

	public List<AccountTransaction> query(BBModel model, Access access) throws BusinessException {
		credentials.init();
		HBCIPassport passport = HbciPassport.create(credentials);
		HBCIHandler handle = null;
		try {
			// Verbindung zum Server aufbauen
			handle = new HBCIHandler(credentials.getHbciVersion().getId(), passport);
			return execute(model, handle, access);
		} finally {
			// Sicherstellen, dass sowohl Passport als auch Handle nach Beendigung
			// geschlossen werden.
			if (handle != null) {
				handle.close();
			}
			if (passport != null) {
				passport.close();
			}
		}
	}

	protected List<AccountTransaction> execute(BBModel model, HBCIHandler handle, Access access) throws BusinessException {
		// See https://github.com/hbci4j/hbci4java/blob/master/src/main/java/org/kapott/hbci/examples/UmsatzAbrufPinTan.java
		List<AccountTransaction> results = new Vector<AccountTransaction>();
		Konto[] accounts = handle.getPassport().getAccounts();
		if (accounts == null || accounts.length == 0) {
			throw new BusinessException("Keine Konten ermittelbar");
		}
		// Create Jobs
		List<HBCIJob> jobs = new Vector<HBCIJob>();
		Map<HBCIJob, Konto> mapJob2Account = new HashMap<>();
		for (Konto account : accounts) {
			model.sync(HbciTools.getAccount(account), access);
			// Auftrag fuer das Abrufen der Umsaetze erzeugen
			// See https://github.com/hbci4j/hbci4java/blob/master/src/main/java/org/kapott/hbci/GV/package.html
			HBCIJob job = handle.newJob("KUmsAll");
			job.setParam("my", account); // festlegen, welches Konto abgefragt werden soll.
			job.setParam("startdate", new Date(Utils.getMillis(getLastTxDate(model, access))));
			job.addToQueue(); // Zur Liste der auszufuehrenden Auftraege hinzufuegen
			jobs.add(job);
			mapJob2Account.put(job, account);
		}
		// Fire jobs
		HBCIExecStatus status = handle.execute();
		if (!status.isOK()) {
			throw new BusinessException(status.toString());
		}
		
		// Parse job results
		for (HBCIJob job : jobs) {
			HBCIJobResult result = job.getJobResult();
			if (!result.isOK()) {
				throw new BusinessException(result.toString());
			}
			if (result instanceof GVRKUms) {
				// Jobs des Typs "KUmsAll" liefern immer GVRKUms
				results.addAll(parse((GVRKUms) result, mapJob2Account.get(job)));
			} else {
				throw new BusinessException("Unknown job result type: " + result.getClass().getName());
			}
		}
		return results;
	}

	private LocalDate getLastTxDate(BBModel model, Access access) {
		return new AccountTransactionList(model.getAccountTransactions()).subList(model, access).getDateMax();
	}

	private List<AccountTransaction> parse(GVRKUms result, Konto account) {
		List<AccountTransaction> results = new Vector<AccountTransaction>();
		List<UmsLine> transactions = result.getFlatData();
		for (UmsLine tx : transactions) {
			results.add(HbciTools.getAccountTransaction(tx, account));
		}
		return results;
	}

}
 