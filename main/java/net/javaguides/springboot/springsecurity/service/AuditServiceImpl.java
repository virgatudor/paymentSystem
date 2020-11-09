package net.javaguides.springboot.springsecurity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.springboot.springsecurity.model.Audit;
import net.javaguides.springboot.springsecurity.repository.AuditRepository;

@Service
@Transactional
public class AuditServiceImpl implements AuditService{
	
	@Autowired
	AuditRepository auditRepository;

	@Override
	public List<Audit> performedByAdmin() {
		return auditRepository.findFirst20ByPerformerOrderByTime("admin");
	}
	
	@Override
	public List<Audit> findAll(){
		return auditRepository.findAll();
	}

	@Override
	public List<Audit> findByPerformedId(Long id) {
		// TODO Auto-generated method stub
		return auditRepository.findFirst20ByPerformedId(id);
	}
	
	@Override
	public List<Audit> findByPerformer(String number){
		return auditRepository.findFirst20ByPerformed(number);
	}
	
	@Override
	public Page<Audit> findByPerformerPageable(String performer, int nr) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(nr, 10);
		return auditRepository.findAllByPerformer(performer, pageable);
	}
	
	@Override
	public Page<Audit> findAllPageable(int nr) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(nr, 10);
		return auditRepository.findAll(pageable);
	}

	
}
