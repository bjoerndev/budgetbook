package de.bjrn.budgetbook.model;

import org.javalite.activejdbc.Model;

public class Access extends Model {

	public static final String PROP_ACCESS_TYPE = "accessType";
	public static final String PROP_BLZ = "blz";
	public static final String PROP_USER_NAME = "userName";
	private static final String PROP_PIN = "pin";
	public static final String PROP_DESCRIPTION = "description";
	
	public static final String[] PROPS4VIEW = new String[] {PROP_DESCRIPTION, PROP_BLZ, PROP_USER_NAME, PROP_ACCESS_TYPE};
	
	public AccessType getAccessType() {
		String s = getString(PROP_ACCESS_TYPE);
		return s == null ? null : AccessType.valueOf(s);
	}
	public void setAccessType(AccessType type) {
		setString(PROP_ACCESS_TYPE, type == null ? null : type.name());
	}
	public String getBlz() {
		return getString(PROP_BLZ);
	}
	public void setBlz(String blz) {
		setString(PROP_BLZ, blz);
	}
	public String getUserName() {
		return getString(PROP_USER_NAME);
	}
	public void setUserName(String user) {
		setString(PROP_USER_NAME, user);
	}
	public String getPin() {
		return getString(PROP_PIN);
	}
	public void setPin(String pin) {
		setString(PROP_PIN, pin);
	}
	public void setDesc(String description) {
		setString(PROP_DESCRIPTION, description);
	}
	public String getDesc() {
		return getString(PROP_DESCRIPTION);
	}
	
}
