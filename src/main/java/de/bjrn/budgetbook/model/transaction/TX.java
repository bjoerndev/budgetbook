package de.bjrn.budgetbook.model.transaction;

import org.javalite.activejdbc.Base;

import de.bjrn.budgetbook.model.BBModel;

public class TX {

	public static void start() {
		Base.open(BBModel.INSTANCE().getDataSource());
		Base.openTransaction();
	}

	public static void abort() {
		try {
			Base.rollbackTransaction();
		} finally {
			Base.close();
		}
	}

	public static void commit() {
		try {
			Base.commitTransaction();
		} finally {
			Base.close();
		}
	}

}
