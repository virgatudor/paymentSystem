package net.javaguides.springboot.springsecurity.web.dto;



import javax.validation.constraints.NotEmpty;

import net.javaguides.springboot.springsecurity.model.Account;

public class PaymentRegistrationDto {
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty
    private String debitAccount;
	
	@NotEmpty
	private String creditAccount;
	
	@NotEmpty
	private String amount;
	
	@NotEmpty
	private String currency;
	
	@NotEmpty
	private String ref;

	public PaymentRegistrationDto(@NotEmpty String debitAccount, @NotEmpty String creditAccount,
			@NotEmpty String amount, @NotEmpty String currency, @NotEmpty String ref, @NotEmpty Long id) {
		super();
		this.id = id;
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
		this.currency = currency;
		this.ref = ref;
	}
	
	

	public PaymentRegistrationDto(@NotEmpty String debitAccount, @NotEmpty String creditAccount,
			@NotEmpty String amount, @NotEmpty String currency, @NotEmpty String ref) {
		super();
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.amount = amount;
		this.currency = currency;
		this.ref = ref;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public PaymentRegistrationDto() {
		
	}

	public String getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}

	public String getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
