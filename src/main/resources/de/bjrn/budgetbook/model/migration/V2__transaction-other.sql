alter table ACCOUNT_TRANSACTIONS drop other;
alter table ACCOUNT_TRANSACTIONS add otherName varchar(1000);
alter table ACCOUNT_TRANSACTIONS add otherName2 varchar(1000);
alter table ACCOUNT_TRANSACTIONS add otherIBAN varchar(100);
alter table ACCOUNT_TRANSACTIONS add otherBIC varchar(100);
