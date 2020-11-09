package net.javaguides.springboot.springsecurity.web;



import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.javaguides.springboot.springsecurity.model.Audit;
import net.javaguides.springboot.springsecurity.model.Balance;
import net.javaguides.springboot.springsecurity.model.BalanceAudit;
import net.javaguides.springboot.springsecurity.model.MyDateComp;
import net.javaguides.springboot.springsecurity.model.MyException;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.repository.AccountRepository;
import net.javaguides.springboot.springsecurity.service.AccountService;
import net.javaguides.springboot.springsecurity.service.AuditService;
import net.javaguides.springboot.springsecurity.service.BalanceService;
import net.javaguides.springboot.springsecurity.service.PaymentService;
import net.javaguides.springboot.springsecurity.service.UserService;



@Controller
public class MainController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	BalanceService balanceService;

    @GetMapping("/")
    public String root(Principal principal) {
    	if("admin".equals(principal.getName())) {
    		
    		return "welcomePageAdmin";
    	}
    	
    	return "welcomePageUser";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }


    
    @GetMapping(value = "/403")
    public String accessDenied() {
 
     
        return "403Page";
    }
    
   
    @GetMapping("account/{number}")
	public String goToAccount(@PathVariable String number,  Model model, Principal principal) {
		//accountService.verifUser(principal, number);
		Balance balance = balanceService.findByAccountNumber(number);
	
		List<BalanceAudit> balances = balanceService.findByBalanceId(balance.getId());
		
		Map<BalanceAudit, Double> balans = balanceService.buildMap(balances);
		
		
		
		String av = String.valueOf(balance.getCreditamount() - balance.getDebitamount());
		
		model.addAttribute("acc", accountService.getByNumber(number));
		
		model.addAttribute("balances", balances);
		model.addAttribute("balans", balans);
		model.addAttribute("available", av);
		model.addAttribute("creditor", paymentService.findByCreditAccount(number));
		model.addAttribute("debitor", paymentService.findByDebitAccount(number));
		model.addAttribute("audits", auditService.findByPerformer(number));

		return "userAccount";
	}
    
    @RequestMapping(value = "account/{number}/search", method= {RequestMethod.GET, RequestMethod.POST})
	public String searchByDate(@RequestParam("from") 
    	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTimeFrom,
    	@RequestParam("to") 
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTimeTo,
		@PathVariable("number") String number, Model model,
		Principal principal) {
		
		
		
		Balance balance = balanceService.findByAccountNumber(number);
		
		List<BalanceAudit> balances = balanceService.findByBalanceId(balance.getId());
		
		Map<BalanceAudit, Double> balans = balanceService.buildMap(balances);
		
		List<BalanceAudit> balancesSearch = balanceService.search(balance.getId(), localDateTimeFrom, localDateTimeTo);
		Map<BalanceAudit, Double> balansSearch = balanceService.buildSearchMap(balancesSearch);
		
		
		
		model.addAttribute("balansSearch", balansSearch);
	
		model.addAttribute("user", userService.findByUsername(principal.getName()));
		model.addAttribute("acc", accountService.findByNumber(number));
		model.addAttribute("number", number);
		
		model.addAttribute("balances", balances);
		model.addAttribute("balans", balans);
		model.addAttribute("available", String.valueOf(balance.getCreditamount() - balance.getDebitamount()));
		model.addAttribute("creditor", paymentService.findByCreditAccount(number));
		model.addAttribute("debitor", paymentService.findByDebitAccount(number));
		model.addAttribute("audits", auditService.findByPerformer(number));
		
		return "userAccount";
	}
    
	
}
