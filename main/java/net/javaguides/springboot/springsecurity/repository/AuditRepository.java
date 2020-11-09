package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.Audit;


@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
	
	List<Audit> findByPerformer(String performer);
	
	List<Audit> findAll();
	
	List<Audit> findByPerformedId(Long id);
	
	List<Audit> findByPerformed(String performed);

	List<Audit> findByPerformerOrderByTime(String string);

	List<Audit> findFirst20ByPerformerOrderByTime(String string);

	List<Audit> findFirst20ByPerformedId(Long id);

	List<Audit> findFirst20ByPerformed(String number);

	Page<Audit> findAllByPerformer(String performer, Pageable pageable);

	
}
