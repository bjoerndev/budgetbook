/** Use data types supported by h2 and PostgreSQL:
 * String	varchar(50)
 * Date		date			date without time
 * Date		timestamp		date with time
 * int		integer
 * long		bigint
 * double	double precision
 * boolean	boolean
 * 
 * http://h2database.com/html/datatypes.html
 * https://www.postgresql.org/docs/9.6/static/datatype-character.html
 */ 

create table ACCESSES (
	id bigint NOT NULL auto_increment PRIMARY KEY,
	description varchar(200),
	accessType 	varchar(50),
	blz 		varchar(100),
	userName	varchar(100),
	pin			varchar(100),
);
create table ACCOUNTS (
	id bigint NOT NULL auto_increment PRIMARY KEY,
	identifier	varchar(100),
	name		varchar(200),
	name2		varchar(200),
	bic			varchar(100),
	iban		varchar(100),
	number		varchar(100),
	subnumber	varchar(100),
	acctype		varchar(100),
	type0		varchar(200),
);

create table ACCOUNT_TRANSACTIONS (
	id bigint NOT NULL auto_increment PRIMARY KEY,
    accountID	varchar(200),
	valuta		timestamp,
	bdate		timestamp,

	valueAmount	bigint,
	valueCurrency 	varchar(50),
	isStorno	boolean,
	saldoDate 	timestamp,
	saldoAmount bigint,
	saldoCurrency 	varchar(50),

	customerref	varchar(100),
	instref		varchar(100),
    origValueAmount bigint,
    origValueCurrency 	varchar(50),
    chargeValueAmount	bigint,
    chargeValueCurrency varchar(50),

    gvcode		varchar(20),
    additional	varchar(20),
    text		varchar(100),
 	primanota	varchar(100),
    usage		varchar(1000),
    /** Gegenkonto der Buchung (optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>) */
    other		varchar(200),
    addkey		varchar(100),
    isSepa		boolean,
    identifier varchar(100),
);
