package de.bjrn.budgetbook.model;

import org.javalite.activejdbc.Model;
import org.kapott.hbci.structures.Konto;

/**
 * Ein Konto bei einer Bank.
 * @see Konto
 * @author bschaedlich
 */
public class Account extends Model {
	
	/**
	 * Aus den anderen Werten berechneter eindeutiger Identifier
	 * @see #updateID()
	 */
	public static final String PROP_IDENTIFIER = "identifier";

    /** Name des Kontoinhabers. Hier wird bei eigenen Konten der
    Inhabername eingestellt, wie er von der Bank bereitgestellt wird.
    Bei fremden Konten (z.B. bei den Konten, die als Gegenkonten auf
    einem Kontoauszug erscheinen) wird hier der Name eingestellt,
    wie er in den Auftragsdaten von der Bank geführt wird. */
	public static final String PROP_NAME = "name";
    /** Name des Kontoinhabers (Fortsetzung) (optional). */
	public static final String PROP_NAME2 = "name2";
    
    /** BIC des Kontos */
	public static final String PROP_BIC = "bic";
    /** IBAN des Kontos */
	public static final String PROP_IBAN = "iban";
    
    /** Kontonummer des Kontos */
	public static final String PROP_NUMBER = "number";
    /** Unterkontomerkmal des Kontos, kann <code>null</code> sein */
	public static final String PROP_SUBNUMBER = "subnumber";
    /** Kontoart (Girokonto, Sparkonto, Festgeldkonto, Kreditkartenkonto, etc.)
        laut Segmentversion 5,6 von HIUPD.
        Wird bspw. bei DeuBa-Konten benötigt da dort verschiedene Konten genau die gleiche
        Kontonummer haben bzw. sich nur in der Kontoart unterscheiden */
	public static final String PROP_ACCTYPE = "acctype";

	public static final String PROP_TYPE = "type0";
	/**
	 * References to {@link Access#getId()}
	 */
	public static final String PROP_ACCESS_ID = "accessID";
	
	public static final String[] PROPS4VIEW = new String[] {PROP_TYPE, PROP_NAME, PROP_NAME2, PROP_IBAN};
    
    /**
     * Set the {@link #identifier} of this by other values of this
     */
    private void updateID() {
    	String identifier = getString(PROP_IBAN);
    	if (identifier == null) {
			identifier = getString(PROP_NUMBER);
		}
    	String subnumber =  getString(PROP_SUBNUMBER);
    	if (subnumber != null) {
    		identifier += "/" + subnumber;
    	}
    	String acctype = getString(PROP_ACCTYPE);
    	if (acctype != null) {
    		identifier += "#" + acctype;
    	}
    	setString(PROP_IDENTIFIER, identifier);
    }
    
    @Override
    public boolean save() {
    	updateID();
    	return super.save();
    }
    
    @Override
    public boolean saveIt() {
    	updateID();
    	return super.saveIt();
    }

	public String getIdentifier() {
		updateID();
		return getString(PROP_IDENTIFIER);
	}

	public void setIdentifier(String identifier) {
		throw new RuntimeException("Identifier not writable");
	}

	public String getNumber() {
		return getString(Account.PROP_NUMBER);
	}
	
	@Override
	public String toString() {
		return getString(PROP_TYPE) + " " + getString(PROP_NAME) + " " + getString(PROP_IBAN);
	}
	
	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}
	
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Account) {
			Account a2 = (Account) o2;
			return getIdentifier().equals(a2.getIdentifier());
		}
		return false;
	}

	public String toStringShort() {
		return getString(PROP_TYPE);
	}

	public void setAccessID(Long accessID) {
		setLong(PROP_ACCESS_ID, accessID);
	}
	
	public Long getAccessID() {
		return getLong(PROP_ACCESS_ID);
	}
    
}
