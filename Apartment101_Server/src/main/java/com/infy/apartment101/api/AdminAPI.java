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
import com.infy.apartment101.service.AdminService;

@CrossOrigin
@RestController
@RequestMapping(value = "admin")
public class AdminAPI {
	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminService adminLoginService;

	@Autowired
	private Environment environment;

	static Logger logger = LogManager.getLogger(AdminAPI.class);

	@PostMapping(value = "registerAdmin")
	public ResponseEntity<String> registerAdmin(@RequestBody User admin) throws Exception {
		try {
			logger.info("ADMIN TRYING TO REGISTER. ADMIN EMAIL ID: " + admin.getEmail());

			String registeredWithEmailID = adminService.registerNewAdmin(admin);

			logger.info("ADMIN REGISTRATION SUCCESSFUL. ADMIN EMAIL ID: " + admin.getEmail());

			registeredWithEmailID = environment.getProperty("AdminAPI.ADMIN_REGISTRATION_SUCCESS")
					+ registeredWithEmailID;

			return new ResponseEntity<String>(registeredWithEmailID, HttpStatus.OK);

		} catch (Exception e) {
			if (e.getMessage().contains("Validator")) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, environment.getProperty(e.getMessage()));
			}
			throw new ResponseStatusException(HttpStatus.CONFLICT, environment.getProperty(e.getMessage()));
		}
	}

	@PostMapping(value = "adminLogin")
	public ResponseEntity<User> authenticateSeller(@RequestBody User admin) throws Exception {
		try {
			logger.info("ADMIN TRYING TO LOGIN. ADMIN EMAIL ID: " + admin.getEmail());

			User adminFromDB = adminLoginService.authenticateAdmin(admin.getEmail(), admin.getPassword());

			logger.info("ADMIN LOGIN SUCCESSFUL. ADMIN EMAIL ID: " + admin.getEmail());

			return new ResponseEntity<User>(adminFromDB, HttpStatus.OK);
		} catch (Exception e) {

			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, environment.getProperty(e.getMessage()));
		}
	}
}
