package net.javaguides.springboot.springsecurity.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.model.Balance;
import net.javaguides.springboot.springsecurity.model.BalanceAudit;
import net.javaguides.springboot.springsecurity.model.MyDateCompPayment;
import net.javaguides.springboot.springsecurity.model.MyException;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.performance.BalanceManager;
import net.javaguides.springboot.springsecurity.repository.AccountRepository;
import net.javaguides.springboot.springsecurity.repository.BalanceAuditRepository;
import net.javaguides.springboot.springsecurity.repository.BalanceRepository;
import net.javaguides.springboot.springsecurity.repository.PaymentRepository;
import net.javaguides.springboot.springsecurity.web.dto.PaymentRegistrationDto;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	BalanceAuditRepository balanceAuditRepository;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	BalanceService balanceService;

	BalanceManager balanceManager;

	@Override
	public void init() {
		this.balanceManager = new BalanceManager(balanceRepository, this, balanceAuditRepository);
	}

	@Override
	public double convert(String original, String from, String to, double value) {
		HashMap<String, HashMap<String, Double>> currency = new HashMap<>();

		HashMap<String, Double> eur = new HashMap<>();

		HashMap<String, Double> usd = new HashMap<>();

		HashMap<String, Double> huf = new HashMap<>();

		HashMap<String, Double> ron = new HashMap<>();

		eur.put("EUR", (double) 1);
		eur.put("USD", 0.89);
		eur.put("RON", 0.21);
		eur.put("HUF", 0.0031);

		usd.put("EUR", 1.13);
		usd.put("USD", (double) 1);
		usd.put("RON", 0.24);
		usd.put("HUF", 0.0035);

		huf.put("EUR", 325.77);
		huf.put("USD", 288.67);
		huf.put("RON", 68.85);
		huf.put("HUF", (double) 1);

		ron.put("EUR", 4.73);
		ron.put("USD", 4.19);
		ron.put("RON", (double) 1);
		ron.put("HUF", 0.015);

		currency.put("EUR", eur);
		currency.put("USD", usd);
		currency.put("RON", ron);
		currency.put("HUF", huf);

		double first = value * currency.get(from).get(original);

		return first * currency.get(to).get(from);

	}

	@Override
	public double convert(String from, String to, double value) {
		HashMap<String, HashMap<String, Double>> currency = new HashMap<>();

		HashMap<String, Double> eur = new HashMap<>();

		HashMap<String, Double> usd = new HashMap<>();

		HashMap<String, Double> huf = new HashMap<>();

		HashMap<String, Double> ron = new HashMap<>();

		eur.put("EUR", (double) 1);
		eur.put("USD", 0.89);
		eur.put("RON", 0.21);
		eur.put("HUF", 0.0031);

		usd.put("EUR", 1.13);
		usd.put("USD", (double) 1);
		usd.put("RON", 0.24);
		usd.put("HUF", 0.0035);

		huf.put("EUR", 325.77);
		huf.put("USD", 288.67);
		huf.put("RON", 68.85);
		huf.put("HUF", (double) 1);

		ron.put("EUR", 4.73);
		ron.put("USD", 4.19);
		ron.put("RON", (double) 1);
		ron.put("HUF", 0.015);

		currency.put("EUR", eur);
		currency.put("USD", usd);
		currency.put("RON", ron);
		currency.put("HUF", huf);

		return value * currency.get(to).get(from);

	}


	@Override
	@Transactional
	public void completePayment(Payment payment) {

		String curr = payment.getCurrency();

		String debitor = payment.getDebitAccount().getCurrency();

		String creditor = payment.getCreditAccount().getCurrency();

		paymentRepository.updatePayment(payment.getId(), "COMPLETED");
		payment.setStatus("COMPLETED");

		balanceManager.credit(payment.getCreditAccount().getBalance().getId(), payment.getDebitAccount().getBalance().getId(), 
				curr, debitor, creditor, payment);
	}
	
	@Override
	@Transactional
	public void completePaymentSimplu(Payment payment) {

		String curr = payment.getCurrency();

		String debitor = payment.getDebitAccount().getCurrency();

		String creditor = payment.getCreditAccount().getCurrency();

		paymentRepository.updatePayment(payment.getId(), "COMPLETED");
		payment.setStatus("COMPLETED");

		Balance balanceDebit = payment.getDebitAccount().getBalance();

		balanceDebit.setDebitamount(
				balanceDebit.getDebitamount() + convert(curr, debitor, payment.getAmount()));

		balanceAuditRepository.save(new BalanceAudit(balanceDebit.getId(), LocalDateTime.now(),
				balanceDebit.getCreditamount() - balanceDebit.getDebitamount()));

		balanceRepository.updateBalanceDebit(balanceDebit.getId(), balanceDebit.getDebitamount());
		
		Balance balanceCredit = payment.getCreditAccount().getBalance();
		
		balanceCredit.setCreditamount(balanceCredit.getCreditamount()
				+ convert(curr, debitor, creditor, payment.getAmount()));

		balanceAuditRepository.save(new BalanceAudit(balanceCredit.getId(), LocalDateTime.now(),
				balanceCredit.getCreditamount() - balanceCredit.getDebitamount()));

		balanceRepository.updateBalanceCredit(balanceCredit.getId(), balanceCredit.getCreditamount());
	}

	@Override
	@Transactional
	public void makePayment(Payment payment) {
		paymentRepository.save(payment);
	}

	@Transactional
	@Override
	public void makePayments(List<Payment> payments) {
		synchronized (this) {
			paymentRepository.saveAll(payments);
		}
	}

	@Override
	public List<Payment> findByCreditAccount(String number) {

		return paymentRepository.findLimit20ByCreditAccount_Number(number);

	}

	@Override
	public List<Payment> findByDebitAccount(String number) {

		return paymentRepository.findLimit20ByDebitAccount_Number(number);
	}

	@Override
	public List<Payment> findByStatus(String status) {

		return paymentRepository.findByStatus(status);
	}

	@Override
	public List<Payment> findByStatusAndPerformer(String status, String performer) {

		return paymentRepository.findByStatusAndPerformerNot(status, performer);
	}

	@Override
	public Payment findById(Long id) {

		return paymentRepository.findById(id).get();
	}

	@Override
	public boolean verifCredit(PaymentRegistrationDto paymentDto) {
		return accountService.existsAccount(paymentDto.getCreditAccount());
	}

	@Override
	public boolean verifDebitAndAmount(PaymentRegistrationDto paymentDto) {
		boolean verifAmount;

		boolean verifDebit = accountService.existsAccount(paymentDto.getDebitAccount());

		Balance debitBalance = balanceService.findByAccountNumber(paymentDto.getDebitAccount());

		String debitCurrency = accountService.findByNumber(paymentDto.getDebitAccount()).getCurrency();

		Double amount = convert(paymentDto.getCurrency(), debitCurrency, Double.valueOf(paymentDto.getAmount()));

		if (debitBalance.getCreditamount() - debitBalance.getDebitamount() >= amount) {
			verifAmount = true;
		} else {
			verifAmount = false;
		}

		return (verifDebit && verifAmount);

	}

	@Override
	@Transactional
	public void deletePayment(Long id) {
		paymentRepository.deleteById(id);
	}

	@Override
	public boolean verificareVerify(Payment payment, PaymentRegistrationDto paymentDto) {

		if (payment.getCreditAccount().getId().equals(Long.valueOf(paymentDto.getCreditAccount()))
				&& payment.getAmount().equals(Double.parseDouble(paymentDto.getAmount()))) {
			return true;
		}

		return false;
	}

	@Override
	public void cancelPayment(Long id) {

		Payment payment = paymentRepository.findById(id).get();
		payment.setStatus("CANCELLED");
		paymentRepository.save(payment);
	}

	@Override
	public void approvePayment(Long id) {
		Payment payment = paymentRepository.findById(id).get();
		payment.setStatus("APPROVE");
		paymentRepository.save(payment);
	}

	@Override
	public boolean verifDebitAccount(Long id) {

		Account acc = findById(id).getDebitAccount();
		if (acc.getStatus().matches("BLOCKED|BLOCK DEBIT|CLOSED"))
			return false;
		System.out.println(acc.getBalance().getCreditamount() - acc.getBalance().getDebitamount() );
		System.out.println(convert(findById(id).getCurrency(), acc.getCurrency(),findById(id).getAmount()));
			return 
			acc.getBalance().getCreditamount() - acc.getBalance().getDebitamount() 
					> 
			convert(findById(id).getCurrency(), acc.getCurrency(),findById(id).getAmount());
	}

	@Override
	public boolean verifCreditAccount(Long id) {

		Account acc = findById(id).getCreditAccount();
		if(acc.getStatus().matches("BLOCKED|BLOCK CREDIT|CLOSED"))
				return false;
		
		return true;

	}

	@Override
	public void sendToAuthorize(Payment payment) {
		payment.setStatus("AUTHORIZE");
		paymentRepository.save(payment);
	}

	@Override
	public Page<Payment> findAll(int nr) {
		Pageable pageable = PageRequest.of(nr, 10);
		return paymentRepository.findAll(pageable);
	}
	
	@Override 
	public List<Payment> findAllByStatus(int from, int to, String status){
		
		return paymentRepository.findMyPaymentsbyStatus(from, to, status);
	}

	@Override
	public void fastPayment(Payment payment) {
		if(payment.getDebitAccount().getBalance().getAvailable() > convert(payment.getCurrency(),
				payment.getDebitAccount().getCurrency(), payment.getAmount())) {

			completePayment(payment);

		} else {
			paymentRepository.updatePayment(payment.getId(), "CANCELLED");
		}
	}
	
	@Override
	public Long findSize() {
		
		return (long) paymentRepository.findSize();
	}
	
	@Override
	public void verifPage(int pageNumber, int size) {
		if(pageNumber <= 0)
			throw new MyException("The page has to be greater than 0.");
		
		if(size%10 == 0) {
			if(pageNumber > size/10) {
				throw new MyException("There are no records available.");
			}	
		}
		else {
			if(pageNumber > size/10+1) {
				throw new MyException("There are no records available.");
			}
		}
	}
	
	@Override
	public List<Payment> findMyPayments(long from, long to){
		return paymentRepository.findMyPayments(from, to);
	}
	
	@Override
	public int sizeByStatus(String status) {
		// TODO Auto-generated method stub
		return paymentRepository.findStatusSize(status);
	} 
	
	@Override
	public Map<Payment, SimpleEntry<Double, Double>> buildMap(List<Payment> transactions){
		Map<Payment, SimpleEntry<Double, Double>> curr = new TreeMap<>(new MyDateCompPayment());

		for (Payment p : transactions) {
				curr.put(p,
						new SimpleEntry<Double, Double>(
								Double.valueOf(convert(p.getCurrency(),
										p.getDebitAccount().getCurrency(), p.getAmount())),
								Double.valueOf(convert(p.getCurrency(),
										p.getCreditAccount().getCurrency(), p.getAmount()))));
			
		}
		
		return curr;
	}
}
