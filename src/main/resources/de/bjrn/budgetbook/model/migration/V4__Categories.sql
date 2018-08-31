create table CATEGORIES (
	id bigint NOT NULL auto_increment PRIMARY KEY,
	name varchar(100),
	description varchar(1000),
	parent bigint,
);

alter table ACCOUNT_TRANSACTIONS add categoryManual integer;
alter table ACCOUNT_TRANSACTIONS add categoryAuto integer;

create table RULES (
	id bigint NOT NULL auto_increment PRIMARY KEY,
	category_id bigint, --- See http://javalite.io/one_to_many_associations
	minAmount 	integer,
	maxAmount 	integer,
	name 		varchar(100),
	iban 		varchar(100),
	bic  		varchar(100),
	customerref	varchar(100),
	instref		varchar(100),
);

