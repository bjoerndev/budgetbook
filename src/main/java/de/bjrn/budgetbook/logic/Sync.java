package de.bjrn.budgetbook.logic;

import de.bjrn.budgetbook.exceptions.BusinessException;

public interface Sync {

	/**
	 * @param access
	 * @return Number of changed transactions
	 * @throws BusinessException
	 */
	public int sync() throws BusinessException;
	
}
