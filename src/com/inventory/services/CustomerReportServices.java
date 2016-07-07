package com.inventory.services;

import java.util.List;

import com.inventory.model.CustomerReport;

public interface CustomerReportServices {
	boolean addOrUpdateCustomerReport(CustomerReport customerReport);
	List<CustomerReport> getCustomerReportList();
}
