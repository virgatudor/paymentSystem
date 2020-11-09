package net.javaguides.springboot.springsecurity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springboot.springsecurity.model.BalanceAudit;

public interface BalanceAuditRepository extends JpaRepository<BalanceAudit, Long>{
	
	
	List<BalanceAudit> findByBalanceIdOrderByDateDesc(Long balanceId);

	List<BalanceAudit> findFirst20ByBalanceIdOrderByDateAsc(Long id);
	
	List<BalanceAudit> findFirst20ByBalanceIdAndDateBetween(Long balanceId, LocalDateTime t1, LocalDateTime t2);

	List<BalanceAudit> findByBalanceIdOrderByDateAsc(Long id);

	List<BalanceAudit> findByBalanceIdAndDateBetween(Long id, LocalDateTime d1, LocalDateTime d2);
}
