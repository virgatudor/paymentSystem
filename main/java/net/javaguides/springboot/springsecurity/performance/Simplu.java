package net.javaguides.springboot.springsecurity.performance;


import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.service.PaymentService;

public class Simplu implements Runnable {

	private PaymentService paymentService;
	private Payment payment;
	private long startTime;
	private AtomicInteger at;
	


	public Simplu(PaymentService paymentService, Payment payment, long startTime, int numberOfPayments,
			AtomicInteger at) {
		this.paymentService = paymentService;
		this.payment = payment;
		this.startTime = startTime;
		this.at = at;
	
	}

	@Override
	public void run() {

		paymentService.fastPayment(payment);

		try {

			File file = new File("Result.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("Transaction " + payment.getId() + " has been " + payment.getStatus() + " at "
					+ LocalDateTime.now() + "."); // write to file
			bw.write(System.lineSeparator());

			long end = System.currentTimeMillis();
			// finding the time difference and converting it into seconds

			float sec = (end - startTime) / 1000F;
			bw.write("Time: " + sec + " seconds    ");
			int count = at.incrementAndGet();

			bw.write("Number of transactions: " + count);

			bw.write("      Performance: " + count / sec + " transactions/sec");
			bw.write(System.lineSeparator());

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
