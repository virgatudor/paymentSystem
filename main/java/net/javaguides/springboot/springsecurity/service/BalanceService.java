package net.javaguides.springboot.springsecurity.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import net.javaguides.springboot.springsecurity.model.Balance;
import net.javaguides.springboot.springsecurity.model.BalanceAudit;

public interface BalanceService {
	
	Balance findByAccountNumber(String number);

	List<BalanceAudit> findByBalanceId(Long id);
	
	List<BalanceAudit> search(Long id, LocalDateTime d1, LocalDateTime d2);

	Map<BalanceAudit, Double> buildMap(List<BalanceAudit> balances);

	Map<BalanceAudit, Double> buildSearchMap(List<BalanceAudit> balancesSearch);

}
