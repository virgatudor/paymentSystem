package net.javaguides.springboot.springsecurity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.Balance;

@Repository
public interface BalanceCrud extends CrudRepository<Balance, Long>{

}
