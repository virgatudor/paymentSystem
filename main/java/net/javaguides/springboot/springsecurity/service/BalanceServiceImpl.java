package net.javaguides.springboot.springsecurity.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.springboot.springsecurity.model.Balance;
import net.javaguides.springboot.springsecurity.model.BalanceAudit;
import net.javaguides.springboot.springsecurity.model.MyDateComp;
import net.javaguides.springboot.springsecurity.repository.BalanceAuditRepository;
import net.javaguides.springboot.springsecurity.repository.BalanceRepository;

@Service
@Transactional
public class BalanceServiceImpl implements BalanceService{
	
	@Autowired
	BalanceRepository balanceRepository;
	
	@Autowired
	BalanceAuditRepository balanceAuditRepository;

	@Override
	public Balance findByAccountNumber(String number) {
		// TODO Auto-generated method stub
		return balanceRepository.findByAccount_Number(number);
	}
	
	@Override
	public List<BalanceAudit> findByBalanceId(Long id){
		return balanceAuditRepository.findByBalanceIdOrderByDateAsc(id);
	}

	@Override
	public List<BalanceAudit> search(Long id, LocalDateTime d1, LocalDateTime d2) {
		// TODO Auto-generated method stub
		return balanceAuditRepository.findByBalanceIdAndDateBetween(id, d1, d2);
	}
	
	@Override
	public Map<BalanceAudit, Double> buildMap(List<BalanceAudit> balances){
		Map<BalanceAudit, Double> balans = new TreeMap<>(new MyDateComp());
		if(!balances.isEmpty()) {
			balans.put(balances.get(0), balances.get(0).getAmount());
		}
		Double now = null;
		for(int i = 1; i < balances.size(); i++) {
			now = balances.get(i).getAmount() - balances.get(i-1).getAmount();
			balans.put(balances.get(i), now);
		}
		
		return balans;
	}
	
	@Override
	public Map<BalanceAudit, Double> buildSearchMap(List<BalanceAudit> balancesSearch) {
		Map<BalanceAudit, Double> balansSearch = new TreeMap<>(new MyDateComp());
		if(!balancesSearch.isEmpty()) {
			balansSearch.put(balancesSearch.get(0), balancesSearch.get(0).getAmount());
		}
		
		for(int i = 1; i < balancesSearch.size(); i++) {
			Double nowSearch = balancesSearch.get(i).getAmount() - balancesSearch.get(i-1).getAmount();
			balansSearch.put(balancesSearch.get(i), nowSearch);
		}
		
		return balansSearch;
	}
	
	

}
