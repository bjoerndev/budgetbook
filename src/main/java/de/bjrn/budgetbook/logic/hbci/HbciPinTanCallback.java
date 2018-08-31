package de.bjrn.budgetbook.logic.hbci;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.passport.HBCIPassport;

public class HbciPinTanCallback extends AbstractHBCICallback {

	private HbciCredentialPinTan creds;
	
	public HbciPinTanCallback(HbciCredentialPinTan creds) {
		this.creds = creds;
	}

	@Override
	public void callback(HBCIPassport passport, int reason, String msg, int datatype, StringBuffer retData) {
		// Diese Funktion ist wichtig. Ueber die fragt HBCI4Java die benoetigten Daten
		// von uns ab.
		switch (reason) {
		// Mit dem Passwort verschluesselt HBCI4Java die Passport-Datei.
		// Wir nehmen hier der Einfachheit halber direkt die PIN. In der Praxis
		// sollte hier aber ein staerkeres Passwort genutzt werden.
		// Die Ergebnis-Daten muessen in dem StringBuffer "retData" platziert werden.
		case NEED_PASSPHRASE_LOAD:
		case NEED_PASSPHRASE_SAVE:
			retData.replace(0, retData.length(), creds.getPin());
			break;

		// PIN wird benoetigt
		case NEED_PT_PIN:
			retData.replace(0, retData.length(), creds.getPin());
			break;

		// BLZ wird benoetigt
		case NEED_BLZ:
			retData.replace(0, retData.length(), creds.getBlz());
			break;

		// Die Benutzerkennung
		case NEED_USERID:
			retData.replace(0, retData.length(), creds.getUser());
			break;

		// Die Kundenkennung. Meist identisch mit der Benutzerkennung.
		// Bei manchen Banken kann man die auch leer lassen
		case NEED_CUSTOMERID:
			retData.replace(0, retData.length(), creds.getUser());
			break;

		// Manche Fehlermeldungen werden hier ausgegeben
		case HAVE_ERROR:
			log(msg);
			break;
			
		case NEED_PT_SECMECH:
			/*
			Dieser muss so implementiert werden, dass er auch den Callback
			"HBCICallback.NEED_PT_SECMECH" beruecksichtigt und verarbeitet. Hierbei
			wird eine Liste von TAN-Verfahren in Form 3-stelliger Zahlen uebergeben,
			die mit "|" getrennt sind. Du musst dann davon das richtige auswaehlen
			und zurueckliefern. 
			 */
			log("Available pintan methods: " + retData.toString()); // z.B. "999:Einschritt-Verfahren|901:mobileTAN"
			
			String[] methods = StringUtils.split(retData.toString(), '|');
			if (methods.length == 0) {
				log("ERROR: No pintan method!");
			} else {
				// Select first one, because we only want to read from bank and so it doesn't matter which "write"-method we choose
				String method = methods[0];
				if (method.contains(":")) {
					// Z.B. "901:mobileTAN" -> "901"
					method = StringUtils.split(method, ':')[0];
				}
				retData.replace(0, retData.length(), method);
			}
			break;
		default:
			// Wir brauchen nicht alle der Callbacks
			log("Unhandled callback: " + reason +", "+ msg + ", default: " + retData.toString());
			break;

		}
	}

	private void log(String msg) {
		System.out.println(msg); // TODO
	}

	@Override
	public void log(String msg, int level, Date date, StackTraceElement trace) {
		// Ausgabe von Log-Meldungen bei Bedarf
		System.out.println(msg); // TODO
	}

	@Override
	public void status(HBCIPassport passport, int statusTag, Object[] o) {
		// So aehnlich wie log(String,int,Date,StackTraceElement) jedoch fuer Status-Meldungen.
	}

}
