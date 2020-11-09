package net.javaguides.springboot.springsecurity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    User save(UserRegistrationDto registration);
    
    
    public List<User> findAll();
    
    public void deleteUserById(Long id);

	void addUser(User user);
	
	void updateUser(User user);

	Optional<User> findById(Long id);
	
	public List<User> filter(String username);

	User findUserById(Long id);

	boolean existsById(long id);

	void verifId(long id);
	
	boolean existsByUsername(String userame);
	
	void verifUsername(String username);

	void deleteUserByUsername(String username);
    
    
}
