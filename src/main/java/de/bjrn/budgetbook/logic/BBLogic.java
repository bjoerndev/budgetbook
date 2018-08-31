package de.bjrn.budgetbook.logic;

import de.bjrn.budgetbook.exceptions.BusinessException;
import de.bjrn.budgetbook.model.Access;
import de.bjrn.budgetbook.model.BBModel;

public class BBLogic {
	
	private BBModel model;

	public BBLogic(BBModel model) {
		this.model = model;
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
