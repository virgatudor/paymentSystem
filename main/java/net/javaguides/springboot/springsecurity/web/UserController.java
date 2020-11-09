package net.javaguides.springboot.springsecurity.web;

import java.security.Principal;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;

import net.javaguides.springboot.springsecurity.repository.AccountCrud;
import net.javaguides.springboot.springsecurity.repository.AccountRepository;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.service.AccountService;
import net.javaguides.springboot.springsecurity.service.AuditService;
import net.javaguides.springboot.springsecurity.service.BalanceService;
import net.javaguides.springboot.springsecurity.service.PaymentService;
import net.javaguides.springboot.springsecurity.service.UserService;


@Controller

public class UserController {
	
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	AccountCrud accountCrud;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BalanceService balanceService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired 
	AuditService auditService;
	

	@GetMapping("/accounts")
	public String user(Model model, Principal principal) {

		model.addAttribute("accounts", accountService.findAccountByUsername(principal.getName()));
		model.addAttribute("audits",
				auditService.findByPerformedId(userService.findByUsername(principal.getName()).getId()));
		return "accounts";
	}
	

}
