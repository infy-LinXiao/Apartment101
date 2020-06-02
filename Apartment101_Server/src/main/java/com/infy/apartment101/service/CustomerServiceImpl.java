package com.infy.apartment101.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.apartment101.dao.CustomerDAO;
import com.infy.apartment101.model.User;
import com.infy.apartment101.validator.CustomerValidator;

@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerDAO customerDAO;

	@Override
	public User authenticateCustomer(String email, String password) throws Exception {

		User customer = null;
		String customerEmailIdFromDAO = customerDAO.authenticateCustomer(email.toLowerCase(), password);
		if (customerEmailIdFromDAO != null) {

			customer = customerDAO.getCustomerByEmailId(customerEmailIdFromDAO);
		} else
			throw new Exception("CustomerService.INVALID_CREDENTIALS");

		return customer;

	}

	@Override
	public String registerNewCustomer(User customer) throws Exception {

		String registeredWithEmailId = null;

		CustomerValidator.validateCustomer(customer);
		Boolean emailAvailable = customerDAO.checkAvailabilityOfEmailId(customer.getEmail().toLowerCase());

		if (emailAvailable) {

			String emailIdToDB = customer.getEmail().toLowerCase();

			customer.setEmail(emailIdToDB);

			registeredWithEmailId = customerDAO.registerNewCustomer(customer);

		}

		else {
			throw new Exception("CustomerService.EMAIL_ID_ALREADY_IN_USE");
		}

		return registeredWithEmailId;
	}

}
