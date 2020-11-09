package net.javaguides.springboot.springsecurity.performance;

public class IdGenerator {

	private Long number;
	private boolean turn;
	
	public IdGenerator() {
		this.number = Long.valueOf(16);
		this.turn = true;
	}
	
	public Long getNext() {
		StringBuilder rev = new StringBuilder();
		
		rev.append(Integer.toString(Integer.parseInt(number.toString(), 10), 2));
		
		rev.reverse();
	
		if(this.turn) {
			this.number = Long.valueOf(Integer.toString(Integer.parseInt(rev.toString(), 2), 10))+31;
			this.turn = false;
		}
		else{
			this.number = Long.valueOf(Integer.toString(Integer.parseInt(rev.toString(), 2), 10))+262144;
			this.turn = true;
		}

		return Long.valueOf(Integer.toString(Integer.parseInt(rev.toString(), 2), 10));
		
	}
	
	public Long getNext1(){
		return ~Long.valueOf(this.number);
	}

}
