package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.model.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Long>{
		
	Balance findByAccount_Number(String number); 
	
	 @Modifying
	 @Query("UPDATE Balance b SET b.creditamount = :creditamount"
	 		+ " WHERE b.id = :balanceId")
	 void updateBalanceCredit(@Param("balanceId") Long balanceId, @Param("creditamount") Double creditamount);
	 
	 @Modifying
	 @Query("UPDATE Balance b SET b.debitamount = :debitamount"
	 		+ " WHERE b.id = :balanceId")
	 void updateBalanceDebit(@Param("balanceId") Long balanceId, @Param("debitamount") Double debitamount);

}
