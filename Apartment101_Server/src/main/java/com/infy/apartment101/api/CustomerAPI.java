package com.infy.apartment101.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.infy.apartment101.model.User;
import com.infy.apartment101.service.CustomerService;

@CrossOrigin
@RestController
@RequestMapping(value = "/customer")
public class CustomerAPI {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private Environment environment;

	static Logger logger = LogManager.getLogger(CustomerAPI.class);

	@PostMapping(value = "/registerCustomer")
	public ResponseEntity<String> registerCustomer(@RequestBody User customer) throws Exception {
		try {
			logger.info("CUSTOMER TRYING TO REGISTER. CUSTOMER EMAIL ID: " + customer.getEmail());

			String registeredWithEmailID = customerService.registerNewCustomer(customer);
			registeredWithEmailID = environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS").toString()
					+ registeredWithEmailID;
			return new ResponseEntity<String>(registeredWithEmailID, HttpStatus.OK);

		} catch (Exception e) {
			if (e.getMessage().contains("Validator")) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, environment.getProperty(e.getMessage()));
			}
			throw new ResponseStatusException(HttpStatus.CONFLICT, environment.getProperty(e.getMessage()));
		}
	}

	@PostMapping(value = "/customerLogin")
	public ResponseEntity<User> authenticateCustomer(@RequestBody User customer) throws Exception {
		try {
			logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAIL ID: " + customer.getEmail());

			User customerfromDB = customerService.authenticateCustomer(customer.getEmail(), customer.getPassword());

			logger.info("CUSTOMER LOGIN SUCCESS, CUSTOMER EMAIL : " + customerfromDB.getEmail());

			return new ResponseEntity<User>(customerfromDB, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, environment.getProperty(e.getMessage()));
		}
	}

}
