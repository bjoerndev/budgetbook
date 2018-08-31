package de.bjrn.budgetbook.logic.hbci;

import java.io.File;
import java.util.Properties;

import org.kapott.hbci.manager.HBCIUtils;

import de.bjrn.budgetbook.model.Access;

public class HbciCredentialPinTan extends HbciCredential {
	private String pin;
	
	private File passportFile;
	
	private Long accessId;
	
	public HbciCredentialPinTan(Access access) {
		super(access.getBlz(), access.getUserName());
		this.pin = access.getPin();
		this.accessId = access.getLongId();
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	@Override
	public int getPort() {
		return 443;
	}

	@Override
	public String getFilterType() {
		return "Base64";
	}

	@Override
	public void init() {
		// HBCI4Java initialisieren
	    // In "props" koennen optional Kernel-Parameter abgelegt werden, die in der Klasse
	    // org.kapott.hbci.manager.HBCIUtils (oben im Javadoc) beschrieben sind.
	    Properties props = new Properties();
	    HBCIUtils.init(props,new HbciPinTanCallback(this));
	
		// In der Passport-Datei speichert HBCI4Java die Daten des Bankzugangs (Bankparameterdaten, Benutzer-Parameter, etc.).
	    // Die Datei kann problemlos geloescht werden. Sie wird beim naechsten mal automatisch neu erzeugt,
	    // wenn der Parameter "client.passport.PinTan.init" den Wert "1" hat (siehe unten).
	    // Wir speichern die Datei der Einfachheit halber im aktuellen Verzeichnis.
		passportFile = new File("testpassport-"+accessId+".dat"); // TODO change folder
		passportFile.deleteOnExit();
		HBCIUtils.setParam("client.passport.default","PinTan"); // Legt als Verfahren PIN/TAN fest.
	    HBCIUtils.setParam("client.passport.PinTan.filename", passportFile.getAbsolutePath());
	    HBCIUtils.setParam("client.passport.PinTan.init","1");
	}

	@Override
	public void close() {
		HBCIUtils.done();
		passportFile.delete();
		passportFile = null;
	}
	
}
