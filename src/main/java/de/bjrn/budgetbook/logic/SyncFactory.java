package de.bjrn.budgetbook.logic;

import org.kapott.hbci.exceptions.InvalidArgumentException;

import de.bjrn.budgetbook.logic.hbci.HbciSync;
import de.bjrn.budgetbook.model.Access;
import de.bjrn.budgetbook.model.BBModel;

public class SyncFactory {

	public final static Sync create(BBModel model, Access access) {
		switch (access.getAccessType()) {
		case HbciPinTan:
			return new HbciSync(model, access);
		default:
			throw new InvalidArgumentException("Unsupported access type: " + access.getAccessType().name());
		}
	}
}
