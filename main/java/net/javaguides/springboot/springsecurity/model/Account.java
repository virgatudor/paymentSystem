package net.javaguides.springboot.springsecurity.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.context.annotation.Lazy;

@Entity
@Table(name="account", schema="public")
public class Account extends BaseEntity{

	 
	
	public Account(String number, String name,  String address,  String currency,
			String status) {
		super();
		this.number = number;
		this.name = name;
		this.address = address;
		this.currency = currency;
		this.status = status;
	}

	@NotEmpty
	private String number;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String address; //4 lines
	
	@NotEmpty
	private String currency;
	
	@NotEmpty
	private String status;//ACTIVE/BLOCKED/BLOCK DEBIT/BLOCK CREDIT/CLOSED
	
	@Lazy 
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable=false)
    private User user;
	
	@OneToOne(cascade = CascadeType.ALL /* , fetch = FetchType.LAZY */)
    @JoinColumn(name = "balance_id", referencedColumnName = "id")
	private Balance balance;
	
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Account [id=" + super.getId() + ", number=" + number + ", name=" + name + ", address=" + address + ", currency="
				+ currency + ", status=" + status + "]";
	}

	
	public Account( String number, String name, String address, String currency, String status, User user) {
		super();
		
		this.number = number;
		this.name = name;
		this.address = address;
		this.currency = currency;
		this.status = status;
		this.user = user;
	}
	
	

	public Account() {
		// TODO Auto-generated constructor stub
	}



	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
