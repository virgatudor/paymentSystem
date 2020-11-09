package net.javaguides.springboot.springsecurity.web;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.model.MyException;
import net.javaguides.springboot.springsecurity.model.Payment;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.performance.CSVing;
import net.javaguides.springboot.springsecurity.performance.Utilitar;
import net.javaguides.springboot.springsecurity.repository.AccountRepository;
import net.javaguides.springboot.springsecurity.repository.AuditRepository;
import net.javaguides.springboot.springsecurity.repository.BalanceAuditRepository;
import net.javaguides.springboot.springsecurity.repository.PaymentRepository;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.service.AccountService;
import net.javaguides.springboot.springsecurity.service.AuditService;
import net.javaguides.springboot.springsecurity.service.BalanceService;
import net.javaguides.springboot.springsecurity.service.PaymentService;
import net.javaguides.springboot.springsecurity.service.UserService;
import net.javaguides.springboot.springsecurity.web.dto.AccountRegistrationDto;
import net.javaguides.springboot.springsecurity.web.dto.PaymentRegistrationDto;
import net.javaguides.springboot.springsecurity.web.dto.UserRegistrationDto;

@Controller
public class AdminController {

	static final String SAVE = "save";

	static final String CANCEL = "cancel";

	static final String VERIFY = "VERIFY";

	static final String NOT_ALLOWED = "You are not allowed to verify this transaction";

	static final String TRANSACTIONS = "transactions";

	static final String PAYMENT = "payment";

	static final String AUDITS = "audits";

	static final String PERFORMANCE = "performance";
	
	static final String USERS = "users";
	
	static final String ADMIN = "admin";
	
	static final String ACCOUNTS = "accounts";
	
	static final String PAGE_NUMBER = "pageNumber";

	@Autowired
	PaymentService paymentService;

	@Autowired
	BalanceService balanceService;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AuditService auditService;

	@Autowired
	AuditRepository auditRepository;

	@Autowired
	BalanceAuditRepository balanceAuditRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@GetMapping(value = "/users")
	public String admin(Model model) {

		model.addAttribute(AUDITS, auditService.findAllPageable(1).getContent());
		model.addAttribute(USERS, userService.findAll());
		return ADMIN;
	}

	@GetMapping("allAccounts")
	public String allAccounts(Model model) {
		int pageNumber = 1;
		model.addAttribute(PAGE_NUMBER, pageNumber);
		return "redirect:/allAccounts/" + pageNumber;
	}

	@GetMapping("allAccounts/{pageNumber}")
	public String allAccountsPaged(@PathVariable int pageNumber, Model model) {
		accountService.verifPage(pageNumber);
		List<Account> accounts = accountService.findMyAccounts((long) pageNumber * 10 - 9, (long) pageNumber * 10);
		model.addAttribute(ACCOUNTS, accounts);
		model.addAttribute("upperLimit", accountService.findSize() / 10);
		return "allAccounts";
	}

	@GetMapping("delete/{username}")
	public String deleteUser(@PathVariable("username") String username, Model model) {
		userService.verifUsername(username);
		userService.deleteUserByUsername(username);
		model.addAttribute(USERS, userService.findAll());
		model.addAttribute(AUDITS, auditService.findAllPageable(1).getContent());
		return ADMIN;
	}

	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	@GetMapping("addUser")
	public String showSignUpForm() {
		return "addUser";
	}

	@PostMapping("addUser")
	public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result,
			Model model) {

		User existing = userService.findByUsername(userDto.getUsername());
		if (existing != null) {
			result.rejectValue("username", null, "There is already an account registered with this username");
		}

		if (result.hasErrors()) {
			return "addUser";
		}

		userDto.setAddress(userDto.getAddress().replaceAll(",", "\n"));
		userService.save(userDto);
		model.addAttribute(USERS, userService.findAll());
		model.addAttribute(AUDITS, auditService.findAllPageable(1).getContent());
		return ADMIN;
	}

	@GetMapping("update/{username}")
	public String showUpdateForm(@PathVariable("username") String username, Model model) {
		userService.verifUsername(username);
		User user = userService.findByUsername(username);
		model.addAttribute("userr", user);
		return "updateUser";

	}

	@PostMapping("update/{username}")
	public String updateUser(@PathVariable("username") String username, @ModelAttribute("userr") @Valid User user,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "updateUser";
		}
		User auxiliaryUser = userService.findByUsername(user.getUsername());
		user.setRoles(auxiliaryUser.getRoles());
		user.setId(auxiliaryUser.getId());
		userService.updateUser(user);
		model.addAttribute(USERS, userService.findAll());
		model.addAttribute(AUDITS, auditService.findAllPageable(1).getContent());
		return "redirect:/users";
	}

	@GetMapping("user/{username}")
	public String goToUser(@PathVariable("username") String username, Model model, HttpServletResponse response) {
		userService.verifUsername(username);
		User user = userService.findByUsername(username);

		model.addAttribute("user", user);
		model.addAttribute(ACCOUNTS, accountService.findAccountByUsername(username));
		model.addAttribute(AUDITS, auditService.findByPerformedId(user.getId()));

		return "user";

	}

	@GetMapping("/delete/account/{number}")
	public String deleteAccount(@PathVariable("number") String number, Model model) {
		accountService.verifNumber(number);
		accountService.findByNumber(number).setStatus("CLOSED");

		model.addAttribute(ACCOUNTS, accountService.findAll());
		int pageNumber = 1;
		model.addAttribute(PAGE_NUMBER, pageNumber);
		return "allAccounts";
	}

	@ModelAttribute("account")
	public AccountRegistrationDto accountRegistrationDto() {
		return new AccountRegistrationDto();
	}

	@GetMapping("/user/{username}/addAccount")
	public String showRegistrationForm(Model model, @PathVariable("username") String username) {
		userService.verifUsername(username);
		User user = userService.findByUsername(username);

		model.addAttribute("user", user);
		return "addAccount";
	}

	@PostMapping("/user/{username}/addAccount")
	public String registerUserAccount(@PathVariable("username") String username,
			@ModelAttribute("account") @Valid AccountRegistrationDto accountDto, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("user", userService.findByUsername(username));
			return "addAccount";
		}

		User user = userService.findByUsername(username);
		Account account = new Account(accountDto.getNumber(), accountDto.getName(), accountDto.getAddress(),
				accountDto.getCurrency(), accountDto.getStatus(), user);

		accountService.addAccount(account, user, accountDto.getAmount());
		model.addAttribute(ACCOUNTS, accountService.findAccountByUsername(username));
		model.addAttribute(USERS, userService.findAll());

		return "redirect:/user/{username}";
	}

	@GetMapping("updateAccount/{number}")
	public String showUpdateFormAccount(@PathVariable("number") String number, Model model) {

		accountService.verifNumber(number);
		Account account = accountService.findByNumber(number);
		model.addAttribute("account", account);

		return "updateAccount";
	}

	@PostMapping("updateAccount/{number}")
	public String updateAccount(@PathVariable("number") String number, @Valid AccountRegistrationDto accountDto,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "updateAccount";
		}

		Account account = accountService.findByName(accountDto.getName());
		account.setNumber(accountDto.getNumber());
		account.setCurrency(accountDto.getCurrency());
		account.setStatus(accountDto.getStatus());
		account.setAddress(accountDto.getAddress());
		account.setId(account.getId());

		accountService.updateAccount(account);
		model.addAttribute(ACCOUNTS, accountService.findAllNotClosed());

		return "redirect:/allAccounts";
	}

	@ModelAttribute("payment")
	public PaymentRegistrationDto paymentRegistrationDto() {
		return new PaymentRegistrationDto();
	}

	@GetMapping("allTransactions")
	public String viewAllTransactions(Model model) {
		int pageNumber = 1;
		model.addAttribute(PAGE_NUMBER, pageNumber);
		return "redirect:/allTransactions/" + pageNumber;
	}

	@GetMapping("allTransactions/{pageNumber}")
	public String allTransactions(Model model, @PathVariable int pageNumber) {
		
		long size = paymentService.findSize();
		paymentService.verifPage(pageNumber, (int) size);
		List<Payment> transactions = paymentService.findMyPayments(size - (long) (pageNumber - 1) * 10 - 9,
				size - (long) (pageNumber - 1) * 10);
	

		Map<Payment, SimpleEntry<Double, Double>> curr = paymentService.buildMap(transactions);

		model.addAttribute("upperLimit", size / 10);
		model.addAttribute("payments", curr);

		return "allTransactions";
	}

	@GetMapping("/makePayment")
	public String showRegistrationForm(Principal principal, Model model) {
		model.addAttribute(ACCOUNTS, accountService.findAccountByUsername(principal.getName()));

		return PAYMENT;
	}

	@PostMapping("/makePayment")
	public String registerPayment(@ModelAttribute("payment") @Valid PaymentRegistrationDto paymentDto,
			BindingResult result, Model model, Principal principal) {

		if (accountService.existsAccount(paymentDto.getDebitAccount()) == false) {
			result.rejectValue("debitAccount", null, "There is no account with this number");
			return PAYMENT;
		}

		if (accountService.existsAccount(paymentDto.getCreditAccount()) == false) {
			result.rejectValue("creditAccount", null, "There is no account with this number");
			return PAYMENT;
		}

		if (result.hasErrors()) {
			return PAYMENT;
		}

		Payment payment = new Payment(accountService.findByNumber(paymentDto.getDebitAccount()),
				accountService.findByNumber(paymentDto.getCreditAccount()), Double.valueOf(paymentDto.getAmount()),
				paymentDto.getCurrency(), LocalDateTime.now(), VERIFY, paymentDto.getRef(), principal.getName());

		paymentService.makePayment(payment);
		model.addAttribute(AUDITS, auditService.findAllPageable(1).getContent());
		model.addAttribute(USERS, userService.findAll());
		return "redirect:/";

	}

	@GetMapping("/verifyTransactions")
	public String showTransations(Model model, Principal principal) {
		model.addAttribute(TRANSACTIONS, paymentService.findByStatusAndPerformer(VERIFY, principal.getName()));

		return "verifyTransactions";
	}

	@GetMapping("/verifyTransactions/transaction/{id}")
	public String showApproveForm(@PathVariable("id") Long id, Model model, Principal principal) {
		Payment payment = paymentService.findById(id);

		if (principal.getName().equals(payment.getPerformer())) {
			throw new MyException(NOT_ALLOWED);

		}
		model.addAttribute(PAYMENT, payment);
		return "verifyTransaction";
	}

	@PostMapping(value = "/verifyTransactions/transaction/{id}")
	public String verifyTransaction(@PathVariable("id") Long id,
			@ModelAttribute("payment") @Valid PaymentRegistrationDto paymentDto, BindingResult result, Model model,
			@RequestParam(value = "action", required = true) String action, Principal principal) {

		if (result.hasErrors()) {
			return "verifyTransaction";
		}

		switch (action) {
		case SAVE:
			if (paymentService.verificareVerify(paymentService.findById(id), paymentDto)) {

				paymentService.approvePayment(id);
			}

			break;

		case CANCEL:
			paymentService.cancelPayment(id);
			break;
		default:
			break;
		}

		model.addAttribute(TRANSACTIONS, paymentService.findByStatus(VERIFY));
		return "verifyTransactions";
	}

	@GetMapping("/approveTransactions")
	public String showApproveTransactions(Model model, Principal principal) {
		model.addAttribute(TRANSACTIONS, paymentService.findByStatusAndPerformer("APPROVE", principal.getName()));
		return "approveTransactions";
	}

	@GetMapping("/approveTransactions/transaction/{id}")
	public String showApprovalForm(@PathVariable("id") Long id, Model model, Principal principal) {
		Payment payment = paymentService.findById(id);
		if (principal.getName().equals(payment.getPerformer())) {
			throw new MyException("You are not allowed to approve this transaction.");

		}
		model.addAttribute(PAYMENT, payment);
		return "approveTransaction";
	}

	@PostMapping(value = "/approveTransactions/transaction/{id}")
	public String approveTransaction(@PathVariable("id") Long id,
			@ModelAttribute("payment") @Valid PaymentRegistrationDto paymentDto, BindingResult result, Model model,
			@RequestParam(value = "action", required = true) String action) {
		if (result.hasErrors()) {
			return "approveTransaction";
		}
		switch (action) {
		case SAVE:
			Payment payment = paymentService.findById(id);
			
			if (paymentService.verifCreditAccount(id) == true && paymentService.verifDebitAccount(id) == true) {
				paymentService.completePaymentSimplu(payment);
			} else {
				paymentService.sendToAuthorize(payment);
			}

			break;

		case CANCEL:
			paymentService.cancelPayment(id);
			break;
		default:
			break;
		}

		model.addAttribute(TRANSACTIONS, paymentService.findByStatus("APPROVE"));
		return "approveTransactions";
	}

	@GetMapping("/authorizeTransactions")
	public String showAuthorizeTransaction(Model model, Principal principal) {
		model.addAttribute(TRANSACTIONS, paymentService.findByStatusAndPerformer("AUTHORIZE", principal.getName()));
		return "authorizeTransactions";
	}

	@GetMapping("/authorizeTransactions/transaction/{id}")
	public String showAuthorizeForm(@PathVariable("id") Long id, Model model, Principal principal) {
		Payment payment = paymentService.findById(id);
		if (principal.getName().equals(payment.getPerformer())) {
			throw new MyException("You are not allowed to authorize this transaction.");

		}
		model.addAttribute(PAYMENT, payment);
		return "authorizeTransaction";
	}

	@PostMapping(value = "/authorizeTransactions/transaction/{id}")
	public String authorizeTransactionForm(@PathVariable("id") Long id,
			@ModelAttribute("payment") @Valid PaymentRegistrationDto paymentDto, BindingResult result, Model model,
			@RequestParam(value = "action", required = true) String action) {
		
		if (result.hasErrors()) {
			return "authorizeTransaction";
		}
		
		switch (action) {
		case SAVE:
			Payment payment = paymentService.findById(id);
			boolean da = paymentService.verifDebitAccount(id);
			boolean ca = paymentService.verifCreditAccount(id);

			
			if (da == true && ca == true) {
				paymentService.completePaymentSimplu(payment);
			}

			break;

		case CANCEL:
			paymentService.cancelPayment(id);
			break;
		default:
			break;
		}

		model.addAttribute(TRANSACTIONS, paymentService.findByStatus("AUTHORIZE"));
		return "authorizeTransactions";
	}

	@GetMapping("/performance")
	public String performancePage() {
		return PERFORMANCE;
	}

	@GetMapping("/generateAccounts/")
	public String generateAccounts() {
		accountService.generateAccounts();
		return PERFORMANCE;
	}

	@GetMapping("/go/")
	public String doPerformance(Model model) throws IOException {

		Utilitar util = new Utilitar(accountService, paymentService, balanceAuditRepository);
		CSVing c = new CSVing(accountRepository, util);

		int numberOfTransactions = 10000;
		c.generate(numberOfTransactions);

		ArrayList<Payment> paymentList = util.loadPayments(c.loadFromCSV());
		paymentService.init();

		util.processPayments(paymentList);

		return PERFORMANCE;
	}

	@GetMapping("searchAccount")
	public String searchAccount(Model model) {
		return "searchAccount";
	}

	@GetMapping("searchAccount/result")
	public String searchAccountResult(@RequestParam("accountNumber") String accountNumber, Model model) {
		String msg = null;
		Account account = accountService.findByNumber(accountNumber);
		if (account == null) {
			msg = "There is no account with this number.";
		}
		model.addAttribute("acc", account);
		model.addAttribute("msg", msg);
		return "searchAccount";
	}

	@GetMapping("searchUser")
	public String searchUser(Model model) {
		return "searchUser";
	}

	@GetMapping("searchUser/result")
	public String searchUserResult(@RequestParam("username") String username, Model model) {
		String msg = null;
		User user = userService.findByUsername(username);
		if (user == null) {
			msg = "There is no user with this username.";
		}
		model.addAttribute("us", user);
		model.addAttribute("msg", msg);
		return "searchUser";
	}

	@GetMapping("searchTransaction")
	public String searchTransaction(Model model) {
		return "searchTransaction";
	}

	@GetMapping("searchTransaction/result") 
	public String searchTransactionResult(@RequestParam("status") String status, Model model) {
		
		int size = paymentService.sizeByStatus(status);
		
		String msg = null;
		
		if(size > 0) {
			int pageNumber = 1;
			model.addAttribute(PAGE_NUMBER, pageNumber);
			model.addAttribute("status", status);
			return "redirect:/searchTransaction/" + status + "/" + pageNumber;
		}
		else {
			msg = "There are no transactions with this status.";
		}
	
	
		model.addAttribute("msg" , msg);
		return "searchTransaction";
	}
	
	@GetMapping("searchTransaction/{status}/{pageNumber}")
	public String searchTransactionResultPaged(@PathVariable("status") String status, @PathVariable("pageNumber")
		int pageNumber, Model model) {
		
		int size = paymentService.sizeByStatus(status);
		paymentService.verifPage(pageNumber, size); 
		List<Payment> transactions = paymentService.findAllByStatus(size - (pageNumber - 1) * 10 - 9,
				size - (pageNumber - 1) * 10, "COMPLETED");
	

		Map<Payment, SimpleEntry<Double, Double>> curr = paymentService.buildMap(transactions);

		model.addAttribute("upperLimit", size / 10);
		model.addAttribute("payments", curr);
		model.addAttribute(PAGE_NUMBER, pageNumber);
		model.addAttribute("status", status);
		return "searchResult";
	}
	

}
