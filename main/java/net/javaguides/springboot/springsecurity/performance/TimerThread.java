package net.javaguides.springboot.springsecurity.performance;

import net.javaguides.springboot.springsecurity.repository.PaymentRepository;

public class TimerThread implements Runnable{
	
	private volatile boolean exit = false;
	private long startTime;
	private PaymentRepository paymentRepository;
	
	public TimerThread(long startTime, PaymentRepository paymentRepository) {
		this.startTime = startTime;
		this.paymentRepository = paymentRepository;
	}

	@Override
	public void run() {
		 long start = System.currentTimeMillis();
		 
		 while(!exit) {
			 System.out.println(paymentRepository.findAllByStatus("COMPLETED").size());
		 }
	      
	      // finding the time after the operation is executed
	      long end = System.currentTimeMillis();
	      //finding the time difference and converting it into seconds
	      float sec = (end - start) / 1000F; 
	      System.out.println(sec + " seconds");
		
		}
	
	public void stop(){
        exit = true;
    }
	
	
	
	

}
