package com.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dao.CustomerDao;
import com.inventory.model.Customer;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true) 
@Service("customerServices")
public class CustomerServicesImpl implements CustomerServices{

	@Autowired
	CustomerDao customerDao;
	
	@Override
	public boolean addOrUpdateCustomer(Customer customer) {
		return customerDao.addOrUpdateCustomer(customer);
	}

	@Override
	public boolean deleteCustomer(long customerId) {
		return customerDao.deleteCustomer(customerId);
	}

	@Override
	public List<Customer> customerList(long adminId) {
		return customerDao.customerList(adminId);
	}

	@Override
	public Customer getCustomerById(long id) {
		return customerDao.getCustomerById(id);
	}

}
