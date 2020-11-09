package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	
	void deleteUserById(Long id);
	
	List<User> findAll();

	List<User> findAllByOrderByIdAsc();

	List<User> findAllByUsername(String username);

	boolean existsByUsername(String username);
}
