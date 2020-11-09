package net.javaguides.springboot.springsecurity.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

public class AccountRegistrationDto {

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@NotEmpty
	private String number;

	@NotEmpty
	private String name;

	@NotEmpty
	private String address;

	@NotEmpty
	private String currency;
	@NotEmpty
	private String status;
	@PositiveOrZero
	private Double amount;
}
