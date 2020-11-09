package net.javaguides.springboot.springsecurity.performance;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.repository.BalanceAuditRepository;
import net.javaguides.springboot.springsecurity.service.AccountService;
import net.javaguides.springboot.springsecurity.service.PaymentService;
import net.javaguides.springboot.springsecurity.web.dto.PaymentRegistrationDto;

public class Utilitar {

	Random r;
	AccountService accountService;
	PaymentService paymentService;
	BalanceAuditRepository balanceAuditRepository;
	
	

	public Utilitar(AccountService accountService, PaymentService paymentService,
			BalanceAuditRepository balanceAuditRepository) {
		r = new Random();
		this.accountService = accountService;
		this.paymentService = paymentService;
		this.balanceAuditRepository = balanceAuditRepository;

	}

	public String[] getRandomAccount(List<Account> accounts) {

		Random r = new Random();
		int randomNumber1 = r.nextInt(accounts.size() - 1);
		int randomNumber2 = r.nextInt(accounts.size() - 1);
		while (randomNumber1 == randomNumber2) {
			randomNumber2 = r.nextInt(accounts.size() - 1);
		}
		//TEST MARE
		
		  return new String[] { accounts.get(randomNumber1).getNumber(),
		  accounts.get(randomNumber2).getNumber() };
		 
		
	}

	public Double getRandomAmount() {
		//TEST MARE
		
		  int randomNumber1 = r.nextInt(20) + 1; return Double.valueOf(randomNumber1);
		 
		

	}

	public ArrayList<Payment> loadPayments(List<PaymentRegistrationDto> payments) {
		long startTime = System.currentTimeMillis();
		ArrayList<Payment> transactions = new ArrayList<>();
		ArrayList<Payment> toReturn = new ArrayList<>();
		int i = 0;
		int c = 0;
		List<Account> conturi = accountService.findAll();
		HashMap<String, Account> alege = new HashMap<>();
		for (Account a : conturi) {
			alege.put(a.getNumber(), a);
		}

		while (i < payments.size()) {
			for (i = c; i < c + 500 && i < payments.size(); i++) {
				Payment p = new Payment(alege.get(payments.get(i).getDebitAccount()),
						alege.get(payments.get(i).getCreditAccount()), Double.valueOf(payments.get(i).getAmount()),
						"EUR", LocalDateTime.now(), "VERIFY", payments.get(i).getRef(), "admin");
				toReturn.add(p);

				transactions.add(p);
			}
			paymentService.makePayments(transactions);

			transactions.clear();

			c += 500;
		}

		long endTime = System.currentTimeMillis();

		// get difference of two nanoTime values
		long timeElapsed = endTime - startTime;

		System.out.println("Execution time for adding the payments in the database: " + timeElapsed / 1000F + " seconds");
		return toReturn;

	}

	public void processPayments(List<Payment> payments) throws IOException {
		long startTime = System.currentTimeMillis();
		System.out.println("-------------------------------------");
		System.out.println("Processing started");
		System.out.println();
		
		Timer timer = new Timer(); 
        TimerTask tt = new TimerTask() {
        	public void run() {
        		int completed = paymentService.sizeByStatus("COMPLETED");
        		int cancelled = paymentService.sizeByStatus("CANCELLED");
        		System.out.println("Time elapsed: " + (System.currentTimeMillis()-startTime)/1000F + " seconds");
        		System.out.println("Completed transactions: " + completed);
        		System.out.println("Cancelled transactions: " + cancelled);
        		System.out.println();
        		if(completed + cancelled == paymentService.findSize()) {
        			System.out.println("-------------------------------------");
        			System.out.println("Final stats");
        			System.out.println("-------------------------------------");
        			System.out.println("Time elapsed: " + (System.currentTimeMillis()-startTime)/1000F + " seconds");
        			System.out.println("Completed transactions: " + completed);
            		System.out.println("Cancelled transactions: " + cancelled);
            		System.out.println("Total transactions: " + paymentService.findSize());
            		System.out.println("Transactions/second: " + paymentService.findSize()/((System.currentTimeMillis()-startTime)/1000F));
        			System.out.println("-------------------------------------");
        			
        			timer.cancel();
        		}	
        	};
        };

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

		File file = new File("Result.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		AtomicInteger atomicInt = new AtomicInteger();
		for (Payment p : payments) {
			Runnable worker = new Simplu(paymentService, p, startTime, payments.size(), atomicInt);
			executor.execute(worker);
		}
		
		timer.schedule(tt, 1000*10, 1000*10);
	}



}
