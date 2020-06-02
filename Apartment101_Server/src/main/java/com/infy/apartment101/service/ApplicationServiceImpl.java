package com.infy.apartment101.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.infy.apartment101.dao.ApplicationDAO;
import com.infy.apartment101.model.Application;

@Service(value = "applicationService")
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
	@Autowired
	ApplicationDAO applicationDAO;

	@Override
	public List<Application> getAllApplications() throws Exception {
		List<Application> aList = applicationDAO.getAllApplications().collect(Collectors.toList());
		if (aList == null)
			throw new Exception("ApplicationService.NULL_APPLICATION_LIST");
		return aList;
	}

	@Override
	public Integer approveApplication(Integer appId) throws Exception {
		Integer result = applicationDAO.approveApplication(appId);
		if (result == 0)
			throw new Exception("ApplicationService.APPLICATION_APPROVED");
		return result;
	}

	@Override
	public String registerNewApp(Application app) throws Exception {
		String add = applicationDAO.registerNewApp(app);
		return add;
	}
	
	@Override
	public List<Application> getAllMyApplications(String email) throws Exception {
		List<Application> aList = applicationDAO.getAllMyApplications(email);
		if (aList == null)
			throw new Exception("ApplicationService.NULL_APPLICATION_LIST");
		return aList;
	}
}
