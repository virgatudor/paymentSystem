package net.javaguides.springboot.springsecurity.model;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="balanceaudit", schema="public")
public class BalanceAudit extends BaseEntity{
	
	
	
	 
    private Long balanceId;
	
	
    
	private LocalDateTime date;
	
	private Double amount;
	
	public BalanceAudit() {
		
	}


	public BalanceAudit(Long balanceId, LocalDateTime date, Double amount) {
		super();
		this.balanceId = balanceId;
		
		this.date = date;
		this.amount = amount;
	}

	public Long getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}

	
	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	

	

	
	

}
