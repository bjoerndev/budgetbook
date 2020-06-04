package de.bjrn.budgetbook.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.javalite.activejdbc.Base;

import de.bjrn.budgetbook.logic.BBConfig;

/**
 * Singleton data root 
 * @author bschaedlich
 */
public class BBModel {
	
	private final static String FLYWAY_MIGRATION_PATH = "de/bjrn/budgetbook/model/migration";
	
	private static BBModel instance;
	
	private DataSource dataSource;
	
	private BBModel() {
		dataSource = createDataSource();
		init();
	}
	
	public static synchronized BBModel INSTANCE() {
		if (instance == null) {
			instance = new BBModel();
		}
		return instance;
	}
	
	/**
	 * Shutdown
	 */
	public void close() {
		closeDB();
		instance = null;
	}

	private void closeDB() {
		Base.close();
	}

	private void init() {
		initDB();
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	private void initDB() {
		initFlyway();
	}
	
	private DataSource createDataSource() {
		JdbcDataSource ds = new JdbcDataSource();
		String dbPath = BBConfig.INSTANCE().getFile().getPath();
		ds.setUrl("jdbc:h2:" + dbPath+";CIPHER=AES");
		ds.setUser("sa");
		String pwd = String.valueOf(BBConfig.INSTANCE().getPwd());
		ds.setPassword(pwd + " " + pwd);
		return ds;
	}

	private void initFlyway() {
		Flyway.configure().dataSource(dataSource).locations(FLYWAY_MIGRATION_PATH).load().migrate();
	}

	public List<Access> getAccesses() {
		return Access.findAll();
	}

	/**
	 * @param atx
	 * @return Changed anything in DB?
	 */
	public boolean sync(AccountTransaction atx) {
		if (atx == null) {
			return false;
		}
		if (atx.isNew()) {
			if (!isInDB(atx)) {
				atx.insert();
				return true;
			}
		} else {
			if (atx.isModified()) {
				atx.save();
				return true;
			}
		}
		return false;
	}

	private boolean isInDB(AccountTransaction atx) {
		List<AccountTransaction> res = AccountTransaction.find(AccountTransaction.PROP_IDENTIFIER + "=?", atx.getIdentifier());
		return !res.isEmpty();
	}

	public AccountTransactionList getAccountTransactions() {
		AccountTransactionList txs = new AccountTransactionList(AccountTransaction.findAll());
		txs.sortByDate(false);
		return txs;
	}

	public DateFormat getDateFormat() {
		return new SimpleDateFormat("dd.MM.yyyy");
	}

	public DateFormat getDateTimeFormat() {
		return new SimpleDateFormat("dd.MM.yyyy hh:mm");
	}
	
	public DateFormat getTimeFormat() {
		return new SimpleDateFormat("hh:mm");
	}
	
	private Account getAccount(String accountIdentifier) {
		List<Account> res = Account.find(Account.PROP_IDENTIFIER+"=?", accountIdentifier);
		switch(res.size()) {
		case 0: return null;
		case 1: return res.get(0);
		default:
			throw new RuntimeException("Error: Found " + res.size() + " accounts for "+Account.PROP_IDENTIFIER+"="+accountIdentifier);
		}
	}

	public List<Account> getAccounts() {
		return Account.findAll();
	}

	public void clearAccountTransactions() {
		AccountTransaction.deleteAll();
	}

	public void sync(Account account, Access access) {
		Account check = getAccount(account.getIdentifier());
		if (check != null) {
			check.copyFrom(account);
			account = check;
		}
		account.setAccessID(access.getLongId());
		account.save();
	}

	public List<Account> getAccounts(Access access) {
		Long accessID = access.getLongId();
		List<Account> accounts = new Vector<>();
		for (Account account : getAccounts()) {
			if (accessID.equals(account.getAccessID())) {
				accounts.add(account);
			}
		}
		return accounts;
	}

	/**
	 * @return All categories
	 */
	public List<Category> getCategories() {
		return Category.findAll();
	}
	
	/**
	 * @param parent null (for main categories), or category for direct childs
	 * @return Categories with the given parent
	 */
	public List<Category> getCategories(Category parent) {
		List<Category> cats;
		if (parent == null) {
			cats = Category.find(Category.PROP_PARENT + " is null");
		} else {
			cats = Category.find(Category.PROP_PARENT + "=?", parent.getId());
		}
		cats.sort((o1, o2) -> ObjectUtils.compare(o1.getName(), o2.getName()));
		return cats;
	}

	public Category getCategory(Category base, String name) {
		if (base == null) {
			return Category.findFirst(Category.PROP_NAME + "=? and "+Category.PROP_PARENT + " is null", name);
		}
		return Category.findFirst(Category.PROP_NAME + "=? and "+Category.PROP_PARENT + "=?", name, base.getId());
	}

	/**
	 * @param parent null, or parent category
	 * @param catNew new category
	 */
	public void addCategory(Category parent, Category catNew) {
		catNew.setLong(Category.PROP_PARENT, parent == null ? null : parent.getLongId());
		catNew.save();
	}

	public List<AccountTransaction> getAccountTransactionsWithoutCategory() {
		return AccountTransaction.find(AccountTransaction.PROP_CATEGORY_MANUAL + " is null and "
				+ AccountTransaction.PROP_CATEGORY_AUTO + " is null");
	}

	public AccountTransactionList getAccountTransactionsByCategory(Category cat, boolean recursice) {
		AccountTransactionList ret = new AccountTransactionList();
		if (recursice) {
			for (Category sub : getCategories(cat)) {
				ret.addAll(getAccountTransactionsByCategory(sub, true));
			}
		}
		ret.addAll(AccountTransaction.find(AccountTransaction.PROP_CATEGORY_MANUAL + "=? or "
				+ AccountTransaction.PROP_CATEGORY_AUTO + "=?", cat.getId(), cat.getId()));
		return ret;
	}

	public void addRule(Category cat, Rule rule) {
		rule.save();
		cat.add(rule);
		executeRule(rule);
	}

	private void executeRule(Rule rule) {
		Category cat = rule.getCategory();
		for (AccountTransaction tx : getAccountTransactionsWithoutCategory()) {
			if (isRule(tx, rule)) {
				tx.setCategoryAuto(cat.getLongId());
				tx.save();
			}
		}
	}

	/**
	 * @return Does the given rule fit on the given transaction
	 */
	public boolean isRule(AccountTransaction tx, Rule rule) {
		for (Entry<String, String> entry : new MapPropAccountTransaction2Rule().entrySet()) {
			String propTx = entry.getKey();
			String propRule = entry.getValue();
			String valRule = rule.getString(propRule);
			if (!StringUtils.isEmpty(valRule)) {
				String valTx = getRuleString(tx.getString(propTx));
				if (!valTx.contains(getRuleString(valRule))) {
					return false;
				}
			}
		}
		return true;
	}

	private String getRuleString(String ruleString) {
		return StringUtils.isEmpty(ruleString) ? "" : StringUtils.replace(ruleString.toUpperCase(), "\n", "").trim();
	}

	/**
	 * Delete category with sub-categories and references in transactions
	 * @param cat
	 */
	public void delete(Category cat) {
		for (Category sub : getCategories(cat)) {
			delete(sub);
		}
		List<AccountTransaction> vAuto = AccountTransaction.find(AccountTransaction.PROP_CATEGORY_AUTO+"=?", cat.getId());
		for (AccountTransaction tx : vAuto) {
			tx.setCategoryAuto(null);
			tx.save();
		}
		List<AccountTransaction> vManuell = AccountTransaction.find(AccountTransaction.PROP_CATEGORY_MANUAL+"=?", cat.getId());
		for (AccountTransaction tx : vManuell) {
			tx.setCategoryManual(null);
			tx.save();
		}
		cat.delete();
	}
	
	public Map<Account, List<AccountTransaction>> getAccountTransactionByAccount() {
		Map<Account, List<AccountTransaction>> map = new HashMap<>();
		for (AccountTransaction tx : getAccountTransactions()) {
			String accountID = tx.getAccountID();
			Account account = getAccount(accountID);
			if (account != null) {
				List<AccountTransaction> txs = map.computeIfAbsent(account, k->new Vector<>());
				txs.add(tx);
			} else {
				throw new RuntimeException("No account for accountID " + accountID);
			}
		}
		return map;
	}

	public void execRules() {
		for (Rule rule : getRules()) {
			executeRule(rule);
		}
	}

	public List<Rule> getRules() {
		return Rule.findAll();
	}

	public Category getCategory(Long id) {
		return Category.findById(id);
	}

	public void clearCategoriesAuto() {
		AccountTransaction.updateAll(AccountTransaction.PROP_CATEGORY_AUTO + "=?", (Long) null);
	}

}
