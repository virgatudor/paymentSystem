package net.javaguides.springboot.springsecurity.service;

import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.springsecurity.model.Audit;

public interface AuditService {
	
		List<Audit> performedByAdmin();

		List<Audit> findAll();
		
		List<Audit> findByPerformedId(Long id);

		List<Audit> findByPerformer(String number);

		Page<Audit> findByPerformerPageable(String performer, int nr);



		Page<Audit> findAllPageable(int nr);

}
