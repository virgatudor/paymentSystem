package net.javaguides.springboot.springsecurity.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.context.annotation.Lazy;

@Entity
@Table(name="balance", schema="public")
public class Balance extends BaseEntity{
	
	
	
	private LocalDateTime timestamp;
	
	//creditamount,debitamount,creditcount,debitcount
	private Double creditamount;
	
	private Double debitamount;

	
	
	@OneToOne(mappedBy = "balance" )
    private Account account;
	
	

	public Balance() {
		super();
	}

	public Balance(LocalDateTime timestamp, Double creditamount, Double debitamount, Account account) {
		super();
		this.timestamp = timestamp;
		this.creditamount = creditamount;
		this.debitamount = debitamount;
		this.account = account;
	}

	public Double getCreditamount() {
		return creditamount;
	}

	public void setCreditamount(Double d) {
		this.creditamount = d;
	}

	public Double getDebitamount() {
		return debitamount;
	}

	public void setDebitamount(Double debitamount) {
		this.debitamount = debitamount;
	}
	
	public Double getAvailable() {
		return this.creditamount - this.debitamount;
	}


	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Balance [timestamp=" + timestamp + ", creditamount=" + creditamount + ", debitamount=" + debitamount
				+ ", account=" + account + "]";
	}

	
	
	
}
