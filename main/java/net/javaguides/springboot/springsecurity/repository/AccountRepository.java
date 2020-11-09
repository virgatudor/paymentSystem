package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.model.AccountView;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.model.User;



@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	
	

	List<Account> findByUser_Username(String username);
	
	
	List<Account> findByUser_Id(Long id);
	List<Account> findAll();
	
	void deleteById(Long id);
	
	Page<Account> findAll(Pageable page);
	
	Account findByName(String name);
	
	Account findByNumber(String number); 
	
	AccountView getByNumber(String number);
	
	boolean existsAccountByNumber(String number);
	
	List<Account> findByStatusNotLike(String status);
	
	@Query(value = "SELECT * FROM \r\n" + 
			"(SELECT *, ROW_NUMBER () OVER () FROM account) x\r\n" + 
			"WHERE ROW_NUMBER BETWEEN ?1 AND ?2\r\n", nativeQuery = true)
	List<Account> findMyAccounts(long from, long to);

}
