package net.javaguides.springboot.springsecurity.performance;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.javaguides.springboot.springsecurity.model.Balance;
import net.javaguides.springboot.springsecurity.model.BalanceAudit;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.repository.BalanceAuditRepository;
import net.javaguides.springboot.springsecurity.repository.BalanceRepository;
import net.javaguides.springboot.springsecurity.service.PaymentService;

public class BalanceManager {

	private ConcurrentHashMap<Long, Lock> balanceLocks = new ConcurrentHashMap<>();
	private BalanceRepository balanceRepository;
	private BalanceAuditRepository balanceAuditRepository;
	private PaymentService paymentService;

	public BalanceManager(BalanceRepository balanceRepository, PaymentService paymentService,
			BalanceAuditRepository balanceAuditRepository) {

		this.balanceRepository = balanceRepository;
		this.paymentService = paymentService;
		this.balanceAuditRepository = balanceAuditRepository;

	}
	
	
	public void doCredit(Long balanceCreditId, String curr, String debitor, String creditor,
			Payment payment) {
		Balance balanceCredit = payment.getCreditAccount().getBalance();
		balanceCredit.setCreditamount(balanceCredit.getCreditamount()
				+ paymentService.convert(curr, debitor, creditor, payment.getAmount()));

		balanceAuditRepository.save(new BalanceAudit(balanceCreditId, LocalDateTime.now(),
				balanceCredit.getCreditamount() - balanceCredit.getDebitamount()));

		balanceRepository.updateBalanceCredit(balanceCreditId, balanceCredit.getCreditamount());
	}
	
	public void doDebit(Long balanceDebitId, String curr, String debitor, String creditor,
			Payment payment) {
		Balance balanceDebit = payment.getDebitAccount().getBalance();

		balanceDebit.setDebitamount(
				balanceDebit.getDebitamount() + paymentService.convert(curr, debitor, payment.getAmount()));

		balanceAuditRepository.save(new BalanceAudit(balanceDebitId, LocalDateTime.now(),
				balanceDebit.getCreditamount() - balanceDebit.getDebitamount()));

		balanceRepository.updateBalanceDebit(balanceDebitId, balanceDebit.getDebitamount());
	}
	
	
	public void credit(Long balanceCreditId, Long balanceDebitId, String curr, String debitor, String creditor,
			Payment payment) {
		if (balanceCreditId < balanceDebitId) {
			Lock lockCredit = balanceLocks.computeIfAbsent(balanceCreditId, k -> new ReentrantLock());
			lockCredit.lock();
			Lock lockDebit = balanceLocks.computeIfAbsent(balanceDebitId, k -> new ReentrantLock());
			lockDebit.lock();
			try {
				doCredit(balanceCreditId, curr, debitor, creditor, payment);
				
				doDebit(balanceDebitId, curr, debitor , creditor, payment);
			} finally {
				lockDebit.unlock();
				lockCredit.unlock();
			}
		} else {
			Lock lockDebit = balanceLocks.computeIfAbsent(balanceDebitId, k -> new ReentrantLock());
			lockDebit.lock();
			Lock lockCredit = balanceLocks.computeIfAbsent(balanceCreditId, k -> new ReentrantLock());
			lockCredit.lock();

			try {
				doDebit(balanceDebitId, curr, debitor , creditor, payment);
				
				doCredit(balanceCreditId, curr, debitor, creditor, payment);

			} finally {
				lockCredit.unlock();
				lockDebit.unlock();
			}
		}

	}

}
