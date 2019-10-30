package de.bjrn.budgetbook.logic.hbci;

import org.kapott.hbci.manager.HBCIVersion;

import java.io.File;

public abstract class HbciCredential {
	private String blz;
	private String user;
	private String country = "DE";
	private HBCIVersion hbciVersion = HBCIVersion.HBCI_300;

	public HbciCredential(String blz, String user) {
		this.blz = blz;
		if ("50010517".equals(blz)) {
			hbciVersion = HBCIVersion.HBCI_PLUS; // 30.10.2019: DiBa PSD2 only supports HBCI_PLUS
		}
		this.user = user;
	}
	
	public String getBlz() {
		return blz;
	}

	public void setBlz(String blz) {
		this.blz = blz;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public HBCIVersion getHbciVersion() {
		return hbciVersion;
	}

	public void setHbciVersion(HBCIVersion hbciVersion) {
		this.hbciVersion = hbciVersion;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public abstract int getPort();
	
	/**
	 * @return Art der Nachrichten-Codierung. Bei Chipkarte/Schluesseldatei wird "None" verwendet. Bei PIN/TAN kommt "Base64" zum Einsatz.
	 */
	public abstract String getFilterType();

	/** Prepare query */
	public abstract void init();
	
	public abstract void close();

	public abstract File getPassportFile();
}
