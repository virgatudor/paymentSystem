package net.javaguides.springboot.springsecurity.performance;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import net.javaguides.springboot.springsecurity.model.Account;
import net.javaguides.springboot.springsecurity.repository.AccountRepository;
import net.javaguides.springboot.springsecurity.web.dto.PaymentRegistrationDto;

public class CSVing {
	
	AccountRepository accountRepository;
	
	Utilitar utils;
	
	
	

	

	public CSVing(AccountRepository accountRepository,Utilitar utils) {
		super();
		this.accountRepository = accountRepository;
		this.utils = utils;
	}


	public void generate(int x) {
		long startTime = System.nanoTime();

		/* ... the code being measured starts ... */

		

		final String CSV_LOCATION = "Payments.csv";

		try {

			// Creating writer class to generate // csv file

			// create a list of Payment
			ArrayList<PaymentRegistrationDto> paymentList = new ArrayList<PaymentRegistrationDto>();

			List<Account> acc = accountRepository.findAll();
	
			for(int i = 0; i < x; i++) {
				
				String[] accounts = utils.getRandomAccount(acc);
			
				
				  PaymentRegistrationDto p = new PaymentRegistrationDto( accounts[0],
				  accounts[1], utils.getRandomAmount().toString(), "RON", "PAY", new Long(i) );
				 
				
				/*
				 * PaymentRegistrationDto p = new PaymentRegistrationDto( "TEST1", "TEST2",
				 * "10", "RON", "PAY", new Long(i) );
				 */
				
				paymentList.add(p);
			}
			
			Writer writer = new FileWriter("Payments.csv");
			StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
			beanToCsv.write(paymentList);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long endTime = System.nanoTime();

		// get difference of two nanoTime values
		long timeElapsed = endTime - startTime;

		System.out.println("Execution time for generating " + x + " transactions: " + timeElapsed / 1000000000 + "." + timeElapsed % 1000000000 + " seconds");

	}

	public List<PaymentRegistrationDto> loadFromCSV() {
		// Hashmap to map CSV data to
		// Bean attributes.
		
		long startTime = System.currentTimeMillis();
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("amount", "amount");
		mapping.put("debitAccount", "debitAccount");
		mapping.put("currency", "Department");
		mapping.put("ref", "ref");
		mapping.put("creditAccount", "creditAccount");
		mapping.put("id", "id");
		// HeaderColumnNameTranslateMappingStrategy
		// for Student class
		HeaderColumnNameTranslateMappingStrategy<PaymentRegistrationDto> strategy = new HeaderColumnNameTranslateMappingStrategy<PaymentRegistrationDto>();
		strategy.setType(PaymentRegistrationDto.class);
		strategy.setColumnMapping(mapping);

		// Create castobaen and csvreader object
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader("Payments.csv"));
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CsvToBean csvToBean = new CsvToBean();

		// call the parse method of CsvToBean
		// pass strategy, csvReader to parse method
		List<PaymentRegistrationDto> list = csvToBean.parse(strategy, csvReader);
		
		

		
		long endTime = System.currentTimeMillis();

		// get difference of two nanoTime values
		long timeElapsed = endTime - startTime;

		

		System.out.println("Execution time for parsing transactions from the the CSV file: " + timeElapsed / 1000F + " seconds");
		
		return list;
	}
	
	

}
