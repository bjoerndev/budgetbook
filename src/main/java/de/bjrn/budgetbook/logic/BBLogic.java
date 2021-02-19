package de.bjrn.budgetbook.logic;

import de.bjrn.budgetbook.exceptions.BusinessException;
import de.bjrn.budgetbook.model.Access;
import de.bjrn.budgetbook.model.AccountTransaction;
import de.bjrn.budgetbook.model.BBModel;

import java.util.Objects;

public class BBLogic {
	
	private BBModel model;

	public BBLogic(BBModel model) {
		this.model = model;
		updateTransactionIdentifier();
	}

	private void updateTransactionIdentifier() {
		for (AccountTransaction tx : model.getAccountTransactions()) {
			String oldIdentifier = tx.getString(AccountTransaction.PROP_IDENTIFIER);
			String newIdentifier = tx.getIdentifier(); // refresh and get identifier
			if (!Objects.equals(oldIdentifier, newIdentifier)) {
				tx.delete(); // Delete old entry
				tx.thaw(); // Re-use deleted entry
				tx.save(); // Add entry with new identifier
			}
		}
	}

	/** Shutdown */
	public void close() {
		model.close();
		model = null;
	}
	
	public int sync() throws BusinessException {
		int txNew = 0;
		for (Access access : model.getAccesses()) {
			txNew += sync(access);
		}
		return txNew;
	}

	/**
	 * @param access
	 * @return Number of changed transactions
	 * @throws BusinessException
	 */
	public int sync(Access access) throws BusinessException {
		return SyncFactory.create(getModel(), access).sync();
	}

	public BBModel getModel() {
		return model;
	}

}
