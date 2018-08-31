package de.bjrn.budgetbook.logic.hbci;

import de.bjrn.budgetbook.model.Access;

public class HbciCredentialFactory {

	public final static HbciCredential create(Access access) {
		switch(access.getAccessType()) {
		case HbciPinTan: return new HbciCredentialPinTan(access);
		default: throw new RuntimeException("Unsupported access type: " + access.getAccessType().name()+". Tried to open a database created with a new version with an old program version?");
		}
	}
}
