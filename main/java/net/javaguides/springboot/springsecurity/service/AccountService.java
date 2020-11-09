package net.javaguides.springboot.springsecurity.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.model.AccountView;
import net.javaguides.springboot.springsecurity.model.User;

public interface AccountService {
	
	List<Account> findAll();
	
	/* List<Account> findAccountsWithUserId(Long id); */
	
	/*
	 * void addAccount(Account account);
	 */
	
	Account findByName(String name);
	
	Account findByNumber(String number);
	
	AccountView getByNumber(String number);
	
	void addAccount(Account account, User user, Double amount);
	
	List<Account> findAccountByUsername(String username);
	
	List<Account> findAccountById(Long id);

	void deleteAccountById(Long id);

	void updateAccount(Account account);

	Account findById(Long id);
	
	boolean existsAccount(String number);

	List<Account> findAllNotClosed();

	void generateAccounts();

	Page<Account> findAllPageable(int nr);

	int findSize();

	/* boolean existsById(long id); */

	List<Account> findMyAccounts(long from, long to);

	/* void verifId(long id); */

	void verifPage(int pageNumber);

	void verifUser(Principal principal, String number);

	boolean existsByNumber(String number);

	void verifNumber(String number);

	/* AccountView findByNumberNou(String number); */
	
}
