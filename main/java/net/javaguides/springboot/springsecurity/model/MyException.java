package net.javaguides.springboot.springsecurity.model;

import java.util.HashMap;

public class MyException extends RuntimeException{
		private static final long serialVersionUID = 2126875486794454225L;
		private String exceptionMsg;


	public MyException( String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	
	public String getExceptionMessage() {
		return this.exceptionMsg;
	}

	
}
