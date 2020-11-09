package net.javaguides.springboot.springsecurity.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.model.AccountView;
import net.javaguides.springboot.springsecurity.model.Audit;
import net.javaguides.springboot.springsecurity.model.Balance;
import net.javaguides.springboot.springsecurity.model.BalanceAudit;
import net.javaguides.springboot.springsecurity.model.MyException;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.repository.AccountCrud;
import net.javaguides.springboot.springsecurity.repository.AccountRepository;
import net.javaguides.springboot.springsecurity.repository.AuditRepository;
import net.javaguides.springboot.springsecurity.repository.BalanceAuditRepository;
import net.javaguides.springboot.springsecurity.repository.BalanceCrud;
import net.javaguides.springboot.springsecurity.repository.BalanceRepository;
import net.javaguides.springboot.springsecurity.web.dto.PaymentRegistrationDto;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	BalanceCrud balanceCrud;

	@Autowired
	UserService userService;

	@Autowired
	BalanceAuditRepository balanceAuditRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountCrud accountCrud;

	@Autowired
	AuditRepository auditRepository;

	@Autowired
	BalanceRepository balanceRepository;
	


	@Override
	@Transactional
	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	@Transactional
	public Account findByName(String name) {
		Account account = accountRepository.findByName(name);
		return account;
	}

	@Override
	@Transactional
	public Account findById(Long id) {
		return accountRepository.findById(id).get();
	}

	@Override
	@Transactional
	public void addAccount(Account account, User user, Double amount) {
		account.setUser(user);

		Balance balance = new Balance(LocalDateTime.now(), amount, (double) 0, account);

		account.setBalance(balance);

		accountCrud.save(account);

		balanceCrud.save(balance);

		auditRepository.saveAndFlush(
				new Audit(LocalDateTime.now(), "CREATE ACCOUNT", "admin", account.getId(), account.getNumber()));

		balanceAuditRepository.save(new BalanceAudit(balance.getId(), LocalDateTime.now(),
				balance.getCreditamount() - balance.getDebitamount()));

	}

	@Override
	public List<Account> findAccountByUsername(String username) {

		return accountRepository.findByUser_Username(username);
	}

	@Override
	public List<Account> findAccountById(Long id) {
		return accountRepository.findByUser_Id(id);
	}

	@Override
	@Transactional
	public void deleteAccountById(Long id) {
		auditRepository.saveAndFlush(new Audit(LocalDateTime.now(), "DELETE ACCOUNT", "admin", id,
				accountRepository.findById(id).get().getNumber()));
		accountRepository.deleteById(id);

	}

	@Override
	@Transactional
	public void updateAccount(Account account) {
		accountCrud.save(account);
		auditRepository.saveAndFlush(
				new Audit(LocalDateTime.now(), "UPDATE ACCOUNT", "admin", account.getId(), account.getNumber()));
	}

	@Override
	public boolean existsAccount(String number) {
		// TODO Auto-generated method stub
		return accountRepository.existsAccountByNumber(number);
	}

	@Override
	public Account findByNumber(String number) {
		// TODO Auto-generated method stub
		return accountRepository.findByNumber(number);
	}

	@Override
	public AccountView getByNumber(String number) {
		return accountRepository.getByNumber(number);
	}

	@Override
	public List<Account> findAllNotClosed() {
		// TODO Auto-generated method stub
		return accountRepository.findByStatusNotLike("CLOSED");
	}
	
	@Override
	public Page<Account> findAllPageable(int nr) {
		Pageable pageable = PageRequest.of(nr, 10);
		return accountRepository.findAll(pageable);
	}

	@Override
	public void generateAccounts() {
		User u = userService.findByUsername("admin");
		// TEST MARE

		for (int i = 1; i <= 100; i++) {

			Account account1 = new Account("TEST" + i, "TEST" + i, "STRADA CLUJ", "EUR", "ACTIVE", u);

			addAccount(account1, u, Double.valueOf(1000000));
		}

		// TEST MIC
		/*
		 * for (int i = 1; i <= 2; i++) {
		 * 
		 * Account account1 = new Account("TEST" + i, "TEST" + i, "STRADA CLUJ", "EUR",
		 * "ACTIVE", u);
		 * 
		 * addAccount(account1, u, Double.valueOf(10000)); }
		 */

	}
	
	@Override
	public int findSize() {
		// TODO Auto-generated method stub
		return accountRepository.findAll().size();
	}
	
	@Override
	public boolean existsByNumber(String number) {
		return accountRepository.existsAccountByNumber(number);
	}
	
	@Override
	public void verifNumber(String number) {
		if(existsByNumber(number) == false) {
			throw new MyException("The account with this id does not exist.");
		}
	}
	
	@Override
	public List<Account> findMyAccounts(long from, long to){
		return accountRepository.findMyAccounts(from, to);
	}
	
	@Override
	public void verifPage(int pageNumber) {
		if(pageNumber <= 0)
			throw new MyException("The page has to be greater than 0.");
		
		if(findSize()%10 == 0) {
			if(pageNumber > findSize()/10) {
				throw new MyException("There are no records available.");
			}	
		}
		else {
			if(pageNumber > findSize()/10+1) {
				throw new MyException("There are no records available.");
			}
		}
	}
	
	@Override
	public void verifUser(Principal principal, String number) {
		if(!principal.getName().contains("admin")) {
			if(!findAccountByUsername(principal.getName()).contains(getByNumber(number))){
				throw new MyException("You are not allowed to see details about this account.");
			}
		}
	}

}
