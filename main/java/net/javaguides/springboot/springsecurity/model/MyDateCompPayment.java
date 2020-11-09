package net.javaguides.springboot.springsecurity.model;

import java.util.Comparator;

public class MyDateCompPayment implements Comparator<Payment>{

	@Override
	public int compare(Payment ba1, Payment ba2) {
		if(ba2.getDate().isBefore(ba1.getDate())) {
			return 1;
		}
		else {
			return -1;
		}
		
	}
	
	

}
