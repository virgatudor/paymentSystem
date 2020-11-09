package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.springboot.springsecurity.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	
	List<Payment> findByDebitAccount_Number(String number);
	
	List<Payment> findByCreditAccount_Number(String number);
	
	List<Payment> findByStatus(String status);
	
	List<Payment> findByStatusAndPerformerNot(String status, String performer);
	
	List<Payment> findAll();
	
	@Query(value= "SELECT COUNT(id) FROM payment WHERE status = ?1", nativeQuery = true)
	int findStatusSize(String status);
	
	@Query(value= "SELECT COUNT(id) FROM payment", nativeQuery = true)
	int findSize();
	
	Page<Payment> findAll(Pageable page);
	
	List<Payment> findAllByOrderByDateAsc();
	
	@Query(value = "SELECT * FROM \r\n" + 
			"(SELECT *, ROW_NUMBER () OVER () FROM payment) x\r\n" + 
			"WHERE ROW_NUMBER BETWEEN ?1 AND ?2\r\n", nativeQuery = true)
    List<Payment> findMyPayments(long from, long to);
	
	@Query(value = "SELECT * FROM \r\n" + 
			"(SELECT *, ROW_NUMBER () OVER () FROM payment WHERE status = ?3) x\r\n" + 
			"WHERE ROW_NUMBER BETWEEN ?1 AND ?2\r\n", nativeQuery = true)
    List<Payment> findMyPaymentsbyStatus(long from, long to, String status);
	
	Page<Payment> findAllByStatus(String status, Pageable page);
	
	 @Modifying
	 @Query("UPDATE Payment p SET p.status = :status WHERE p.id = :paymentId")
	 void updatePayment(@Param("paymentId") Long paymentId, @Param("status") String status);

	List<Payment> findFirst50AllByOrderByDateAsc();

	List<Payment> findLimit20ByCreditAccount_Number(String number);

	List<Payment> findLimit20ByDebitAccount_Number(String number);

	List<Payment> findAllByStatus(String status);

	
}
