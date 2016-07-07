package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dao.CustomerReportDao;
import com.inventory.model.CustomerReport;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("customerReportServices")
public class CustomerReportServicesImpl implements CustomerReportServices{

	@Autowired
	CustomerReportDao customerReportDao;

	@Override
	public boolean addOrUpdateCustomerReport(CustomerReport customerReport) {
		return customerReportDao.addOrUpdateCustomerReport(customerReport);
	}

	@Override
	public List<CustomerReport> getCustomerReportList() {
		return customerReportDao.getCustomerReportList();
	}
	
}
