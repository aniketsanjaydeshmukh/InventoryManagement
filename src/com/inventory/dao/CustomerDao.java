package com.inventory.dao;

import java.util.List;

import com.inventory.model.Customer;


public interface CustomerDao {

	boolean addOrUpdateCustomer(Customer customer);
	boolean deleteCustomer(long customerId);
	List<Customer> customerList(long adminId);
	Customer getCustomerById(long id);
}
