package net.javaguides.springboot.springsecurity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.User;

@Repository
public interface UserCrud extends CrudRepository<User, Long>{

	void deleteByUsername(String username);

}
