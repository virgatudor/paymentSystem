package net.javaguides.springboot.springsecurity.web;

import java.util.NoSuchElementException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.javaguides.springboot.springsecurity.model.MyException;

@ControllerAdvice
public class ExceptionController {
	
	static final String ERROR = "error";
	
	@ExceptionHandler({ NullPointerException.class })
	public String noAccount(Exception ex, Model model) {
		model.addAttribute("msg", "No account with this number");
		return ERROR;
	}

	@ExceptionHandler({ NoSuchElementException.class })
	public String noUser(Exception ex, Model model) {
		model.addAttribute("msg", "No object with this id");
		return ERROR;
	}

	
	@ExceptionHandler({ MyException.class })
	public String transaction(MyException ex, Model model) {
		model.addAttribute("msg", ex.getExceptionMessage());
		return ERROR;
	}
}
