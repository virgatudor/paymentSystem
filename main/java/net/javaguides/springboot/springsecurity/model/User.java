package net.javaguides.springboot.springsecurity.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user", schema="public", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User extends BaseEntity{
	        @NotEmpty
    private String username;
    
    @NotEmpty
	private String fullName;
    
    @NotEmpty
    private String address;
    
    @NotEmpty
    @Email
    private String email;
    
    @NotEmpty
    private String password;
    
        @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
        )
    private List<Account> accounts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String username, String fullName, String email, String password,String address) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public User(String username, String fullName, String email, String password,String address, Collection<Role> roles) {
    	 this.username = username;
         this.fullName = fullName;
         this.email = email;
         this.password = password;
         this.address = address;
         this.roles = roles;
    }

	
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", firstName='" + username + '\'' +
                ", lastName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "*********" + '\'' +
                ", roles=" + roles +
                '}';
    }
}
