package net.javaguides.springboot.springsecurity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.Account;

@Repository
public interface AccountCrud extends CrudRepository<Account, Long>{

}
