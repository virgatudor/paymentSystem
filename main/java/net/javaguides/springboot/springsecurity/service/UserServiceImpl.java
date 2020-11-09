package net.javaguides.springboot.springsecurity.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.springboot.springsecurity.model.Audit;
import net.javaguides.springboot.springsecurity.model.MyException;
import net.javaguides.springboot.springsecurity.model.Role;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.repository.AuditRepository;
import net.javaguides.springboot.springsecurity.repository.UserCrud;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.web.dto.UserRegistrationDto;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserCrud crudRepository;
    
    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Transactional
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    
    @Transactional
    public List<User> filter(String username) {
    	return userRepository.findAllByUsername(username);
    }
    
    @Transactional
    public User save(UserRegistrationDto registration){
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setFullName(registration.getFullName());
        user.setEmail(registration.getEmail());
        user.setAddress(registration.getAddress());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        auditRepository.saveAndFlush(new Audit(LocalDateTime.now(), "ADD", "admin", user.getId(), user.getUsername()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public List<User> findAll(){
    	return userRepository.findAllByOrderByIdAsc();
    }

	@Override
	@Transactional
	public void deleteUserById(Long id) {
		auditRepository.saveAndFlush(new Audit(LocalDateTime.now(), "DELETE USER", "admin", id, userRepository.findById(id).get().getUsername()));
		crudRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public void addUser(User user) {
		auditRepository.saveAndFlush(new Audit(LocalDateTime.now(), "CREATE USER", "admin", user.getId(), user.getUsername()));
		crudRepository.save(user);
	}
	
	@Override
	@Transactional
	public void updateUser(User user) {
		auditRepository.saveAndFlush(new Audit(LocalDateTime.now(), "UPDATE USER", "admin", user.getId(), user.getUsername()));
		crudRepository.save(user);
	}

	@Override
	@Transactional
	public Optional<User> findById(Long id) {
		
		return crudRepository.findById(id);
	}
	
	@Override
	@Transactional
	public User findUserById(Long id) {
		return userRepository.findById(id).get();
	}
	
	@Override
	public boolean existsById(long id) {
		return userRepository.existsById(id);
	}
	
	@Override
	public void verifId(long id) {
		if(existsById(id) == false) {
			throw new MyException("The user with this id does not exist.");
		}
	}
	
	@Override
	public boolean existsByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.existsByUsername(username);
	}
	
	@Override
	public void verifUsername(String username) {
		if(existsByUsername(username) == false) {
			throw new MyException("The user with this username does not exist.");
		}
		
	}
	
	@Override
	public void deleteUserByUsername(String username) {
		auditRepository.saveAndFlush(new Audit(LocalDateTime.now(), "DELETE USER", "admin", userRepository.findByUsername(username).getId(), userRepository.findByUsername(username).getUsername()));
		crudRepository.deleteByUsername(username);
		
	}
    
    
}
