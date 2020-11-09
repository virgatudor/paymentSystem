package net.javaguides.springboot.springsecurity.service;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.springsecurity.model.BalanceAudit;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.web.dto.PaymentRegistrationDto;

public interface PaymentService {
	
	

	void completePayment(Payment payment);
	
	List<Payment> findByCreditAccount(String number);
	
	List<Payment> findByDebitAccount(String number);
	
	List<Payment> findByStatus(String status);
	
	public double convert(String original, String from, String to, double value);

	double convert(String from, String to, double value);

	void makePayment(Payment payment);

	Payment findById(Long id);

	boolean verifDebitAndAmount(PaymentRegistrationDto paymentDto);

	boolean verifCredit(PaymentRegistrationDto paymentDto);

	void deletePayment(Long id);

	boolean verificareVerify(Payment payment, PaymentRegistrationDto paymentDto);

	void cancelPayment(Long id);

	void approvePayment(Long id);
	
	boolean verifDebitAccount(Long id);

	boolean verifCreditAccount(Long id);

	void sendToAuthorize(Payment payment);

	List<Payment> findByStatusAndPerformer(String status, String performer);
	
	Page<Payment> findAll(int nr);
	
	Long findSize();

	void fastPayment(Payment payment);

	void makePayments(List<Payment> payments);

	void init();



	List<Payment> findMyPayments(long from, long to);

	void completePaymentSimplu(Payment payment);
	
	int sizeByStatus(String status);


	Map<Payment, SimpleEntry<Double, Double>> buildMap(List<Payment> transactions);

	List<Payment> findAllByStatus(int from, int to, String status);

	void verifPage(int pageNumber, int size);
	


	/* void complicatPayments(List<Payment> payments); */

}
