package de.bjrn.budgetbook.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

import de.bjrn.budgetbook.logic.Utils;

/**
 * Data bean for one transaction
 */
public class AccountTransaction extends Model {

	/**
	 * Corresponding banking account: {@link Account#getIdentifier()}
	 */
	public final static String PROP_ACCOUNT_ID = "accountID";
    
    /** Datum der Wertstellung */
	public final static String PROP_VALUTA = "valuta";
    /** Buchungsdatum */
	public final static String PROP_BDATE = "bdate";

    /** Gebuchter Betrag: Wert * 100 */
	public final static String PROP_VALUE_AMOUNT = "valueAmount";
    /** Gebuchter Betrag: Währung, zB EUR */
	public final static String PROP_VALUE_CURRENCY = "valueCurrency";
    /** Handelt es sich um eine Storno-Buchung? */
	public final static String PROP_IS_STORNO = "isStorno";
    /** Der Saldo <em>nach</em> dem Buchen des Betrages <code>value</code>: Timestamp */
	public final static String PROP_SALDO_DATE = "saldoDate";
    /** Der Saldo <em>nach</em> dem Buchen des Betrages <code>value</code>: Wert * 100 */
	public final static String PROP_SALDO_AMOUNT = "saldoAmount";
    /** Der Saldo <em>nach</em> dem Buchen des Betrages <code>value</code>: Währung, zB EUR */
	public final static String PROP_SALDO_CURRENCY = "saldoCurrency";
    /** Kundenreferenz */
	public final static String PROP_CUSTOMER_REF = "customerref";
    /** Kreditinstituts-Referenz */
	public final static String PROP_INST_REF = "instref";
    /** Ursprünglicher Betrag (bei ausländischen Buchungen; optional): Wert * 100 */
	public final static String PROP_ORIG_VALUE_AMOUNT = "origValueAmount";
    /** Ursprünglicher Betrag (bei ausländischen Buchungen; optional): Währung, zB EUR */
	public final static String PROP_ORIG_VALUE_CURRENCY = "origValueCurrency";
    /** Betrag für Gebühren des Geldverkehrs (optional): Wert * 100 */
	public final static String PROP_CHARGE_VALUE_AMOUNT = "chargeValueAmount";
    /** Betrag für Gebühren des Geldverkehrs (optional): Währung, zB EUR */
	public final static String PROP_CHARGE_VALUE_CURRENCY = "chargeValueCurrency";

    /** Art der Buchung (bankinterner Code). Nur wenn hier ein Wert ungleich
     * <code>999</code> drinsteht, enthalten die Attribute <code>text</code>,
     * <code>primanota</code>, <code>usage</code>, <code>other</code> und
     * <code>addkey</code> sinnvolle Werte. Andernfalls sind all diese
     * Informationen möglicherweise im Feld <code>additional</code> enthalten,
     * allerdings in einem nicht definierten Format (siehe auch
     * <code>additional</code>). */
	public final static String PROP_GV_CODE = "gvcode";

    /** <p>Zusatzinformationen im Rohformat. Wenn Zusatzinformationen zu dieser
        Transaktion in einem unbekannten Format vorliegen, dann enthält dieser
        String diese Daten (u.U. ist dieser String leer, aber nicht <code>null</code>).
    Das ist genau dann der Fall, wenn der Wert von  <code>gvcode</code> gleich <code>999</code> ist.</p>
        <p>Wenn die Zusatzinformationen aber ausgewertet werden können (und <code>gvcode!=999</code>),
    so ist dieser String <code>null</code>, und die Felder <code>text</code>, <code>primanota</code>,
        <code>usage</code>, <code>other</code> und <code>addkey</code>
        enthalten die entsprechenden Werte (siehe auch <code>gvcode</code>)</p> */
	public final static String PROP_ADDITIONAL = "additional";

    /** Beschreibung der Art der Buchung (optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>)*/
	public final static String PROP_TEXT = "text";
    /** Primanotakennzeichen (optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>) */
    public final static String PROP_PRIMANOTA = "primanota";
    /** Liste von Strings mit den Verwendungszweckzeilen. Hier durch \n getrennt
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>)*/
    public final static String PROP_USAGE = "usage";
    /** Gegenkonto der Buchung (optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>) */
    public final static String PROP_OWNER = "other";
    /** Erweiterte Informationen zur Art der Buchung (bankintern, optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>) */
    public final static String PROP_ADD_KEY = "addkey";
    
    /** Gibt an, ob ein Umsatz ein SEPA-Umsatz ist **/
    public final static String PROP_IS_SEPA = "isSepa";
    
    public final static String PROP_OTHER_NAME = "otherName";
    public final static String PROP_OTHER_NAME2 = "otherName2";
    public final static String PROP_OTHER_IBAN = "otherIBAN";
    public final static String PROP_OTHER_BIC = "otherBIC";
    
    public final static String PROP_IDENTIFIER = "identifier";
    
    public final static String PROP_CATEGORY_MANUAL = "categoryManual";
    public final static String PROP_CATEGORY_AUTO = "categoryAuto";
    
    public final static String PROP_DESCRIPTION = "description";

	public static final String[] PROPS4VIEW = new String[] {
			PROP_BDATE,
			PROP_OTHER_NAME,
			PROP_VALUE_AMOUNT,
			PROP_SALDO_AMOUNT,
			PROP_TEXT,
			PROP_USAGE,
			PROP_DESCRIPTION,
			PROP_CATEGORY_AUTO,
			PROP_CATEGORY_MANUAL
			};
	
	public static final String[] PROPS4VIEW_NO_SALDO = new String[] {
			PROP_BDATE,
			PROP_OTHER_NAME,
			PROP_VALUE_AMOUNT,
			PROP_TEXT,
			PROP_USAGE,
			PROP_DESCRIPTION,
			PROP_CATEGORY_AUTO,
			PROP_CATEGORY_MANUAL
			};
    
    public Date getValuta() {
    	return getDate(PROP_VALUTA);
    }
    
    public void setValuta(Date value) {
    	setDate(PROP_VALUTA, value);
    }
    
    public Date getSaldoDate() {
    	return getDate(PROP_SALDO_DATE);
    }
    
    public void setSaldoDate(Date value) {
    	setDate(PROP_SALDO_DATE, value);
    }
    
    public Date getBDate() {
    	return getDate(PROP_BDATE);
    }
    
    public void setBDate(Date value) {
    	setDate(PROP_BDATE, value);
    }
    
    public String getValueCurrency() {
    	return getString(PROP_VALUE_CURRENCY);
    }
    
    public void setValueCurrency(String value) {
    	setString(PROP_VALUE_CURRENCY, value);
    }
    
    public String getSaldoCurrency() {
    	return getString(PROP_SALDO_CURRENCY);
    }
    
    public void setSaldoCurrency(String value) {
    	setString(PROP_SALDO_CURRENCY, value);
    }
    
    public String getCustomerRef() {
    	return getString(PROP_CUSTOMER_REF);
    }
    
    public void setCustomerRef(String value) {
    	setString(PROP_CUSTOMER_REF, value);
    }
    
    public String getInstRef() {
    	return getString(PROP_INST_REF);
    }
    
    public void setInstRef(String value) {
    	setString(PROP_INST_REF, value);
    }
    
    public String getChargeValueCurrency() {
    	return getString(PROP_CHARGE_VALUE_CURRENCY);
    }
    
    public void setChargeValueCurrency(String value) {
    	setString(PROP_CHARGE_VALUE_CURRENCY, value);
    }
    
    public String getGVCode() {
    	return getString(PROP_GV_CODE);
    }
    
    public void setGVCode(String value) {
    	setString(PROP_GV_CODE, value);
    }
    
    public String getAdditional() {
    	return getString(PROP_ADDITIONAL);
    }
    
    public void setAdditional(String value) {
    	setString(PROP_ADDITIONAL, value);
    }
    
    public String getPrimanota() {
    	return getString(PROP_PRIMANOTA);
    }
    
    public void setPrimanota(String value) {
    	setString(PROP_PRIMANOTA, value);
    }
    
    public String getUsage() {
    	return getString(PROP_USAGE);
    }
    
    public void setUsage(String value) {
    	setString(PROP_USAGE, value);
    }
    
    public String getOwner() {
    	return getString(PROP_OWNER);
    }
    
    public void setOwner(String value) {
    	setString(PROP_OWNER, value);
    }
    
    public String getAccountID() {
    	return getString(PROP_ACCOUNT_ID);
    }
    
    public void setAccountID(String value) {
    	setString(PROP_ACCOUNT_ID, value);
    }
    
    public String getAddKey() {
    	return getString(PROP_ADD_KEY);
    }
    
    public void setAddKey(String value) {
    	setString(PROP_ADD_KEY, value);
    }
    
    public String getOrigValueCurrencyKey() {
    	return getString(PROP_ORIG_VALUE_CURRENCY);
    }
    
    public void setOrigValueCurrency(String value) {
    	setString(PROP_ORIG_VALUE_CURRENCY, value);
    }
    
    public String getText() {
    	return getString(PROP_TEXT);
    }
    
    public void setText(String value) {
    	setString(PROP_TEXT, value);
    }
    
    public Boolean isSepa() {
    	return getBoolean(PROP_IS_SEPA);
    }
    
    public void setSepa(Boolean value) {
    	setBoolean(PROP_IS_SEPA, value);
    }
    
    public Boolean isStorno() {
    	return getBoolean(PROP_IS_STORNO);
    }
    
    public void setStorno(Boolean value) {
    	setBoolean(PROP_IS_STORNO, value);
    }
    
    public Long getValueAmount() {
    	return getLong(PROP_VALUE_AMOUNT);
    }
    
    public void setValueAmount(Long value) {
    	setLong(PROP_VALUE_AMOUNT, value);
    }
    
    public Long getSaldoAmount() {
    	return getLong(PROP_SALDO_AMOUNT);
    }
    
    public void setSaldoAmount(Long value) {
    	setLong(PROP_SALDO_AMOUNT, value);
    }
    
    public Long getOrigValueAmount() {
    	return getLong(PROP_ORIG_VALUE_AMOUNT);
    }
    
    public void setOrigValueAmount(Long value) {
    	setLong(PROP_ORIG_VALUE_AMOUNT, value);
    }
    
    public Long getChargeValueAmount() {
    	return getLong(PROP_CHARGE_VALUE_AMOUNT);
    }
    
    public void setChargeValueAmount(Long value) {
    	setLong(PROP_CHARGE_VALUE_AMOUNT, value);
    }

	public void setOtherName(String value) {
		setString(PROP_OTHER_NAME, value);
	}
	
	public String getOtherName() {
		return getString(PROP_OTHER_NAME);
	}
	
	public void setOtherName2(String value) {
		setString(PROP_OTHER_NAME2, value);
	}
	
	public String getOtherName2() {
		return getString(PROP_OTHER_NAME2);
	}
	
	public void setOtherIBAN(String value) {
		setString(PROP_OTHER_IBAN, value);
	}
	
	public String getOtherIBAN() {
		return getString(PROP_OTHER_IBAN);
	}
	
	public void setOtherBIC(String value) {
		setString(PROP_OTHER_BIC, value);
	}
	
	public String getOtherBIC() {
		return getString(PROP_OTHER_BIC);
	}
	
	public void setCategoryManual(Long value) {
		setLong(PROP_CATEGORY_MANUAL, value);
	}
	
	public Long getCategoryManual() {
		return getLong(PROP_CATEGORY_MANUAL);
	}
	
	public void setCategoryAuto(Long value) {
		setLong(PROP_CATEGORY_AUTO, value);
	}
	
	public Long getCategoryAuto() {
		return getLong(PROP_CATEGORY_AUTO);
	}
	
    /**
     * Set the {@link #getIdentifier()} of this by other values of this
     */
    private void updateIdentifier() {
		String identifier = Integer.toString((getText() + getUsage()).hashCode());
    	String date =  getValuta() == null ? null : Long.toString(getValuta().getTime());
    	if (date != null) {
    		identifier += "/" + date;
    	}
    	String valueAmount = Long.toString(getValueAmount());
    	if (valueAmount != null) {
    		identifier += "/" + valueAmount;
    	}
    	if (getOtherName() != null) {
    		identifier += "/" + getOtherName().hashCode();
		}
    	setString(PROP_IDENTIFIER, identifier);
    }
    
    public String getIdentifier() {
    	updateIdentifier();
    	return getString(PROP_IDENTIFIER);
    }
    
    @Override
    public boolean save() {
    	updateIdentifier();
    	return super.save();
    }
    
    @Override
    public boolean saveIt() {
    	updateIdentifier();
    	return super.saveIt();
    }
    
    public Double getAmount4UI() {
    	Long amount = getValueAmount();
    	if (amount == null) return null;
		return Utils.round(amount / 100.0, 2);
    }

	public Long getCategory() {
		return getCategoryManual() == null ? getCategoryAuto() : getCategoryManual();
	}
    
}
