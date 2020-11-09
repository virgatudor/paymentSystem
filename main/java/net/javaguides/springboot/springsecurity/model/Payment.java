
package net.javaguides.springboot.springsecurity.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "payment", schema = "public")
public class Payment{
	
	
	  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	  "hilo_sequence_generator")
	  
	  @GenericGenerator( name = "hilo_sequence_generator", strategy =
	  "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
	  
	  @Parameter(name = "sequence_name", value = "hilo_seqeunce"),
	  
	  @Parameter(name = "initial_value", value = "1"),
	  
	  @Parameter(name = "increment_size", value = "20"),
	  
	  @Parameter(name = "optimizer", value = "hilo") })
	 
	@Id
	private Long id;
	  

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "debitAccount_id", nullable = false, updatable=false)
	private Account debitAccount;
	
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "creditAccount_id", nullable = false, updatable=false)
	private Account creditAccount;

	private Double amount;

	private String currency;
	
	private LocalDateTime date;
	
	private String status;

	private String ref;

	private String performer;

	public Payment() {
		super();
	}

	public Payment(Account debitAccount, Account creditAccount, Double amount, String currency, String status,
			String ref) {
		super();
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
		this.currency = currency;
		this.status = status;
		this.ref = ref;
	}
	
	

	
	  public Payment(Long id, Account debitAccount, Account creditAccount, Double amount, String currency, LocalDateTime date,
			String status, String ref, String performer) {
		super();
		this.id = id;
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
		this.currency = currency;
		this.setDate(date);
		this.status = status;
		this.ref = ref;
		this.performer = performer;
	}
	  
	  public Payment( Account debitAccount, Account creditAccount, Double amount, String currency, LocalDateTime date,
				String status, String ref, String performer) {
			super();
			
			this.debitAccount = debitAccount;
			this.creditAccount = creditAccount;
			this.amount = amount;
			this.currency = currency;
			this.setDate(date);
			this.status = status;
			this.ref = ref;
			this.performer = performer;
		}

	public Payment(Account debitAccount, Account creditAccount, Double amount,
	  String currency , String ref) { super(); this.debitAccount = debitAccount;
	  this.creditAccount = creditAccount; this.amount = amount; this.currency =
	  currency; this.ref = ref;
	  
	  }
	 

	public Payment(Account debitAccount, Account creditAccount, Double amount, String currency, String ref,
			String status, String performer) {
		super();
		
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
		this.currency = currency;
		this.ref = ref;
		this.status = status;
		this.performer = performer;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Account getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(Account debitAccount) {
		this.debitAccount = debitAccount;
	}

	public Account getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(Account creditAccount) {
		this.creditAccount = creditAccount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getPerformer() {
		return performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public Long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", debitAccount=" + debitAccount + ", creditAccount=" + creditAccount + ", date="
				+ date + ", status=" + status + "]";
	}
	
	

}
