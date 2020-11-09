package net.javaguides.springboot.springsecurity.model;

import java.util.Comparator;

public class MyDateComp implements Comparator<BalanceAudit>{

	@Override
	public int compare(BalanceAudit ba1, BalanceAudit ba2) {
		if(ba2.getDate().isBefore(ba1.getDate())) {
			return -1;
		}
		else {
			return 1;
		}
		
	
	

}
}
