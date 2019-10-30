package de.bjrn.budgetbook.logic.hbci;

import org.kapott.hbci.manager.BankInfo;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;

public class HbciPassport {

	public static HBCIPassport create(HbciCredential credentials) {
	    HBCIPassport passport = AbstractHBCIPassport.getInstance(credentials.getPassportFile());
	    
	    passport.setCountry(credentials.getCountry());
	    
	    // Server-Adresse angeben. Koennen wir entweder manuell eintragen oder direkt von HBCI4Java ermitteln lassen
	    BankInfo info = HBCIUtils.getBankInfo(credentials.getBlz());
	    passport.setHost(info.getPinTanAddress());
	    
	    // TCP-Port des Servers. Bei PIN/TAN immer 443, da das ueber HTTPS laeuft.
	    passport.setPort(credentials.getPort());
	    
	    // Art der Nachrichten-Codierung. Bei Chipkarte/Schluesseldatei wird
	    // "None" verwendet. Bei PIN/TAN kommt "Base64" zum Einsatz.
	    passport.setFilterType(credentials.getFilterType());
	    
	    passport.setBLZ(credentials.getBlz());
	    passport.setUserId(credentials.getUser());
	    if (passport instanceof HbciCredentialPinTan) {
			HbciCredentialPinTan pinTan = (HbciCredentialPinTan) passport;
			if (pinTan.getPin() == null || "".equals(pinTan.getPin())) {
				pinTan.setPin(((HbciCredentialPinTan)credentials).getPin());
			}
		}
		return passport;
	}

}
