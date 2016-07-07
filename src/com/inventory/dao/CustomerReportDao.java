package com.inventory.dao;

import java.util.List;

import com.inventory.model.CustomerReport;

public interface CustomerReportDao {
	boolean addOrUpdateCustomerReport(CustomerReport customerReport);
	List<CustomerReport> getCustomerReportList();
}
