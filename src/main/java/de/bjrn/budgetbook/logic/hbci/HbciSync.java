package de.bjrn.budgetbook.logic.hbci;

import de.bjrn.budgetbook.exceptions.BusinessException;
import de.bjrn.budgetbook.logic.Sync;
import de.bjrn.budgetbook.model.Access;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.BBModel;

public class HbciSync implements Sync {
	
	private Access access;
	private BBModel model;
	
	public HbciSync(BBModel model, Access access) {
		this.access = access;
		this.model = model;
	}

	@Override
	public int sync() throws BusinessException {
		HbciCredential credentials = HbciCredentialFactory.create(access);
		try {
			HbciTransaction tx = new HbciTransaction(credentials);
			int txNew = 0;
			for (AccountTransaction atx : tx.query(model, access)) {
				boolean added = model.sync(atx);
				if (added) {
					txNew++;
				}
			}
			return txNew;
		} finally {
			credentials.close();
		}
	}

}
